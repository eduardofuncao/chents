package br.com.fiap.chents.service;

import br.com.fiap.chents.entity.User;
import br.com.fiap.chents.entity.dto.UserDTO;
import br.com.fiap.chents.entity.mapper.UserMapper;
import br.com.fiap.chents.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO createUser(UserDTO userDTO, String rawPassword) {
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(rawPassword));
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDTO);
    }

    public Optional<UserDTO> updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id)
                .map(existing -> {
                    User updated = userMapper.toEntity(userDTO);
                    updated.setId(id);
                    updated.setPassword(existing.getPassword());
                    User saved = userRepository.save(updated);
                    return userMapper.toDTO(saved);
                });
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
