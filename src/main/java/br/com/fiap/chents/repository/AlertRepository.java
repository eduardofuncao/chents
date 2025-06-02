package br.com.fiap.chents.repository;

import br.com.fiap.chents.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    @Query(value = """
    SELECT a.* FROM alert a
    JOIN position p ON a.position_id = p.id
    WHERE
      a.creation >= :since
      AND (6371 * acos(
            cos(radians(:lat)) * cos(radians(p.latitude)) *
            cos(radians(p.longitude) - radians(:lng)) +
            sin(radians(:lat)) * sin(radians(p.latitude))
          )) < :radius
    """, nativeQuery = true)
    List<Alert> findAlertsNearby(
            @Param("lat") double latitude,
            @Param("lng") double longitude,
            @Param("radius") double radius,
            @Param("since") LocalDateTime since
    );
}
