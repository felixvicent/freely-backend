package com.freely.backend.project;

import com.freely.backend.client.Client;
import com.freely.backend.client.ClientRepository;
import com.freely.backend.exceptions.ResourceNotFoundException;
import com.freely.backend.project.entities.Activity;
import com.freely.backend.project.entities.Project;
import com.freely.backend.project.repositories.ActivityRepository;
import com.freely.backend.project.repositories.ProjectRepository;
import com.freely.backend.user.UserAccount;
import com.freely.backend.web.clients.dto.ClientListDTO;
import com.freely.backend.web.project.dto.ActivityDTO;
import com.freely.backend.web.project.dto.ProjectDTO;
import com.freely.backend.web.project.dto.ProjectForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ActivityRepository activityRepository;

    public Page<ProjectDTO> findAll(UserAccount user, Pageable pageable) {
        return projectRepository.findByUser(user, pageable).map(this::entityToDTO);
    }

    @Transactional
    public ProjectDTO save(UserAccount user, ProjectForm form) {
        Optional<Client> client = clientRepository.findByIdAndUser(form.getClientId(), user);

        if (client.isEmpty()) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }

        Project newProject = new Project();

        newProject.setTitle(form.getTitle());
        newProject.setValue(form.getValue());
        newProject.setEstimatedDate(form.getEstimedDate());
        newProject.setClient(client.get());
        newProject.setUser(user);

        projectRepository.save(newProject);

        form.getActivities().forEach(activityForm -> {
            Activity activity = new Activity();
            activity.setTitle(activityForm.getTitle());
            activity.setStatus(ActivityStatusEnum.PENDING);
            activity.setProject(newProject);

            activityRepository.save(activity);
        });


        return entityToDTO(projectRepository.save(newProject));

    }

    @Transactional
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
        updatedProject.setEstimatedDate(form.getEstimedDate());
        updatedProject.setClient(client.get());

        projectRepository.save(updatedProject);

        updatedProject.getActivities().forEach(activity -> activityRepository.delete(activity));

        form.getActivities().forEach(activityForm -> {
            Activity activity = new Activity();
            activity.setTitle(activityForm.getTitle());
            activity.setStatus(ActivityStatusEnum.PENDING);
            activity.setProject(updatedProject);

            activityRepository.save(activity);
        });

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
                .estimatedDate(project.getEstimatedDate())
                .createdAt(project.getCreatedAt())
                .client(ClientListDTO.builder()
                        .id(project.getClient().getId())
                        .email(project.getClient().getEmail())
                        .firstName(project.getClient().getFirstName())
                        .lastName(project.getClient().getLastName())
                        .quantityOfProjects(project.getClient().getProjects().size())
                        .build())
                .activities(project.getActivities().stream().map(activity -> ActivityDTO.builder()
                        .id(activity.getId())
                        .title(activity.getTitle())
                        .status(activity.getStatus())
                        .createdAt(activity.getCreatedAt())
                        .build()).toList())
                .build();
    }
}
