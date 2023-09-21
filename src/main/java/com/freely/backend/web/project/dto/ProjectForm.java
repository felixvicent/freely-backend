package com.freely.backend.web.project.dto;

import com.freely.backend.web.activity.dto.ActivityForm;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ProjectForm {
    private String title;
    private UUID clientId;
    private double value;
    private LocalDateTime estimatedDate;
    private List<ActivityForm> activities;
}
