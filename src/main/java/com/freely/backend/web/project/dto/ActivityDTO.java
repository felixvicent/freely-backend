package com.freely.backend.web.project.dto;

import com.freely.backend.project.ActivityStatusEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ActivityDTO {
    private UUID id;
    private String title;
    private ActivityStatusEnum status;
    private LocalDateTime createdAt;
}
