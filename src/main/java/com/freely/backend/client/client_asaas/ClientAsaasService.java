package com.freely.backend.client.client_asaas;

import com.freely.backend.client.Client;
import com.freely.backend.client.client_asaas.dto.ClientAsaasDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientAsaasService {
    @Autowired
    private ClientAsaasRepository clientAsaasRepository;

    @Autowired
    private ClientAsaasHttpService clientAsaasHttpService;

    public ClientAsaas getClient(Client client){
        ClientAsaas clientAsaas = clientAsaasRepository.findByClient(client);

        if(clientAsaas != null) {
            return clientAsaas;
        }

        return create(client);
    }

    public ClientAsaas create(Client client) {
        ClientAsaasDTO clientAsaasDTO = clientAsaasHttpService.createClient(client);

        ClientAsaas clientAsaas = new ClientAsaas();

        clientAsaas.setClient(client);
        clientAsaas.setAsaasCustomerId(clientAsaasDTO.getId());

        return clientAsaasRepository.save(clientAsaas);
    }
}
