package br.com.fiap.chents.controller;

import br.com.fiap.chents.entity.dto.AlertDTO;
import br.com.fiap.chents.entity.dto.LocationDTO;
import br.com.fiap.chents.entity.dto.PositionDTO;
import br.com.fiap.chents.entity.dto.UserDTO;
import br.com.fiap.chents.service.AlertService;
import br.com.fiap.chents.service.LocationService;
import br.com.fiap.chents.service.PositionService;
import br.com.fiap.chents.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class LoginController {

    private final AlertService alertService;
    private final LocationService locationService;
    private final UserService userService;
    private final PositionService positionService;

    public LoginController(AlertService alertService, LocationService locationService, UserService userService, PositionService positionService) {
        this.alertService = alertService;
        this.locationService = locationService;
        this.userService = userService;
        this.positionService = positionService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());

        // Recent alerts
        List<AlertDTO> recentAlerts = alertService.getAllAlerts();
        model.addAttribute("recentAlerts", recentAlerts);

        // Locations
        List<LocationDTO> locations = locationService.getAllLocations();
        model.addAttribute("locations", locations);

        Map<Long, String> locationMap = new HashMap<>();
        for (LocationDTO location : locations) {
            locationMap.put(location.getId(), location.getCity() + ", " + location.getState());
        }
        model.addAttribute("locationMap", locationMap);

        // Nearby alerts logic
        Optional<UserDTO> userOpt = userService.getAllUsers().stream()
                .filter(u -> u.getUsername().equals(auth.getName()))
                .findFirst();
        List<AlertDTO> nearbyAlerts = new ArrayList<>();
        if (userOpt.isPresent() && userOpt.get().getPositionId() != null) {
            PositionDTO pos = positionService.getPositionById(userOpt.get().getPositionId()).orElse(null);
            if (pos != null) {
                LocalDateTime since = LocalDateTime.now().minusDays(1);
                nearbyAlerts = alertService.getAlertsNearby(pos.getLatitude(), pos.getLongitude(), 5.0, since);
            }
        }
        model.addAttribute("nearbyAlerts", nearbyAlerts);

        return "home";
    }

}
