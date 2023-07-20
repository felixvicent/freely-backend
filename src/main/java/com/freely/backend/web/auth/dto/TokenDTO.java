package com.freely.backend.web.auth.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDTO {

  private String type;
  private String token;
  private List<String> roles;

}
