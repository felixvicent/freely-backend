package com.freely.backend.web.clients.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientListDTO {
    private UUID id;
    private String name;
    private String telephone;
    private String email;
    private long quantityOfProjects;

}
