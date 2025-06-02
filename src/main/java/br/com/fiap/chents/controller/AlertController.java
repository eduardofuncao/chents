package br.com.fiap.chents.controller;

import br.com.fiap.chents.entity.dto.AlertDTO;
import br.com.fiap.chents.entity.dto.LocationDTO;
import br.com.fiap.chents.entity.dto.PositionDTO;
import br.com.fiap.chents.entity.dto.UserDTO;
import br.com.fiap.chents.service.AlertService;
import br.com.fiap.chents.service.LocationService;
import br.com.fiap.chents.service.PositionService;
import br.com.fiap.chents.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/alerts")
public class AlertController {

    private final AlertService alertService;
    private final LocationService locationService;
    private final UserService userService;
    private final PositionService positionService;

    public AlertController(AlertService alertService, LocationService locationService,
                           UserService userService, PositionService positionService) {
        this.alertService = alertService;
        this.locationService = locationService;
        this.userService = userService;
        this.positionService = positionService;
    }

    @GetMapping
    public String listAlerts(Model model) {
        List<AlertDTO> alerts = alertService.getAllAlerts();
        model.addAttribute("alerts", alerts);

        List<LocationDTO> locations = locationService.getAllLocations();
        Map<Long, String> locationMap = new HashMap<>();
        for (LocationDTO location : locations) {
            locationMap.put(location.getId(), location.getCity() + ", " + location.getState());
        }
        model.addAttribute("locationMap", locationMap);

        Map<Long, String> userMap = new HashMap<>();
        List<UserDTO> users = userService.getAllUsers();
        for (UserDTO user : users) {
            userMap.put(user.getId(), user.getName());
        }
        model.addAttribute("userMap", userMap);

        // Add current timestamp for display
        model.addAttribute("currentDateTime", LocalDateTime.now());
        model.addAttribute("currentUser", "eduardofuncao");

        return "alerts/list";
    }

    @GetMapping("/new")
    public String showCreateAlertForm(Model model) {
        AlertDTO newAlert = new AlertDTO();
        // Pre-populate creation date and time
        newAlert.setCreation(LocalDateTime.now());

        model.addAttribute("alert", newAlert);
        model.addAttribute("locations", locationService.getAllLocations());
        model.addAttribute("users", userService.getAllUsers());

        // Add current timestamp for display
        model.addAttribute("currentDateTime", LocalDateTime.now());
        model.addAttribute("currentUser", "eduardofuncao");

        return "alerts/form";
    }

    @PostMapping("/new")
    public String createAlert(@ModelAttribute("alert") AlertDTO alertDTO,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("locations", locationService.getAllLocations());
            model.addAttribute("users", userService.getAllUsers());
            return "alerts/form";
        }

        // Get the user's positionId
        if (alertDTO.getUserId() != null) {
            UserDTO userDTO = userService.getUserById(alertDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            if (userDTO != null) {
                alertDTO.setPositionId(userDTO.getPositionId());
            }
        }

        alertService.createAlert(alertDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Alert created successfully!");
        return "redirect:/alerts";
    }

    @GetMapping("/{id}/edit")
    public String showEditAlertForm(@PathVariable("id") Long id, Model model) {
        Optional<AlertDTO> alertOpt = alertService.getAlertById(id);

        if (alertOpt.isPresent()) {
            model.addAttribute("alert", alertOpt.get());
            model.addAttribute("locations", locationService.getAllLocations());
            model.addAttribute("users", userService.getAllUsers());

            // Add current timestamp for display
            model.addAttribute("currentDateTime", LocalDateTime.now());
            model.addAttribute("currentUser", "eduardofuncao");

            return "alerts/form";
        } else {
            return "redirect:/alerts";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateAlert(@PathVariable("id") Long id,
                              @ModelAttribute("alert") AlertDTO alertDTO,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("locations", locationService.getAllLocations());
            model.addAttribute("users", userService.getAllUsers());
            return "alerts/form";
        }

        alertService.updateAlert(id, alertDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Alert updated successfully!");
        return "redirect:/alerts";
    }

    @GetMapping("/{id}/delete")
    public String deleteAlert(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = alertService.deleteAlert(id);

        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "Alert deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Alert could not be deleted. It may have been removed already.");
        }

        return "redirect:/alerts";
    }
}
