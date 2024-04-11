package com.freely.backend.payment.payment_asaas.dto;

import lombok.Data;

@Data
public class PaymentAsaasForm {
    private String customer;
    private String billingType;
    private double value;
    private String dueDate;
    private String description;
    private String externalReference;
}
