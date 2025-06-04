package br.com.fiap.chents.service;

import br.com.fiap.chents.entity.dto.AlertDTO;
import br.com.fiap.chents.entity.Alert;
import br.com.fiap.chents.entity.dto.AlertMessage;
import br.com.fiap.chents.repository.AlertRepository;
import br.com.fiap.chents.entity.mapper.AlertMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;
    private final AlertMessageProducer alertMessageProducer;

    public AlertService(AlertRepository alertRepository, AlertMapper alertMapper, AlertMessageProducer alertMessageProducer) {
        this.alertRepository = alertRepository;
        this.alertMapper = alertMapper;

        this.alertMessageProducer = alertMessageProducer;
    }

    public AlertDTO createAlert(AlertDTO alertDTO) {
        Alert alert = alertMapper.toEntity(alertDTO);
        Alert savedAlert = alertRepository.save(alert);

        AlertMessage message = new AlertMessage();
        message.setId(savedAlert.getId());
        message.setCreation(savedAlert.getCreation());
        message.setMessage(savedAlert.getMessage());
        message.setUserName(savedAlert.getUser().getName());
        message.setCity(savedAlert.getLocation().getCity());
        message.setMessageCreation(LocalDateTime.now());
        message.setLatitude(savedAlert.getPosition().getLatitude());
        message.setLongitude(savedAlert.getPosition().getLongitude());
        alertMessageProducer.sendOcorrenciaMessage(message);

        return alertMapper.toDTO(savedAlert);
    }

    public List<AlertDTO> getAllAlerts() {
        return alertRepository.findAll().stream()
                .map(alertMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<AlertDTO> getAlertById(Long id) {
        return alertRepository.findById(id).map(alertMapper::toDTO);
    }

    public Optional<AlertDTO> updateAlert(Long id, AlertDTO alertDTO) {
        return alertRepository.findById(id)
                .map(existing -> {
                    Alert updated = alertMapper.toEntity(alertDTO);
                    updated.setId(id);
                    Alert saved = alertRepository.save(updated);
                    return alertMapper.toDTO(saved);
                });
    }

    public boolean deleteAlert(Long id) {
        if (alertRepository.existsById(id)) {
            alertRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<AlertDTO> getAlertsNearby(double lat, double lng, double radiusKm, LocalDateTime since) {
        List<Alert> alerts = alertRepository.findAlertsNearby(lat, lng, radiusKm, since);
        return alerts.stream().map(alertMapper::toDTO).collect(Collectors.toList());
    }
}