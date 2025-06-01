package br.com.fiap.chents.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "latitude cannot be null")
    private double latitude;

    @NotNull(message = "longitude cannot be null")
    private double longitude;

    public Position() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NotNull(message = "latitude cannot be null")
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(@NotNull(message = "latitude cannot be null") double latitude) {
        this.latitude = latitude;
    }

    @NotNull(message = "longitude cannot be null")
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(@NotNull(message = "longitude cannot be null") double longitude) {
        this.longitude = longitude;
    }
}
