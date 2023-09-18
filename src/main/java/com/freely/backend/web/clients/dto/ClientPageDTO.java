package com.freely.backend.web.clients.dto;

import com.freely.backend.web.project.dto.ProjectDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ClientPageDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String document;
    private String email;
    private String telephone;
    private AddressDTO address;
    private List<ProjectDTO> projects;
}
