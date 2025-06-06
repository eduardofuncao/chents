package br.com.fiap.chents.service;

import br.com.fiap.chents.entity.User;
import br.com.fiap.chents.entity.dto.UserDTO;
import br.com.fiap.chents.entity.mapper.UserMapper;
import br.com.fiap.chents.exception.AlertAlreadyCreatedByUserException;
import br.com.fiap.chents.repository.AlertRepository;
import br.com.fiap.chents.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        // Setup common test data
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setUsername("testuser");
        user.setPassword("encoded_password");

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("Test User");
        userDTO.setUsername("testuser");
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        // Act
        List<UserDTO> result = userService.getAllUsers();

        // Assert
        assertEquals(1, result.size());
        assertEquals(userDTO, result.get(0));
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_whenUserExists_shouldReturnUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // Act
        Optional<UserDTO> result = userService.getUserById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userDTO, result.get());
        verify(userRepository).findById(1L);
    }

    @Test
    void createUser_shouldEncodePasswordAndSaveUser() {
        // Arrange
        String rawPassword = "password123";
        String encodedPassword = "encoded_password";

        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.createUser(userDTO, rawPassword);

        // Assert
        assertEquals(userDTO, result);
        verify(passwordEncoder).encode(rawPassword);
        verify(userRepository).save(user);
        verify(userMapper).toEntity(userDTO);
        verify(userMapper).toDTO(user);
    }

    @Test
    void deleteUser_whenUserExistsAndHasNoAlerts_shouldDeleteAndReturnTrue() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(alertRepository.existsByUser(user)).thenReturn(false);
        doNothing().when(userRepository).deleteById(1L);

        // Act
        boolean result = userService.deleteUser(1L);

        // Assert
        assertTrue(result);
        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_whenUserHasAlerts_shouldThrowException() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(alertRepository.existsByUser(user)).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(AlertAlreadyCreatedByUserException.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("There is an alert associated with the user. The user cannot be deleted.", exception.getMessage());
        verify(userRepository, never()).deleteById(1L);
    }
}
