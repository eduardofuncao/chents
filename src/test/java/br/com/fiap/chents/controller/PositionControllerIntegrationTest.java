package br.com.fiap.chents.controller;

import br.com.fiap.chents.entity.Position;
import br.com.fiap.chents.entity.User;
import br.com.fiap.chents.entity.dto.PositionDTO;
import br.com.fiap.chents.repository.UserRepository;
import br.com.fiap.chents.service.PositionService;
import br.com.fiap.chents.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PositionController.class)
public class PositionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private PositionService positionService;

    @MockBean
    private UserRepository userRepository;

    private User user;
    private Position position;
    private PositionDTO positionDTO;

    @BeforeEach
    void setUp() {
        // Setup common test data
        position = new Position();
        position.setId(1L);
        position.setLatitude(-23.5505);
        position.setLongitude(-46.6333);

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setUsername("testuser");
        user.setPosition(position);

        positionDTO = new PositionDTO();
        positionDTO.setId(1L);
        positionDTO.setLatitude(-23.5505);
        positionDTO.setLongitude(-46.6333);
    }

    @Test
    @WithMockUser(username = "testuser")
    void getUserPosition_whenUserExists_shouldReturnPosition() throws Exception {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Act & Assert
        mockMvc.perform(get("/api/user/position"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latitude").value(-23.5505))
                .andExpect(jsonPath("$.longitude").value(-46.6333));
    }

    @Test
    @WithMockUser(username = "testuser")
    void updateUserPosition_withValidData_shouldUpdatePosition() throws Exception {
        // Arrange
        Map<String, Double> positionData = new HashMap<>();
        positionData.put("latitude", -23.5505);
        positionData.put("longitude", -46.6333);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(positionService.getPositionById(1L)).thenReturn(Optional.of(positionDTO));
        when(positionService.updatePosition(1L, positionDTO)).thenReturn(Optional.of(positionDTO));

        // Act & Assert
        mockMvc.perform(post("/api/user/position/update")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(positionData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(username = "nonexistentuser")
    void updateUserPosition_whenUserDoesNotExist_shouldReturnBadRequest() throws Exception {
        // Arrange
        Map<String, Double> positionData = new HashMap<>();
        positionData.put("latitude", -23.5505);
        positionData.put("longitude", -46.6333);

        when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/user/position/update")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(positionData)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void updateUserPosition_withInvalidData_shouldReturnBadRequest() throws Exception {
        // Arrange
        Map<String, Double> positionData = new HashMap<>();
        // Missing longitude
        positionData.put("latitude", -23.5505);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Act & Assert
        mockMvc.perform(post("/api/user/position/update")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(positionData)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid position data"));
    }
}
