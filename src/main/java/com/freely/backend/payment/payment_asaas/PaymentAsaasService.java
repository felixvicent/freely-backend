package com.freely.backend.payment.payment_asaas;

import com.freely.backend.client.client_asaas.ClientAsaas;
import com.freely.backend.client.client_asaas.ClientAsaasService;
import com.freely.backend.payment.Payment;
import com.freely.backend.payment.payment_asaas.dto.PaymentAsaasDTO;
import com.freely.backend.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PaymentAsaasService {
    @Autowired
    private ClientAsaasService clientAsaasService;

    @Autowired
    private PaymentAsaasHttpService paymentAsaasHttpService;

    public PaymentAsaasDTO generate(Project project, Payment payment){
        ClientAsaas clientAsaas = clientAsaasService.getClient(project.getClient());

        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusDays(30);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDueDate = dueDate.format(formatter);

        return paymentAsaasHttpService.createPayment(project, payment, clientAsaas, formattedDueDate);
    }
}
