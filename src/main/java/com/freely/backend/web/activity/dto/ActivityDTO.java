package com.freely.backend.web.activity.dto;

import com.freely.backend.activity.ActivityStatusEnum;
import com.freely.backend.user.UserAccount;
import com.freely.backend.web.project.dto.ProjectDTO;
import com.freely.backend.web.user.dto.UserDTO;
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
    private ProjectDTO project;
    private LocalDateTime createdAt;
    private LocalDateTime estimatedDate;
    private LocalDateTime finishedAt;
    private UserDTO responsible;
    private String description;
}
