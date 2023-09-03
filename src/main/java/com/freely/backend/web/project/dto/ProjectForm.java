package com.freely.backend.web.project.dto;

import com.freely.backend.web.clients.dto.ClientDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProjectForm {
    private String title;
    private UUID clientId;
    private double value;
    private LocalDateTime estimedDate;
}
