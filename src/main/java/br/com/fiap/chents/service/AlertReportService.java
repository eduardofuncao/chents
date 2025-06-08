package br.com.fiap.chents.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AlertReportService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Executes a stored procedure that returns a SYS_REFCURSOR
     */
    private List<Object[]> executeStoredProcedure(String procedureName) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery(procedureName);
        query.registerStoredProcedureParameter(1, void.class, jakarta.persistence.ParameterMode.REF_CURSOR);

        query.execute();
        return query.getResultList();
    }

    /**
     * Get count of alerts by city
     * @return List of city and alert count pairs
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAlertsByCity() {
        List<Object[]> results = executeStoredProcedure("count_alerts_by_city");

        return results.stream()
                .map(row -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("city", row[0]);
                    map.put("totalAlerts", row[1]);
                    return map;
                })
                .collect(Collectors.toList());
    }

    /**
     * Get average number of alerts per user
     * @return Average value
     */
    @Transactional(readOnly = true)
    public Double getAvgAlertsPerUser() {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("avg_alerts_per_user");
        query.registerStoredProcedureParameter(1, void.class, jakarta.persistence.ParameterMode.REF_CURSOR);

        query.execute();

        // Handle the case where Oracle returns a single value directly
        Object result = query.getSingleResult();

        if (result instanceof BigDecimal) {
            return ((BigDecimal) result).doubleValue();
        } else if (result instanceof Object[]) {
            // Handle case where it might return as array in some configurations
            Object[] row = (Object[]) result;
            return row[0] == null ? 0.0 : ((Number) row[0]).doubleValue();
        } else if (result instanceof Number) {
            return ((Number) result).doubleValue();
        }

        return 0.0; // Fallback default
    }

    /**
     * Get maximum latitude by city
     * @return List of city and max latitude pairs
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getMaxLatitudeByCity() {
        List<Object[]> results = executeStoredProcedure("max_latitude_by_city");

        return results.stream()
                .map(row -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("city", row[0]);
                    map.put("maxLatitude", row[1]);
                    return map;
                })
                .collect(Collectors.toList());
    }

    /**
     * Get user(s) with the most alerts
     * @return List of user name and alert count pairs
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getUserWithMostAlerts() {
        List<Object[]> results = executeStoredProcedure("user_with_most_alerts");

        return results.stream()
                .map(row -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userName", row[0]);
                    map.put("totalAlerts", row[1]);
                    return map;
                })
                .collect(Collectors.toList());
    }

    /**
     * Get all report data in a single call
     * @return Map containing all report data
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getAllReportData() {
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("alertsByCity", getAlertsByCity());
        reportData.put("avgAlertsPerUser", getAvgAlertsPerUser());
        reportData.put("maxLatitudeByCity", getMaxLatitudeByCity());
        reportData.put("usersWithMostAlerts", getUserWithMostAlerts());
        return reportData;
    }
}
