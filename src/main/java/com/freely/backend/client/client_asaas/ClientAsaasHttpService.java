package com.freely.backend.client.client_asaas;

import com.freely.backend.client.Client;
import com.freely.backend.client.client_asaas.dto.ClientAsaasForm;
import com.freely.backend.client.client_asaas.dto.ClientAsaasDTO;
import com.freely.backend.integration.asaas.IntegrationAsaasService;
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
public class ClientAsaasHttpService {
    @Value("${asaas.baseUrl}")
    private String asaasBaseUrl;

    @Autowired
    private IntegrationAsaasService integrationAsassService;

    public ClientAsaasDTO createClient(Client client) {
        HttpHeaders httpHeaders = getHeader(client);
        RestTemplate restTemplate = new RestTemplate();

        ClientAsaasForm clientAsaasForm = new ClientAsaasForm();

        clientAsaasForm.setName(client.getName());
        clientAsaasForm.setCpfCnpj(client.getDocument());
        clientAsaasForm.setEmail(client.getEmail());
        clientAsaasForm.setPhone(client.getTelephone());
        clientAsaasForm.setAddress(client.getAddress().getStreet());
        clientAsaasForm.setAddressNumber(client.getAddress().getNumber());
        clientAsaasForm.setComplement(client.getAddress().getComplement());
        clientAsaasForm.setPostalCode(client.getAddress().getZipCode());
        clientAsaasForm.setExternalReference(client.getId().toString());
        clientAsaasForm.setNotificationDisabled(true);
        clientAsaasForm.setCompany(client.getCompany().getName());

        HttpEntity<ClientAsaasForm> request = new HttpEntity<>(clientAsaasForm, httpHeaders);
        String url = generateUrl("/customers");

        ResponseEntity<ClientAsaasDTO> result = restTemplate.postForEntity(url, request, ClientAsaasDTO.class);

        return result.getBody();
    }

    private HttpHeaders getHeader(Client client) {
        UserPropertyDTO userPropertyDTO = integrationAsassService.get(client.getCompany());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("access_token", userPropertyDTO.getValue());

        return httpHeaders;
    }

    private String generateUrl(String path) {
        return asaasBaseUrl + path;
    }
}
