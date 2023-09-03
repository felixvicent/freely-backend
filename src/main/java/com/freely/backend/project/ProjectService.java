package com.freely.backend.project;

import com.freely.backend.client.Client;
import com.freely.backend.client.ClientRepository;
import com.freely.backend.exceptions.ResourceNotFoundException;
import com.freely.backend.user.UserAccount;
import com.freely.backend.web.clients.dto.AddressDTO;
import com.freely.backend.web.clients.dto.ClientDTO;
import com.freely.backend.web.project.dto.ProjectDTO;
import com.freely.backend.web.project.dto.ProjectForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ClientRepository clientRepository;

    public Page<ProjectDTO> findAll(UserAccount user, Pageable pageable) {
        return projectRepository.findByUser(user, pageable).map(this::entityToDTO);
    }

    public ProjectDTO save(UserAccount user, ProjectForm form) {
        Optional<Client> client = clientRepository.findByIdAndUser(form.getClientId(), user);

        if (client.isEmpty()) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        Project newProject = new Project();

        newProject.setTitle(form.getTitle());
        newProject.setValue(form.getValue());
        newProject.setEstimedDate(form.getEstimedDate());
        newProject.setClient(client.get());
        newProject.setUser(user);

        return entityToDTO(projectRepository.save(newProject));

    }

    public ProjectDTO update(UserAccount user, ProjectForm form, UUID id) {
        Optional<Project> project = projectRepository.findByIdAndUser(id, user);

        if (project.isEmpty()) {
            throw new ResourceNotFoundException("Projeto não encontrado");
        }

        Optional<Client> client = clientRepository.findByIdAndUser(form.getClientId(), user);

        if (client.isEmpty()) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        Project updatedProject = project.get();

        updatedProject.setTitle(form.getTitle());
        updatedProject.setValue(form.getValue());
        updatedProject.setEstimedDate(form.getEstimedDate());
        updatedProject.setClient(client.get());

        return entityToDTO(projectRepository.save(updatedProject));

    }

    public void delete(UserAccount user, UUID id) {
        Optional<Project> project = projectRepository.findByIdAndUser(id, user);

        if (project.isEmpty()) {
            throw new ResourceNotFoundException("Projeto não encontrado");
        }


        projectRepository.delete(project.get());
    }

    private ProjectDTO entityToDTO(Project project) {
        return ProjectDTO.builder()
                .id(project.getId())
                .title(project.getTitle())
                .value(project.getValue())
                .estimedDate(project.getEstimedDate())
                .createdAt(project.getCreatedAt())
                .client(ClientDTO.builder()
                        .id(project.getClient().getId())
                        .email(project.getClient().getEmail())
                        .firstName(project.getClient().getFirstName())
                        .lastName(project.getClient().getLastName())
                        .document(project.getClient().getDocument())
                        .createdAt(project.getClient().getCreatedAt())
                        .address(AddressDTO.builder()
                                .id(project.getClient().getAddress().getId())
                                .city(project.getClient().getAddress().getCity())
                                .complement(project.getClient().getAddress().getComplement())
                                .state(project.getClient().getAddress().getState())
                                .number(project.getClient().getAddress().getNumber())
                                .street(project.getClient().getAddress().getStreet())
                                .reference(project.getClient().getAddress().getReference())
                                .zipCode(project.getClient().getAddress().getZipCode())
                                .build())
                        .build())
                .build();
    }
}
