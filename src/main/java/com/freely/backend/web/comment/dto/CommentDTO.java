package com.freely.backend.web.comment.dto;

import com.freely.backend.activity.ActivityStatusEnum;
import com.freely.backend.web.project.dto.ProjectDTO;
import com.freely.backend.web.user.dto.UserDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CommentDTO {
    private UUID id;
    private String comment;
    private UserDTO user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
