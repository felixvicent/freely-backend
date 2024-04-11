package com.freely.backend.payment.payment_asaas;

import com.freely.backend.client.client_asaas.ClientAsaas;
import com.freely.backend.integration.asaas.IntegrationAsaasService;
import com.freely.backend.payment.payment_asaas.dto.PaymentAsaasDTO;
import com.freely.backend.payment.payment_asaas.dto.PaymentAsaasForm;
import com.freely.backend.project.Project;
import com.freely.backend.user.user_properties.dto.UserPropertyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentAsaasHttpService {
    @Autowired
    private IntegrationAsaasService integrationAsassService;

    @Value("${asaas.baseUrl}")
    private String asaasBaseUrl;

    public PaymentAsaasDTO createPayment(Project project, ClientAsaas clientAsaas, String dueDate) {
        HttpHeaders httpHeaders = getHeader(project);
        RestTemplate restTemplate = new RestTemplate();

        PaymentAsaasForm paymentAsaasForm = new PaymentAsaasForm();

        paymentAsaasForm.setCustomer(clientAsaas.getAsaasCustomerId());
        paymentAsaasForm.setValue(project.getValue());
        paymentAsaasForm.setDueDate(dueDate);
        paymentAsaasForm.setDescription(project.getTitle());
        paymentAsaasForm.setExternalReference(project.getId().toString());
        paymentAsaasForm.setBillingType("UNDEFINED");

        HttpEntity<PaymentAsaasForm> request = new HttpEntity<>(paymentAsaasForm, httpHeaders);
        String url = generateUrl("/payments");

        ResponseEntity<PaymentAsaasDTO> result = restTemplate.postForEntity(url, request, PaymentAsaasDTO.class);

        return result.getBody();
    }

    private HttpHeaders getHeader(Project project) {
        UserPropertyDTO userPropertyDTO = integrationAsassService.get(project.getCompany());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("access_token", userPropertyDTO.getValue());

        return httpHeaders;
    }

    private String generateUrl(String path) {
        return asaasBaseUrl + path;
    }
}
