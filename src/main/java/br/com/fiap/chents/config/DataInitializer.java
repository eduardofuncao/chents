package br.com.fiap.chents.config;

import br.com.fiap.chents.entity.Alert;
import br.com.fiap.chents.entity.Location;
import br.com.fiap.chents.entity.Position;
import br.com.fiap.chents.entity.User;
import br.com.fiap.chents.entity.enums.Role;
import br.com.fiap.chents.repository.AlertRepository;
import br.com.fiap.chents.repository.LocationRepository;
import br.com.fiap.chents.repository.PositionRepository;
import br.com.fiap.chents.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            LocationRepository locationRepository,
            PositionRepository positionRepository,
            UserRepository userRepository,
            AlertRepository alertRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            if (locationRepository.count() > 0) {
                return;
            }

            List<Location> locations = Arrays.asList(
                    createLocation("São Paulo", "SP"),
                    createLocation("Rio de Janeiro", "RJ"),
                    createLocation("Belo Horizonte", "MG"),
                    createLocation("Porto Alegre", "RS"),
                    createLocation("Recife", "PE")
            );
            locationRepository.saveAll(locations);

            List<Position> positions = Arrays.asList(
                    createPosition(-23.5505, -46.6333), // São Paulo
                    createPosition(-22.9068, -43.1729), // Rio de Janeiro
                    createPosition(-19.9167, -43.9345), // Belo Horizonte
                    createPosition(-30.0277, -51.2287), // Porto Alegre
                    createPosition(-8.0476, -34.8770)   // Recife
            );
            positionRepository.saveAll(positions);

            User admin = createUser(
                    "admin",
                    passwordEncoder.encode("admin"),
                    true,
                    Role.ADMIN,
                    "Administrator",
                    "admin@example.com",
                    "Av. Paulista, 1106",
                    locations.get(0),
                    positions.get(0)
            );

            User user1 = createUser(
                    "user1",
                    passwordEncoder.encode("user1"),
                    true,
                    Role.USER,
                    "User One",
                    "user1@example.com",
                    "Rua das Flores, 123",
                    locations.get(1),
                    positions.get(1)
            );

            User user2 = createUser(
                    "user2",
                    passwordEncoder.encode("user2"),
                    true,
                    Role.USER,
                    "User Two",
                    "user2@example.com",
                    "Av. Atlântica, 500",
                    locations.get(2),
                    positions.get(2)
            );

            userRepository.saveAll(Arrays.asList(admin, user1, user2));

            List<Alert> alerts = Arrays.asList(
                    createAlert(
                            LocalDateTime.now().minusDays(2),
                            "Risco de inundação na região central",
                            user1,
                            locations.get(1),
                            positions.get(1)
                    ),
                    createAlert(
                            LocalDateTime.now().minusDays(1),
                            "Alerta de chuvas intensas na zona sul",
                            user2,
                            locations.get(2),
                            positions.get(2)
                    ),
                    createAlert(
                            LocalDateTime.now(),
                            "Deslizamento de terra na região serrana",
                            admin,
                            locations.get(0),
                            positions.get(0)
                    )
            );
            alertRepository.saveAll(alerts);
        };
    }

    private Location createLocation(String city, String state) {
        Location location = new Location();
        location.setCity(city);
        location.setState(state);
        return location;
    }

    private Position createPosition(double latitude, double longitude) {
        Position position = new Position();
        position.setLatitude(latitude);
        position.setLongitude(longitude);
        return position;
    }

    private User createUser(String username, String password, boolean enabled, Role role,
                            String name, String email, String endereco, Location location, Position position) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEnabled(enabled);
        user.setRole(role);
        user.setName(name);
        user.setEmail(email);
        user.setEndereco(endereco);
        user.setLocation(location);
        user.setPosition(position);
        return user;
    }

    private Alert createAlert(LocalDateTime creation, String message, User user, Location location, Position position) {
        Alert alert = new Alert();
        alert.setCreation(creation);
        alert.setMessage(message);
        alert.setUser(user);
        alert.setLocation(location);
        alert.setPosition(position);
        return alert;
    }
}
