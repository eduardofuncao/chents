package br.com.fiap.chents.controller;

import br.com.fiap.chents.entity.dto.AlertDTO;
import br.com.fiap.chents.entity.dto.LocationDTO;
import br.com.fiap.chents.service.AlertService;
import br.com.fiap.chents.service.LocationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    private final AlertService alertService;
    private final LocationService locationService;

    public LoginController(AlertService alertService, LocationService locationService) {
        this.alertService = alertService;
        this.locationService = locationService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());

        List<AlertDTO> recentAlerts = alertService.getAllAlerts();
        model.addAttribute("recentAlerts", recentAlerts);

        List<LocationDTO> locations = locationService.getAllLocations();
        model.addAttribute("locations", locations);

        Map<Long, String> locationMap = new HashMap<>();
        for (LocationDTO location : locations) {
            locationMap.put(location.getId(), location.getCity() + ", " + location.getState());
        }
        model.addAttribute("locationMap", locationMap);

        return "home";
    }
}
