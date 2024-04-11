package com.freely.backend.client.client_asaas.dto;

import lombok.Data;

@Data
public class ClientAsaasForm {

    private String name;
    private String cpfCnpj;
    private String email;
    private String phone;
    private String address;
    private String addressNumber;
    private String complement;
    private String province;
    private String postalCode;
    private String externalReference;
    private String company;
    private boolean notificationDisabled;
}
