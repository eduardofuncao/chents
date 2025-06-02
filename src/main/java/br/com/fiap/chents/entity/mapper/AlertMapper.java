package br.com.fiap.chents.entity.mapper;

import br.com.fiap.chents.entity.dto.AlertDTO;
import br.com.fiap.chents.entity.Alert;
import br.com.fiap.chents.entity.Location;
import br.com.fiap.chents.entity.Position;
import br.com.fiap.chents.entity.User;
import br.com.fiap.chents.repository.LocationRepository;
import br.com.fiap.chents.repository.PositionRepository;
import br.com.fiap.chents.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class AlertMapper {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final PositionRepository positionRepository;

    public AlertMapper(UserRepository userRepository, LocationRepository locationRepository, PositionRepository positionRepository) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.positionRepository = positionRepository;
    }

    public AlertDTO toDTO(Alert alert) {
        if (alert == null) return null;

        AlertDTO dto = new AlertDTO();
        dto.setId(alert.getId());
        dto.setCreation(alert.getCreation());
        dto.setMessage(alert.getMessage());
        dto.setUserId(alert.getUser().getId());
        dto.setLocationId(alert.getLocation().getId());
        dto.setPositionId(alert.getPosition().getId());
        return dto;
    }

    public Alert toEntity(AlertDTO dto) {
        if (dto == null) return null;
        Alert alert = new Alert();
        if (dto.getId() != null) alert.setId(dto.getId());
        alert.setCreation(dto.getCreation());
        alert.setMessage(dto.getMessage());

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        alert.setUser(user);

        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));
        alert.setLocation(location);

        Position position = positionRepository.findById(dto.getPositionId())
                .orElseThrow(() -> new RuntimeException("Position not found"));
        alert.setPosition(position);

        return alert;
    }
}
