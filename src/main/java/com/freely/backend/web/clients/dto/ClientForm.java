package com.freely.backend.web.clients.dto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ClientForm {
  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @NotBlank
  private String telephone;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String document;

  @Valid
  private AddressForm address;
}
