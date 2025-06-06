package br.com.fiap.chents.controller;

import br.com.fiap.chents.entity.dto.AlertDTO;
import br.com.fiap.chents.entity.dto.LocationDTO;
import br.com.fiap.chents.entity.dto.UserDTO;
import br.com.fiap.chents.service.AlertService;
import br.com.fiap.chents.service.LocationService;
import br.com.fiap.chents.service.PositionService;
import br.com.fiap.chents.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlertController.class)
public class AlertControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlertService alertService;

    @MockBean
    private LocationService locationService;

    @MockBean
    private UserService userService;

    @MockBean
    private PositionService positionService;

    private AlertDTO alertDTO;
    private UserDTO userDTO;
    private LocationDTO locationDTO;

    @BeforeEach
    void setUp() {
        // Setup common test data
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("Test User");
        userDTO.setUsername("testuser");
        userDTO.setPositionId(1L);

        locationDTO = new LocationDTO();
        locationDTO.setId(1L);
        locationDTO.setCity("São Paulo");
        locationDTO.setState("SP");

        alertDTO = new AlertDTO();
        alertDTO.setId(1L);
        alertDTO.setMessage("Test flood alert");
        alertDTO.setCreation(LocalDateTime.now());
        alertDTO.setUserId(1L);
        alertDTO.setLocationId(1L);
        alertDTO.setPositionId(1L);
    }

    @Test
    @WithMockUser
    void listAlerts_shouldDisplayAllAlerts() throws Exception {
        // Arrange
        when(alertService.getAllAlerts()).thenReturn(Arrays.asList(alertDTO));
        when(locationService.getAllLocations()).thenReturn(Arrays.asList(locationDTO));
        when(userService.getAllUsers()).thenReturn(Arrays.asList(userDTO));

        // Act & Assert
        mockMvc.perform(get("/alerts"))
                .andExpect(status().isOk())
                .andExpect(view().name("alerts/list"))
                .andExpect(model().attributeExists("alerts"))
                .andExpect(model().attributeExists("locationMap"))
                .andExpect(model().attributeExists("userMap"));
    }

    @Test
    @WithMockUser
    void showCreateAlertForm_shouldDisplayForm() throws Exception {
        // Arrange
        when(locationService.getAllLocations()).thenReturn(Arrays.asList(locationDTO));
        when(userService.getAllUsers()).thenReturn(Arrays.asList(userDTO));

        // Act & Assert
        mockMvc.perform(get("/alerts/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("alerts/form"))
                .andExpect(model().attributeExists("alert"))
                .andExpect(model().attributeExists("locations"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @WithMockUser
    void createAlert_withValidData_shouldRedirectToAlertsList() throws Exception {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(Optional.of(userDTO));
        when(alertService.createAlert(any(AlertDTO.class))).thenReturn(alertDTO);

        // Act & Assert
        mockMvc.perform(post("/alerts/new")
                        .with(csrf())
                        .param("message", "Test flood alert")
                        .param("userId", "1")
                        .param("locationId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/alerts"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @WithMockUser
    void showEditAlertForm_whenAlertExists_shouldDisplayForm() throws Exception {
        // Arrange
        when(alertService.getAlertById(1L)).thenReturn(Optional.of(alertDTO));
        when(locationService.getAllLocations()).thenReturn(Arrays.asList(locationDTO));
        when(userService.getAllUsers()).thenReturn(Arrays.asList(userDTO));

        // Act & Assert
        mockMvc.perform(get("/alerts/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("alerts/form"))
                .andExpect(model().attributeExists("alert"))
                .andExpect(model().attributeExists("locations"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @WithMockUser
    void deleteAlert_whenAlertExists_shouldRedirectWithSuccessMessage() throws Exception {
        // Arrange
        when(alertService.deleteAlert(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/alerts/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/alerts"))
                .andExpect(flash().attributeExists("successMessage"));
    }
}
