package br.com.fiap.chents.service;

import br.com.fiap.chents.entity.dto.AlertDTO;
import br.com.fiap.chents.entity.Alert;
import br.com.fiap.chents.repository.AlertRepository;
import br.com.fiap.chents.entity.mapper.AlertMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;

    public AlertService(AlertRepository alertRepository, AlertMapper alertMapper) {
        this.alertRepository = alertRepository;
        this.alertMapper = alertMapper;
    }

    public AlertDTO createAlert(AlertDTO alertDTO) {
        Alert alert = alertMapper.toEntity(alertDTO);
        Alert savedAlert = alertRepository.save(alert);
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
}