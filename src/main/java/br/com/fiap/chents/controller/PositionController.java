package br.com.fiap.chents.controller;

import br.com.fiap.chents.entity.Position;
import br.com.fiap.chents.entity.User;
import br.com.fiap.chents.entity.dto.PositionDTO;
import br.com.fiap.chents.repository.UserRepository;
import br.com.fiap.chents.service.PositionService;
import br.com.fiap.chents.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class PositionController {

    private final UserService userService;
    private final PositionService positionService;
    private final UserRepository userRepository;

    public PositionController(UserService userService, PositionService positionService, UserRepository userRepository) {
        this.userService = userService;
        this.positionService = positionService;
        this.userRepository = userRepository;
    }

    /**
     * Get the current logged-in user's position
     */
    @GetMapping("/position")
    public ResponseEntity<?> getUserPosition() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Position position = user.getPosition();

            if (position != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("latitude", position.getLatitude());
                response.put("longitude", position.getLongitude());
                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.ok(new HashMap<>());
    }

    /**
     * Update the current logged-in user's position with GPS coordinates
     */
    @PostMapping("/position/update")
    public ResponseEntity<?> updateUserPosition(@RequestBody Map<String, Double> positionData) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
        }

        User user = userOpt.get();
        Double latitude = positionData.get("latitude");
        Double longitude = positionData.get("longitude");

        if (latitude == null || longitude == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid position data"));
        }

        PositionDTO positionDTO = positionService.getPositionById(user.getPosition().getId())
                .orElse(new PositionDTO());

        positionDTO.setLatitude(latitude);
        positionDTO.setLongitude(longitude);

        positionService.updatePosition(positionDTO.getId(), positionDTO);

        return ResponseEntity.ok(Map.of("success", true));
    }
}
