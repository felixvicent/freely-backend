package com.freely.backend.web.auth.dto;

import lombok.Data;

@Data
public class ActiveAccountForm {
    private String password;
    private String code;
}
