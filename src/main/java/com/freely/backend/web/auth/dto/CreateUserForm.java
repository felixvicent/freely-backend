package com.freely.backend.web.auth.dto;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.freely.backend.role.Role;

import lombok.Data;

@Data
public class CreateUserForm {
  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Size(min = 3, max = 50)
  private String name;

  @NotBlank
  @Size(min = 3, max = 20)
  private String password;

  private Role role;
}
