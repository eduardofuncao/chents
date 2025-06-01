package br.com.fiap.chents.repository;

import br.com.fiap.chents.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Long> {
}
