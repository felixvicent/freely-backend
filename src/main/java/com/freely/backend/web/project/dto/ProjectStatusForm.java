package com.freely.backend.web.project.dto;

import com.freely.backend.project.ProjectStatusEnum;
import lombok.Data;

@Data
public class ProjectStatusForm {
    private ProjectStatusEnum status;
}
