package br.com.fiap.chents.repository;

import br.com.fiap.chents.entity.Alert;
import br.com.fiap.chents.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    Boolean existsByUser(User user);

    @Query(value = """
        SELECT a.* FROM alert a
        JOIN position p ON a.position_id = p.id
        WHERE
          a.creation >= :since
          AND (6371 * acos(
                cos(:lat * ACOS(-1)/180) * cos(p.latitude * ACOS(-1)/180) *
                cos(p.longitude * ACOS(-1)/180 - :lng * ACOS(-1)/180) +
                sin(:lat * ACOS(-1)/180) * sin(p.latitude * ACOS(-1)/180)
              )) < :radius
        """, nativeQuery = true)
        List<Alert> findAlertsNearby(
        @Param("lat") double latitude,
        @Param("lng") double longitude,
        @Param("radius") double radius,
        @Param("since") LocalDateTime since
    );
}
