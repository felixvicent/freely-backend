package com.freely.backend.user.user_properties.dto;

import com.freely.backend.user.user_properties.UserPropertyDomainEnum;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserPropertyDTO {
    private UUID id;
    private UserPropertyDomainEnum domain;
    private String key;
    private String value;
}
