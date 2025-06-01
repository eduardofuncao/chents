package br.com.fiap.chents.service;

import br.com.fiap.chents.entity.Position;
import br.com.fiap.chents.entity.dto.PositionDTO;
import br.com.fiap.chents.entity.mapper.PositionMapper;
import br.com.fiap.chents.repository.PositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PositionService {

    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    public PositionService(PositionRepository positionRepository, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
    }

    public PositionDTO createPosition(PositionDTO positionDTO) {
        Position position = positionMapper.toEntity(positionDTO);
        Position savedPosition = positionRepository.save(position);
        return positionMapper.toDTO(savedPosition);
    }

    public List<PositionDTO> getAllPositions() {
        return positionRepository.findAll().stream()
                .map(positionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<PositionDTO> getPositionById(Long id) {
        return positionRepository.findById(id).map(positionMapper::toDTO);
    }

    public Optional<PositionDTO> updatePosition(Long id, PositionDTO positionDTO) {
        return positionRepository.findById(id)
                .map(existing -> {
                    Position updated = positionMapper.toEntity(positionDTO);
                    updated.setId(id);
                    Position saved = positionRepository.save(updated);
                    return positionMapper.toDTO(saved);
                });
    }

    public boolean deletePosition(Long id) {
        if (positionRepository.existsById(id)) {
            positionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
