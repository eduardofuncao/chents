package br.com.fiap.chents.service;

import br.com.fiap.chents.entity.Alert;
import br.com.fiap.chents.entity.Location;
import br.com.fiap.chents.entity.Position;
import br.com.fiap.chents.entity.User;
import br.com.fiap.chents.entity.dto.AlertDTO;
import br.com.fiap.chents.entity.dto.AlertMessage;
import br.com.fiap.chents.entity.mapper.AlertMapper;
import br.com.fiap.chents.repository.AlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlertServiceTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private AlertMapper alertMapper;

    @Mock
    private AlertMessageProducer alertMessageProducer;

    @InjectMocks
    private AlertService alertService;

    private Alert alert;
    private AlertDTO alertDTO;
    private User user;
    private Location location;
    private Position position;

    @BeforeEach
    void setUp() {
        // Setup common test data
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setUsername("testuser");

        location = new Location();
        location.setId(1L);
        location.setCity("São Paulo");
        location.setState("SP");

        position = new Position();
        position.setId(1L);
        position.setLatitude(-23.5505);
        position.setLongitude(-46.6333);

        alert = new Alert();
        alert.setId(1L);
        alert.setMessage("Test flood alert");
        alert.setCreation(LocalDateTime.now());
        alert.setUser(user);
        alert.setLocation(location);
        alert.setPosition(position);

        alertDTO = new AlertDTO();
        alertDTO.setId(1L);
        alertDTO.setMessage("Test flood alert");
        alertDTO.setCreation(LocalDateTime.now());
        alertDTO.setUserId(1L);
        alertDTO.setLocationId(1L);
        alertDTO.setPositionId(1L);
    }

    @Test
    void getAllAlerts_shouldReturnAllAlerts() {
        // Arrange
        List<Alert> alerts = Arrays.asList(alert);
        when(alertRepository.findAll()).thenReturn(alerts);
        when(alertMapper.toDTO(any(Alert.class))).thenReturn(alertDTO);

        // Act
        List<AlertDTO> result = alertService.getAllAlerts();

        // Assert
        assertEquals(1, result.size());
        assertEquals(alertDTO, result.get(0));
        verify(alertRepository).findAll();
    }

    @Test
    void getAlertById_whenAlertExists_shouldReturnAlert() {
        // Arrange
        when(alertRepository.findById(1L)).thenReturn(Optional.of(alert));
        when(alertMapper.toDTO(alert)).thenReturn(alertDTO);

        // Act
        Optional<AlertDTO> result = alertService.getAlertById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(alertDTO, result.get());
        verify(alertRepository).findById(1L);
    }

    @Test
    void getAlertById_whenAlertDoesNotExist_shouldReturnEmpty() {
        // Arrange
        when(alertRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<AlertDTO> result = alertService.getAlertById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(alertRepository).findById(999L);
    }

    @Test
    void createAlert_shouldSaveAlertAndSendMessage() {
        // Arrange
        when(alertMapper.toEntity(alertDTO)).thenReturn(alert);
        when(alertRepository.save(alert)).thenReturn(alert);
        when(alertMapper.toDTO(alert)).thenReturn(alertDTO);

        // Act
        AlertDTO result = alertService.createAlert(alertDTO);

        // Assert
        assertEquals(alertDTO, result);
        verify(alertRepository).save(alert);
        verify(alertMapper).toEntity(alertDTO);
        verify(alertMapper).toDTO(alert);

        // Verify that the message was sent
        ArgumentCaptor<AlertMessage> messageCaptor = ArgumentCaptor.forClass(AlertMessage.class);
        verify(alertMessageProducer).sendOcorrenciaMessage(messageCaptor.capture());

        AlertMessage capturedMessage = messageCaptor.getValue();
        assertEquals(alert.getId(), capturedMessage.getId());
        assertEquals(alert.getMessage(), capturedMessage.getMessage());
        assertEquals(alert.getUser().getName(), capturedMessage.getUserName());
    }

    @Test
    void deleteAlert_whenAlertExists_shouldDeleteAndReturnTrue() {
        // Arrange
        when(alertRepository.existsById(1L)).thenReturn(true);
        doNothing().when(alertRepository).deleteById(1L);

        // Act
        boolean result = alertService.deleteAlert(1L);

        // Assert
        assertTrue(result);
        verify(alertRepository).existsById(1L);
        verify(alertRepository).deleteById(1L);
    }

    @Test
    void deleteAlert_whenAlertDoesNotExist_shouldReturnFalse() {
        // Arrange
        when(alertRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean result = alertService.deleteAlert(999L);

        // Assert
        assertFalse(result);
        verify(alertRepository).existsById(999L);
        verify(alertRepository, never()).deleteById(999L);
    }

    @Test
    void getAlertsNearby_shouldReturnNearbyAlerts() {
        // Arrange
        double latitude = -23.5505;
        double longitude = -46.6333;
        double radiusKm = 5.0;
        LocalDateTime since = LocalDateTime.now().minusDays(1);

        List<Alert> nearbyAlerts = Arrays.asList(alert);
        when(alertRepository.findAlertsNearby(latitude, longitude, radiusKm, since)).thenReturn(nearbyAlerts);
        when(alertMapper.toDTO(alert)).thenReturn(alertDTO);

        // Act
        List<AlertDTO> result = alertService.getAlertsNearby(latitude, longitude, radiusKm, since);

        // Assert
        assertEquals(1, result.size());
        assertEquals(alertDTO, result.get(0));
        verify(alertRepository).findAlertsNearby(latitude, longitude, radiusKm, since);
    }
}
