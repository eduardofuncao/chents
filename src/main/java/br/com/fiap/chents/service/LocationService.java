package br.com.fiap.chents.service;

import br.com.fiap.chents.entity.Location;
import br.com.fiap.chents.entity.dto.LocationDTO;
import br.com.fiap.chents.entity.mapper.LocationMapper;
import br.com.fiap.chents.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public LocationService(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    public LocationDTO createLocation(LocationDTO locationDTO) {
        Location location = locationMapper.toEntity(locationDTO);
        Location savedLocation = locationRepository.save(location);
        return locationMapper.toDTO(savedLocation);
    }

    public List<LocationDTO> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(locationMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<LocationDTO> getLocationById(Long id) {
        return locationRepository.findById(id).map(locationMapper::toDTO);
    }

    public Optional<LocationDTO> updateLocation(Long id, LocationDTO locationDTO) {
        return locationRepository.findById(id)
                .map(existing -> {
                    Location updated = locationMapper.toEntity(locationDTO);
                    updated.setId(id);
                    Location saved = locationRepository.save(updated);
                    return locationMapper.toDTO(saved);
                });
    }

    public boolean deleteLocation(Long id) {
        if (locationRepository.existsById(id)) {
            locationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
