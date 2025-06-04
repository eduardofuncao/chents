package br.com.fiap.chents.entity.dto;

import java.time.LocalDateTime;

public class AlertMessage {

    private Long id;
    private LocalDateTime creation;
    private LocalDateTime messageCreation;
    private String message;
    private String userName;
    private String city;
    private double latitude;
    private double longitude;

    public AlertMessage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreation() {
        return creation;
    }

    public void setCreation(LocalDateTime creation) {
        this.creation = creation;
    }

    public LocalDateTime getMessageCreation() {
        return messageCreation;
    }

    public void setMessageCreation(LocalDateTime messageCreation) {
        this.messageCreation = messageCreation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
