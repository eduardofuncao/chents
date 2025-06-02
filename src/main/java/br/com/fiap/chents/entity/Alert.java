package br.com.fiap.chents.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Alert creation date can't be null")
    private LocalDateTime creation;

    private String message;

    @ManyToOne
    private User user;

    @ManyToOne
    private Location location;

    @ManyToOne
    private Position position;

    public Alert() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public @NotNull(message = "Alert creation date can't be null") LocalDateTime getCreation() {
        return creation;
    }

    public void setCreation(@NotNull(message = "Alert creation date can't be null") LocalDateTime creation) {
        this.creation = creation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
