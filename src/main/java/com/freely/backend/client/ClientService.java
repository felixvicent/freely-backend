package com.freely.backend.client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freely.backend.exceptions.ResourceNotFoundException;
import com.freely.backend.user.UserAccount;
import com.freely.backend.web.clients.dto.AddressDTO;
import com.freely.backend.web.clients.dto.ClientDTO;
import com.freely.backend.web.clients.dto.ClientForm;

@Service
public class ClientService {
  @Autowired
  private ClientRepository clientRepository;

  public List<ClientDTO> findAll(UserAccount user) {
    List<Client> clients = clientRepository.findByUser(user);

    List<ClientDTO> parsedClients = clients.stream().map(client -> entityToDTO(client)).toList();

    return parsedClients;
  }

  public ClientDTO create(ClientForm form, UserAccount user) {
    Client newClient = new Client();
    Address newAddress = new Address();

    BeanUtils.copyProperties(form, newClient);
    BeanUtils.copyProperties(form.getAddress(), newAddress);

    newClient.setUser(user);
    newClient.setAddress(newAddress);

    Client savedClient = clientRepository.save(newClient);

    return entityToDTO(savedClient);
  }

  public ClientDTO update(ClientForm form, UUID clientId, UserAccount user) {
    Optional<Client> client = clientRepository.findByIdAndUser(clientId, user);

    if (!client.isPresent()) {
      throw new ResourceNotFoundException("Cliente não encontrado");
    }

    Client updatedClient = new Client();
    Address updatedAddress = new Address();

    BeanUtils.copyProperties(form, updatedClient);
    BeanUtils.copyProperties(form.getAddress(), updatedAddress);

    updatedClient.setId(clientId);
    updatedClient.setUser(user);
    updatedClient.setAddress(updatedAddress);

    Client savedClient = clientRepository.save(updatedClient);

    return entityToDTO(savedClient);
  }

  public void delete(UUID clientId, UserAccount user) {
    Optional<Client> client = clientRepository.findByIdAndUser(clientId, user);

    if (!client.isPresent()) {
      throw new ResourceNotFoundException("Cliente não encontrado");
    }

    clientRepository.delete(client.get());
  }

  private ClientDTO entityToDTO(Client client) {
    return ClientDTO.builder()
        .id(client.getId())
        .firstName(client.getFirstName())
        .lastName(client.getLastName())
        .telephone(client.getTelephone())
        .document(client.getDocument())
        .email(client.getDocument())
        .createdAt(client.getCreatedAt())
        .address(AddressDTO.builder()
            .id(client.getAddress().getId())
            .street(client.getAddress().getStreet())
            .number(client.getAddress().getNumber())
            .zipCode(client.getAddress().getZipCode())
            .city(client.getAddress().getCity())
            .state(client.getAddress().getState())
            .complement(client.getAddress().getComplement())
            .reference(client.getAddress().getReference())
            .build())
        .build();
  }
}
