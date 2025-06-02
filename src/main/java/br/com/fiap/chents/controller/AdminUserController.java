package br.com.fiap.chents.controller;

import br.com.fiap.chents.entity.dto.UserDTO;
import br.com.fiap.chents.entity.dto.LocationDTO;
import br.com.fiap.chents.entity.dto.PositionDTO;
import br.com.fiap.chents.service.UserService;
import br.com.fiap.chents.service.LocationService;
import br.com.fiap.chents.service.PositionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService userService;
    private final LocationService locationService;
    private final PositionService positionService;

    public AdminUserController(UserService userService, LocationService locationService, PositionService positionService) {
        this.userService = userService;
        this.locationService = locationService;
        this.positionService = positionService;
    }

    @GetMapping
    public String listUsers(Model model) {
        List<UserDTO> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/user-list";
    }

    @GetMapping("/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new UserDTO());
        model.addAttribute("locations", locationService.getAllLocations());
        model.addAttribute("positions", positionService.getAllPositions());
        model.addAttribute("formAction", "/admin/users/new");
        model.addAttribute("isNew", true);
        return "admin/user-form";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("user") UserDTO userDTO,
                             @RequestParam String password,
                             BindingResult result,
                             Model model,
                             RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("locations", locationService.getAllLocations());
            model.addAttribute("positions", positionService.getAllPositions());
            model.addAttribute("formAction", "/admin/users/new");
            model.addAttribute("isNew", true);
            return "admin/user-form";
        }
        userService.createUser(userDTO, password);
        attributes.addFlashAttribute("successMessage", "User created successfully!");
        return "redirect:/admin/users";
    }

    @GetMapping("/{id}/edit")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        Optional<UserDTO> userOpt = userService.getUserById(id);
        if (userOpt.isEmpty()) {
            return "redirect:/admin/users";
        }
        model.addAttribute("user", userOpt.get());
        model.addAttribute("locations", locationService.getAllLocations());
        model.addAttribute("positions", positionService.getAllPositions());
        model.addAttribute("formAction", "/admin/users/" + id + "/edit");
        model.addAttribute("isNew", false);
        return "admin/user-form";
    }

    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute("user") UserDTO userDTO,
                             BindingResult result,
                             Model model,
                             RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute("locations", locationService.getAllLocations());
            model.addAttribute("positions", positionService.getAllPositions());
            model.addAttribute("formAction", "/admin/users/" + id + "/edit");
            model.addAttribute("isNew", false);
            return "admin/user-form";
        }
        userService.updateUser(id, userDTO);
        attributes.addFlashAttribute("successMessage", "User updated successfully!");
        return "redirect:/admin/users";
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes attributes) {
        userService.deleteUser(id);
        attributes.addFlashAttribute("successMessage", "User deleted successfully!");
        return "redirect:/admin/users";
    }
}
