package com.freely.backend.web.dashboard.dto;

import com.freely.backend.web.clients.dto.ClientListDTO;
import com.freely.backend.web.project.dto.ActivityDTO;
import com.freely.backend.web.project.dto.ProjectDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardDTO {
    private long quantityOfClients;
    private List<ClientListDTO> latestClients;
    private long quantityOfProjects;
    private List<ProjectDTO> latestProjects;
    private long quantityOfActivities;
    private List<ActivityDTO> latestActivities;
}
