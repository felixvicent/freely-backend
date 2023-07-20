package com.freely.backend.web.user.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.freely.backend.role.Role;

import lombok.Data;

@Data
public class UpdateUserForm {
  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Size(min = 3, max = 50)
  private String name;

  @NotNull
  private boolean active;

  private Role role;
}
