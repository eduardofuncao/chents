package br.com.fiap.chents.service;

import br.com.fiap.chents.entity.dto.reports.AlertCountByCityDTO;
import br.com.fiap.chents.entity.dto.reports.AvgAlertsPerUserDTO;
import br.com.fiap.chents.entity.dto.reports.MaxLatitudeByCityDTO;
import br.com.fiap.chents.entity.dto.reports.UserWithMostAlertsDTO;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    public ReportService(@Qualifier("oracleJdbcTemplate") JdbcTemplate jdbcTemplate,
                         @Qualifier("oracleDataSource") DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    public List<AlertCountByCityDTO> getAlertCountsByCity() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource)
                .withCatalogName("chents_queries_pkg")
                .withProcedureName("count_alerts_by_city")
                .declareParameters(
                        new SqlOutParameter("p_result", OracleTypes.CURSOR,
                                rs -> {
                                    List<AlertCountByCityDTO> result = new ArrayList<>();
                                    while (rs.next()) {
                                        result.add(new AlertCountByCityDTO(
                                                rs.getString("city"),
                                                rs.getLong("total_alerts")
                                        ));
                                    }
                                    return result;
                                }));

        Map<String, Object> out = jdbcCall.execute(new HashMap<>());
        return (List<AlertCountByCityDTO>) out.get("p_result");
    }

    public AvgAlertsPerUserDTO getAvgAlertsPerUser() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource)
                .withCatalogName("chents_queries_pkg")
                .withProcedureName("avg_alerts_per_user")
                .declareParameters(
                        new SqlOutParameter("p_result", OracleTypes.CURSOR,
                                rs -> {
                                    AvgAlertsPerUserDTO result = null;
                                    if (rs.next()) {
                                        result = new AvgAlertsPerUserDTO(
                                                rs.getBigDecimal("avg_alerts")
                                        );
                                    }
                                    return result;
                                }));

        Map<String, Object> out = jdbcCall.execute(new HashMap<>());
        return (AvgAlertsPerUserDTO) out.get("p_result");
    }

    public List<MaxLatitudeByCityDTO> getMaxLatitudeByCity() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource)
                .withCatalogName("chents_queries_pkg")
                .withProcedureName("max_latitude_by_city")
                .declareParameters(
                        new SqlOutParameter("p_result", OracleTypes.CURSOR,
                                rs -> {
                                    List<MaxLatitudeByCityDTO> result = new ArrayList<>();
                                    while (rs.next()) {
                                        result.add(new MaxLatitudeByCityDTO(
                                                rs.getString("city"),
                                                rs.getBigDecimal("max_latitude")
                                        ));
                                    }
                                    return result;
                                }));

        Map<String, Object> out = jdbcCall.execute(new HashMap<>());
        return (List<MaxLatitudeByCityDTO>) out.get("p_result");
    }

    public List<UserWithMostAlertsDTO> getUsersWithMostAlerts() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource)
                .withCatalogName("chents_queries_pkg")
                .withProcedureName("user_with_most_alerts")
                .declareParameters(
                        new SqlOutParameter("p_result", OracleTypes.CURSOR,
                                rs -> {
                                    List<UserWithMostAlertsDTO> result = new ArrayList<>();
                                    while (rs.next()) {
                                        result.add(new UserWithMostAlertsDTO(
                                                rs.getString("name"),
                                                rs.getLong("total_alerts")
                                        ));
                                    }
                                    return result;
                                }));

        Map<String, Object> out = jdbcCall.execute(new HashMap<>());
        return (List<UserWithMostAlertsDTO>) out.get("p_result");
    }
}
