package com.freely.backend.web.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private UUID id;
    private String name;
    private String email;
    private String document;
    private String telephone;
    private Boolean active;
    private String role;
    private LocalDateTime createdAt;
    private String office;

}
