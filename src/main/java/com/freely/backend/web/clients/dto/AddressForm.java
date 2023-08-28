package com.freely.backend.web.clients.dto;


import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AddressForm {
  @NotBlank
  private String street;

  @NotBlank
  private String number;

  @NotBlank
  private String zipCode;

  @NotBlank
  private String city;

  @NotBlank
  private String state;

  private String complement;

  private String reference;
}
