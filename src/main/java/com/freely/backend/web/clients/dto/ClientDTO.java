package com.freely.backend.web.clients.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientDTO {
  private UUID id;

  private String firstName;

  private String lastName;

  private String telephone;

  private String email;

  private String document;

  private LocalDateTime createdAt;

  private AddressDTO address;
}
