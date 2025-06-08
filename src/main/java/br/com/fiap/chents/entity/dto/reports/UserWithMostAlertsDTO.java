package br.com.fiap.chents.entity.dto.reports;

public class UserWithMostAlertsDTO {
    private String name;
    private Long totalAlerts;

    public UserWithMostAlertsDTO() {
    }

    public UserWithMostAlertsDTO(String name, Long totalAlerts) {
        this.name = name;
        this.totalAlerts = totalAlerts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTotalAlerts() {
        return totalAlerts;
    }

    public void setTotalAlerts(Long totalAlerts) {
        this.totalAlerts = totalAlerts;
    }
}
