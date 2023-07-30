package com.freely.backend.web.clients.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDTO {
  private UUID id;

  private String street;

  private String number;

  private String zipCode;

  private String city;

  private String state;

  private String complement;

  private String reference;
}
