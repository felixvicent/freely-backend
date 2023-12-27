package com.freely.backend.web.dashboard;

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

    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenue(@AuthenticationPrincipal UserAccount userAccount,
                                        @RequestParam("periodStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodStart,
                                        @RequestParam("periodEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodEnd) {

        var revenue = projectService.getRevenue(userAccount, periodStart, periodEnd);

        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/clients")
    public ResponseEntity<?> getClients(@AuthenticationPrincipal UserAccount userAccount,
                                        @RequestParam("periodStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodStart,
                                        @RequestParam("periodEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodEnd) {

        var revenue = clientService.countByCompany(userAccount, periodStart, periodEnd);

        return ResponseEntity.ok(revenue);
    }
}
