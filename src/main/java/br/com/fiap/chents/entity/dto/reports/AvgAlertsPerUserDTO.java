package br.com.fiap.chents.entity.dto.reports;

import java.math.BigDecimal;

public class AvgAlertsPerUserDTO {
    private BigDecimal avgAlerts;

    public AvgAlertsPerUserDTO() {
    }

    public AvgAlertsPerUserDTO(BigDecimal avgAlerts) {
        this.avgAlerts = avgAlerts;
    }

    public BigDecimal getAvgAlerts() {
        return avgAlerts;
    }

    public void setAvgAlerts(BigDecimal avgAlerts) {
        this.avgAlerts = avgAlerts;
    }
}
