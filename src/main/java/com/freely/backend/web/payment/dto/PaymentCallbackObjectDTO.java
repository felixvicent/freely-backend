package com.freely.backend.web.payment.dto;

import lombok.Data;

@Data
public class PaymentCallbackObjectDTO {
    private String object;
    private String id;
    private String dateCreated;
    private String customer;
    private String dueDate;
    private String value;
    private String externalReference;
    private String status;
}
