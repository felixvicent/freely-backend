package com.freely.backend.client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.freely.backend.exceptions.ResourceAlreadyExistsException;
import com.freely.backend.web.clients.dto.ClientPageDTO;
import com.freely.backend.web.project.dto.ActivityDTO;
import com.freely.backend.web.project.dto.ProjectDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.freely.backend.exceptions.ResourceNotFoundException;
import com.freely.backend.user.UserAccount;
import com.freely.backend.web.clients.dto.AddressDTO;
import com.freely.backend.web.clients.dto.ClientListDTO;
import com.freely.backend.web.clients.dto.ClientForm;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public Page<ClientListDTO> findAll(UserAccount user, String query, Pageable pageable) {
        return clientRepository.findByUser(user, query, pageable).map(this::entityToListDTO);
    }

    public List<ClientListDTO> findLatest(UserAccount user) {
        return clientRepository.findLatest(user.getId()).stream().map(this::entityToListDTO).toList();
    }

    public ClientListDTO create(ClientForm form, UserAccount user) {
        Optional<Client> client = clientRepository.findByDocumentAndUser(form.getDocument(), user);

        if (client.isPresent()) {
            throw new ResourceAlreadyExistsException("Já existe cliente com esse CPF/CNPJ");
        }

        Client newClient = new Client();
        Address newAddress = new Address();

        BeanUtils.copyProperties(form, newClient);
        BeanUtils.copyProperties(form.getAddress(), newAddress);

        newClient.setUser(user);
        newClient.setAddress(newAddress);

        Client savedClient = clientRepository.save(newClient);

        return entityToListDTO(savedClient);
    }

    public ClientListDTO update(ClientForm form, UUID clientId, UserAccount user) {
        Optional<Client> client = clientRepository.findByIdAndUser(clientId, user);

        if (client.isEmpty()) {
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

        return entityToListDTO(savedClient);
    }

    public void delete(UUID clientId, UserAccount user) {
        Optional<Client> client = clientRepository.findByIdAndUser(clientId, user);

        if (client.isEmpty()) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        clientRepository.delete(client.get());
    }

    public List<ClientListDTO> getSuggestion(String query, UserAccount user) {
        return clientRepository.findSuggestions(query, user.getId()).stream().map(this::entityToListDTO).toList();
    }

    public ClientPageDTO findById(UUID clientId, UserAccount user) {
        Optional<Client> client = clientRepository.findByIdAndUser(clientId, user);

        if (client.isEmpty()) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        return entityToPageDTO(client.get());
    }

    public long countByUser(UserAccount user) {
        return clientRepository.countByUser(user);
    }


    private ClientListDTO entityToListDTO(Client client) {
        return ClientListDTO.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .telephone(client.getTelephone())
                .email(client.getEmail())
                .quantityOfProjects(client.getProjects().size())
                .build();
    }

    private ClientPageDTO entityToPageDTO(Client client) {
        return ClientPageDTO.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .telephone(client.getTelephone())
                .email(client.getEmail())
                .document(client.getDocument())
                .address(AddressDTO.builder()
                        .street(client.getAddress().getStreet())
                        .zipCode(client.getAddress().getZipCode())
                        .city(client.getAddress().getCity())
                        .state(client.getAddress().getState())
                        .reference(client.getAddress().getReference())
                        .number(client.getAddress().getNumber())
                        .complement(client.getAddress().getComplement())
                        .id(client.getAddress().getId())
                        .build())
                .projects(client.getProjects().stream().map(project -> ProjectDTO.builder()
                        .id(project.getId())
                        .title(project.getTitle())
                        .value(project.getValue())
                        .estimatedDate(project.getEstimatedDate())
                        .createdAt(project.getCreatedAt())
                        .activities(project.getActivities().stream().map(activity -> ActivityDTO.builder()
                                .id(activity.getId())
                                .title(activity.getTitle())
                                .status(activity.getStatus())
                                .createdAt(activity.getCreatedAt())
                                .build()).toList())
                        .build()).toList())
                .build();
    }
}
