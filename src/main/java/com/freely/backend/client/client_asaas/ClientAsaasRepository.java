package com.freely.backend.client.client_asaas;

import com.freely.backend.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientAsaasRepository extends JpaRepository<ClientAsaas, UUID> {

    ClientAsaas findByClient(Client client);
}
