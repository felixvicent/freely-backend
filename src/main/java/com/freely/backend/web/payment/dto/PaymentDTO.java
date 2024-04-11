package com.freely.backend.web.payment.dto;

import com.freely.backend.client.Client;
import com.freely.backend.payment.PaymentStatusEnum;
import com.freely.backend.project.Project;
import com.freely.backend.web.clients.dto.ClientListDTO;
import com.freely.backend.web.project.dto.ProjectDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PaymentDTO {

    private UUID id;
    private ProjectDTO project;
    private ClientListDTO client;
    private String invoiceUrl;
    private PaymentStatusEnum status;
    private double value;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
