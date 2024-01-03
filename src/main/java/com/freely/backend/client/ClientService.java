package com.freely.backend.client;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.freely.backend.exceptions.ResourceAlreadyExistsException;
import com.freely.backend.suggestion.dto.SuggestionDTO;
import com.freely.backend.web.clients.dto.ClientPageDTO;
import com.freely.backend.web.activity.dto.ActivityDTO;
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


    public Page<ClientListDTO> findAll(UserAccount company, List<UUID> clientIds, Pageable pageable) {
        boolean filterById = clientIds != null;

        return clientRepository.findByCompany(company, clientIds, filterById, pageable).map(this::entityToListDTO);
    }


    public ClientListDTO create(ClientForm form, UserAccount user) {
        Optional<Client> client = clientRepository.findByDocumentAndCompany(form.getDocument(), user);

        if (client.isPresent()) {
            throw new ResourceAlreadyExistsException("Já existe cliente com esse CPF/CNPJ");
        }

        Client newClient = new Client();
        Address newAddress = new Address();

        BeanUtils.copyProperties(form, newClient);
        BeanUtils.copyProperties(form.getAddress(), newAddress);

        newClient.setCompany(user);
        newClient.setAddress(newAddress);

        Client savedClient = clientRepository.save(newClient);

        return entityToListDTO(savedClient);
    }

    public ClientListDTO update(ClientForm form, UUID clientId, UserAccount user) {
        Optional<Client> client = clientRepository.findByIdAndCompany(clientId, user);

        if (client.isEmpty()) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        Client updatedClient = new Client();
        Address updatedAddress = new Address();

        BeanUtils.copyProperties(form, updatedClient);
        BeanUtils.copyProperties(form.getAddress(), updatedAddress);

        updatedClient.setId(clientId);
        updatedClient.setCompany(user);
        updatedClient.setAddress(updatedAddress);

        Client savedClient = clientRepository.save(updatedClient);

        return entityToListDTO(savedClient);
    }

    public void delete(UUID clientId, UserAccount user) {
        Optional<Client> client = clientRepository.findByIdAndCompany(clientId, user);

        if (client.isEmpty()) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        clientRepository.delete(client.get());
    }

    public List<SuggestionDTO> getSuggestion(String query, UserAccount user) {
        return clientRepository.findSuggestions(query, user.getId()).stream().map(client -> SuggestionDTO.builder().label(client.getName()).value(client.getId()).build()).toList();
    }

    public ClientPageDTO findById(UUID clientId, UserAccount user) {
        Optional<Client> client = clientRepository.findByIdAndCompany(clientId, user);

        if (client.isEmpty()) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        return entityToPageDTO(client.get());
    }

    public long countByCompany(UserAccount company, LocalDate periodStart, LocalDate periodEnd) {
        LocalDate start = LocalDate.of(2000, 1, 1);
        LocalDate end = LocalDate.of(2100, 1, 1);

        if(periodStart != null) {
            start = periodStart;
        }
        if(periodEnd != null) {
            end = periodEnd;
        }
        return clientRepository.countByCompany(company, start, end);
    }

    private ClientListDTO entityToListDTO(Client client) {
        return ClientListDTO.builder()
                .id(client.getId())
                .name(client.getName())
                .telephone(client.getTelephone())
                .email(client.getEmail())
                .quantityOfProjects(client.getProjects().size())
                .build();
    }

    private ClientPageDTO entityToPageDTO(Client client) {
        return ClientPageDTO.builder()
                .id(client.getId())
                .name(client.getName())
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
