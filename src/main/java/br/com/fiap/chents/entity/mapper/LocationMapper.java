package br.com.fiap.chents.entity.mapper;

import br.com.fiap.chents.entity.Location;
import br.com.fiap.chents.entity.dto.LocationDTO;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    public LocationDTO toDTO(Location location) {
        if (location == null) return null;
        LocationDTO dto = new LocationDTO();
        dto.setId(location.getId());
        dto.setCity(location.getCity());
        dto.setState(location.getState());
        return dto;
    }

    public Location toEntity(LocationDTO dto) {
        if (dto == null) return null;
        Location location = new Location();
        location.setId(dto.getId() != null ? dto.getId() : 0L);
        location.setCity(dto.getCity());
        location.setState(dto.getState());
        return location;
    }
}
