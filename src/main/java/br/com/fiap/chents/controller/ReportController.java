package br.com.fiap.chents.controller;

import br.com.fiap.chents.entity.dto.reports.AlertCountByCityDTO;
import br.com.fiap.chents.entity.dto.reports.AvgAlertsPerUserDTO;
import br.com.fiap.chents.entity.dto.reports.MaxLatitudeByCityDTO;
import br.com.fiap.chents.entity.dto.reports.UserWithMostAlertsDTO;
import br.com.fiap.chents.service.ReportService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public String showReportsDashboard(Model model) {
        // Add current timestamp for display
        model.addAttribute("currentDateTime", LocalDateTime.now());
        model.addAttribute("currentUser", "eduardofuncao");
        return "reports/dashboard";
    }

    @GetMapping("/alerts-by-city")
    public String showAlertsByCity(Model model) {
        List<AlertCountByCityDTO> alertCounts = reportService.getAlertCountsByCity();
        model.addAttribute("alertCounts", alertCounts);
        model.addAttribute("reportTitle", "Alerts Count by City");
        model.addAttribute("reportDescription", "Number of alerts reported in each city");
        model.addAttribute("currentDateTime", LocalDateTime.now());
        model.addAttribute("currentUser", "eduardofuncao");
        return "reports/alerts-by-city";
    }

    @GetMapping("/avg-alerts-per-user")
    public String showAvgAlertsPerUser(Model model) {
        AvgAlertsPerUserDTO avgAlerts = reportService.getAvgAlertsPerUser();
        model.addAttribute("avgAlerts", avgAlerts);
        model.addAttribute("reportTitle", "Average Alerts per User");
        model.addAttribute("reportDescription", "Average number of alerts created by each user");
        model.addAttribute("currentDateTime", LocalDateTime.now());
        model.addAttribute("currentUser", "eduardofuncao");
        return "reports/avg-alerts-per-user";
    }

    @GetMapping("/max-latitude-by-city")
    public String showMaxLatitudeByCity(Model model) {
        List<MaxLatitudeByCityDTO> maxLatitudes = reportService.getMaxLatitudeByCity();
        model.addAttribute("maxLatitudes", maxLatitudes);
        model.addAttribute("reportTitle", "Maximum Latitude by City");
        model.addAttribute("reportDescription", "Highest latitude recorded in alerts for each city");
        model.addAttribute("currentDateTime", LocalDateTime.now());
        model.addAttribute("currentUser", "eduardofuncao");
        return "reports/max-latitude-by-city";
    }

    @GetMapping("/users-with-most-alerts")
    public String showUsersWithMostAlerts(Model model) {
        List<UserWithMostAlertsDTO> topUsers = reportService.getUsersWithMostAlerts();
        model.addAttribute("topUsers", topUsers);
        model.addAttribute("reportTitle", "Users with Most Alerts");
        model.addAttribute("reportDescription", "Users who have created the highest number of alerts");
        model.addAttribute("currentDateTime", LocalDateTime.now());
        model.addAttribute("currentUser", "eduardofuncao");
        return "reports/users-with-most-alerts";
    }

    @GetMapping("/all")
    public String showAllReports(Model model) {
        // Fetch all report data
        List<AlertCountByCityDTO> alertCounts = reportService.getAlertCountsByCity();
        AvgAlertsPerUserDTO avgAlerts = reportService.getAvgAlertsPerUser();
        List<MaxLatitudeByCityDTO> maxLatitudes = reportService.getMaxLatitudeByCity();
        List<UserWithMostAlertsDTO> topUsers = reportService.getUsersWithMostAlerts();

        // Add to model
        model.addAttribute("alertCounts", alertCounts);
        model.addAttribute("avgAlerts", avgAlerts);
        model.addAttribute("maxLatitudes", maxLatitudes);
        model.addAttribute("topUsers", topUsers);

        // Common attributes
        model.addAttribute("reportTitle", "All Reports");
        model.addAttribute("reportDescription", "Combined view of all system reports");
        model.addAttribute("currentDateTime", LocalDateTime.now());
        model.addAttribute("currentUser", "eduardofuncao");

        return "reports/all-reports";
    }
}
