package br.com.fiap.chents.entity.mapper;

import br.com.fiap.chents.entity.User;
import br.com.fiap.chents.entity.Location;
import br.com.fiap.chents.entity.Position;
import br.com.fiap.chents.entity.dto.UserDTO;
import br.com.fiap.chents.entity.enums.Role;
import br.com.fiap.chents.repository.LocationRepository;
import br.com.fiap.chents.repository.PositionRepository;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final LocationRepository locationRepository;
    private final PositionRepository positionRepository;

    public UserMapper(LocationRepository locationRepository, PositionRepository positionRepository) {
        this.locationRepository = locationRepository;
        this.positionRepository = positionRepository;
    }

    public UserDTO toDTO(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setEndereco(user.getEndereco());
        dto.setRole(user.getRole() != null ? user.getRole().name() : null);
        dto.setEnabled(user.isEnabled());

        dto.setLocationId(user.getLocation() != null ? user.getLocation().getId() : null);
        dto.setPositionId(user.getPosition() != null ? user.getPosition().getId() : null);

        return dto;
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId() != null ? dto.getId() : 0L);
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setEndereco(dto.getEndereco());
        user.setRole(dto.getRole() != null ? Role.valueOf(dto.getRole()) : null);
        user.setEnabled(dto.isEnabled());

        if (dto.getLocationId() != null) {
            Location location = locationRepository.findById(dto.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found"));
            user.setLocation(location);
        }
        if (dto.getPositionId() != null) {
            Position position = positionRepository.findById(dto.getPositionId())
                    .orElseThrow(() -> new RuntimeException("Position not found"));
            user.setPosition(position);
        }

        return user;
    }
}
