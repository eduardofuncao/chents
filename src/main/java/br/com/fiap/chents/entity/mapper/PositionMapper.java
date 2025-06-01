package br.com.fiap.chents.entity.mapper;

import br.com.fiap.chents.entity.Position;
import br.com.fiap.chents.entity.dto.PositionDTO;
import org.springframework.stereotype.Component;

@Component
public class PositionMapper {

    public PositionDTO toDTO(Position position) {
        if (position == null) return null;
        PositionDTO dto = new PositionDTO();
        dto.setId(position.getId());
        dto.setLatitude(position.getLatitude());
        dto.setLongitude(position.getLongitude());
        return dto;
    }

    public Position toEntity(PositionDTO dto) {
        if (dto == null) return null;
        Position position = new Position();
        position.setId(dto.getId() != null ? dto.getId() : 0L);
        position.setLatitude(dto.getLatitude());
        position.setLongitude(dto.getLongitude());
        return position;
    }
}
