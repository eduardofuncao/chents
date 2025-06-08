package br.com.fiap.chents.controller;

import br.com.fiap.chents.service.AlertReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reports")
public class AlertReportController {

    private final AlertReportService reportService;

    @Autowired
    public AlertReportController(AlertReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Main report view that displays all report data
     */
    @GetMapping
    public String showReportsPage(Model model) {
        model.addAttribute("reportData", reportService.getAllReportData());
        return "reports"; // Thymeleaf template name
    }

}
