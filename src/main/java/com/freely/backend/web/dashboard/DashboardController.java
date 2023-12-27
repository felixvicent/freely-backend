package com.freely.backend.web.dashboard;

import com.freely.backend.activity.ActivityService;
import com.freely.backend.client.ClientService;
import com.freely.backend.project.ProjectService;
import com.freely.backend.user.UserAccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ActivityService activityService;

    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenue(@AuthenticationPrincipal UserAccount userAccount,
                                        @RequestParam(name = "periodStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodStart,
                                        @RequestParam(name = "periodEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodEnd) {

        var revenue = projectService.getRevenue(userAccount, periodStart, periodEnd);

        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/clients")
    public ResponseEntity<?> getClients(@AuthenticationPrincipal UserAccount userAccount,
                                        @RequestParam(name = "periodStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodStart,
                                        @RequestParam(name = "periodEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodEnd) {

        var clients = clientService.countByCompany(userAccount, periodStart, periodEnd);

        return ResponseEntity.ok(clients);
    }

    @GetMapping("/projects")
    public ResponseEntity<?> getProjects(@AuthenticationPrincipal UserAccount userAccount,
                                        @RequestParam(name = "periodStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodStart,
                                        @RequestParam(name = "periodEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodEnd) {

        var projects = projectService.countByCompany(userAccount, periodStart, periodEnd);

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/activities")
    public ResponseEntity<?> getActivities(@AuthenticationPrincipal UserAccount userAccount,
                                         @RequestParam(name = "periodStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodStart,
                                         @RequestParam(name = "periodEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodEnd) {

        var activities = activityService.countByCompany(userAccount, periodStart, periodEnd);

        return ResponseEntity.ok(activities);
    }
}
