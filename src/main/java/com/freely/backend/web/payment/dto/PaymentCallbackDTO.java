package com.freely.backend.web.payment.dto;

import lombok.Data;

@Data
public class PaymentCallbackDTO {
    private String event;
    private PaymentCallbackObjectDTO payment;
}
