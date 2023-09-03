package com.freely.backend.web.project.dto;

import com.freely.backend.web.clients.dto.ClientDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ProjectDTO {
    private UUID id;
    private String title;
    private ClientDTO client;
    private double value;
    private LocalDateTime estimedDate;
    private LocalDateTime createdAt;
}
