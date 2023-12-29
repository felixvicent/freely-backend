package com.freely.backend.web.activity.dto;

import com.freely.backend.project.ActivityStatusEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ActivityForm {

    @NotBlank
    private String title;

    @NotBlank
    private LocalDateTime estimatedDate;

    @NotBlank
    private UUID projectId;

    private ActivityStatusEnum status;
}
