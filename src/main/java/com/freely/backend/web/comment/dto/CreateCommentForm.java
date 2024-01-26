package com.freely.backend.web.comment.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class CreateCommentForm {
    @NotBlank
    private String comment;

    private UUID activityId;
}
