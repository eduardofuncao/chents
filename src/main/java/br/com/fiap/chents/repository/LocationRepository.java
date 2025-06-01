package br.com.fiap.chents.repository;

import br.com.fiap.chents.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
