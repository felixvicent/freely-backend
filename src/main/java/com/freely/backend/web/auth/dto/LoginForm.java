package com.freely.backend.web.auth.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginForm {

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String password;
}
