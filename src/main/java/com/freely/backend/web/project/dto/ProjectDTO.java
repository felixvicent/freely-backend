package com.freely.backend.web.project.dto;

import com.freely.backend.project.ProjectStatusEnum;
import com.freely.backend.web.activity.dto.ActivityDTO;
import com.freely.backend.web.clients.dto.ClientListDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ProjectDTO {
    private UUID id;
    private String title;
    private ClientListDTO client;
    private double value;
    private LocalDateTime estimatedDate;
    private LocalDateTime createdAt;
    private List<ActivityDTO> activities;
    private ProjectStatusEnum status;
}
