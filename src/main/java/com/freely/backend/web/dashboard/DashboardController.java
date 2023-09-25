package com.freely.backend.web.dashboard;

import com.freely.backend.activity.ActivityService;
import com.freely.backend.client.ClientService;
import com.freely.backend.project.ProjectService;
import com.freely.backend.user.UserAccount;
import com.freely.backend.web.clients.dto.ClientListDTO;
import com.freely.backend.web.dashboard.dto.DashboardDTO;
import com.freely.backend.web.project.dto.ActivityDTO;
import com.freely.backend.web.project.dto.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ActivityService activityService;

    @GetMapping
    public ResponseEntity<?> index(@AuthenticationPrincipal UserAccount user) {
        long quantityOfClients = clientService.countByUser(user);
        long quantityOfProjects = projectService.countByUser(user);
        long quantityOfActivities = activityService.countByUser(user);
        List<ClientListDTO> latestClients = clientService.findLatest(user);
        List<ProjectDTO> latestProjects = projectService.findLatest(user);
        List<ActivityDTO> latestActivities = activityService.findLatest(user);

        DashboardDTO dashboard = DashboardDTO.builder()
                .quantityOfClients(quantityOfClients)
                .quantityOfProjects(quantityOfProjects)
                .quantityOfActivities(quantityOfActivities)
                .latestClients(latestClients)
                .latestProjects(latestProjects)
                .latestActivities(latestActivities)
                .build();


        return ResponseEntity.ok(dashboard);
    }
}
