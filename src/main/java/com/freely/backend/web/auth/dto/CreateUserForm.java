package com.freely.backend.web.auth.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
