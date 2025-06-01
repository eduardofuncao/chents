package br.com.fiap.chents.repository;

import br.com.fiap.chents.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
}
