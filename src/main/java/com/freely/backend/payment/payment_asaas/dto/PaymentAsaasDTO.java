package com.freely.backend.payment.payment_asaas.dto;

import lombok.Data;

@Data
public class PaymentAsaasDTO {
    private String id;
    private String invoiceUrl;
    private String invoiceNumber;
    private String status;
    private String externalReference;
}
