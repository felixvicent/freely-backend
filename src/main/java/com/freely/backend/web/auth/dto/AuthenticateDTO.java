package com.freely.backend.web.auth.dto;

import com.freely.backend.web.user.dto.UserDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticateDTO {
  private UserDTO user;
  private TokenDTO token;
}
