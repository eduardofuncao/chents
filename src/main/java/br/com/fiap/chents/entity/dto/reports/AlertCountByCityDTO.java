package br.com.fiap.chents.entity.dto.reports;

public class AlertCountByCityDTO {
    private String city;
    private Long totalAlerts;

    public AlertCountByCityDTO() {
    }

    public AlertCountByCityDTO(String city, Long totalAlerts) {
        this.city = city;
        this.totalAlerts = totalAlerts;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getTotalAlerts() {
        return totalAlerts;
    }

    public void setTotalAlerts(Long totalAlerts) {
        this.totalAlerts = totalAlerts;
    }
}
