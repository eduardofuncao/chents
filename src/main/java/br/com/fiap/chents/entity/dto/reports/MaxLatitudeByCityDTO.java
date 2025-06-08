package br.com.fiap.chents.entity.dto.reports;

import java.math.BigDecimal;

public class MaxLatitudeByCityDTO {
    private String city;
    private BigDecimal maxLatitude;

    public MaxLatitudeByCityDTO() {
    }

    public MaxLatitudeByCityDTO(String city, BigDecimal maxLatitude) {
        this.city = city;
        this.maxLatitude = maxLatitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public BigDecimal getMaxLatitude() {
        return maxLatitude;
    }

    public void setMaxLatitude(BigDecimal maxLatitude) {
        this.maxLatitude = maxLatitude;
    }
}
