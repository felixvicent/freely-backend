package com.freely.backend.activity;

import com.freely.backend.exceptions.ResourceNotFoundException;
import com.freely.backend.project.Project;
import com.freely.backend.project.ProjectRepository;
import com.freely.backend.project.ProjectStatusEnum;
import com.freely.backend.user.UserAccount;
import com.freely.backend.user.UserService;
import com.freely.backend.web.activity.dto.ActivityForm;
import com.freely.backend.web.activity.dto.ActivityDTO;
import com.freely.backend.web.activity.dto.UpdateActivityResponsibleForm;
import com.freely.backend.web.project.dto.ProjectDTO;
import com.freely.backend.web.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserService userService;


    public List<ActivityDTO> findByStatus(ActivityStatusEnum status, UUID projectId, List<UUID> collaboratorsIds, UserAccount user) {
        boolean filterByCollaboratorIds = collaboratorsIds != null;

        if(projectId == null) {
            return  activityRepository.findByStatusAndCompany(status, user.getCompany(), collaboratorsIds, filterByCollaboratorIds).stream().map(this::entityToDTO).toList();
        }
        return activityRepository.findByStatusAndCompanyAndProject(status, projectId, user.getCompany(), collaboratorsIds, filterByCollaboratorIds).stream().map(this::entityToDTO).toList();
    }

    public ActivityDTO create(ActivityForm form, UserAccount user) {
        Optional<Project> project = projectRepository.findByIdAndCompany(form.getProjectId(), user.getCompany());

        if (project.isEmpty()) {
            throw new ResourceNotFoundException("Projeto não encontrado");
        }

        Activity activity = new Activity();

        activity.setTitle(form.getTitle());
        activity.setEstimatedDate(form.getEstimatedDate());
        activity.setStatus(ActivityStatusEnum.PENDING);
        activity.setProject(project.get());
        activity.setCompany(user.getCompany());

        return entityToDTO(activityRepository.save(activity));
    }

    public ActivityDTO update(ActivityForm form, UUID activityId, UserAccount user) {
        Optional<Project> project = projectRepository.findByIdAndCompany(form.getProjectId(), user.getCompany());

        if (project.isEmpty()) {
            throw new ResourceNotFoundException("Projeto não encontrado");
        }

        Optional<Activity> activity = activityRepository.findById(activityId);

        if (activity.isEmpty()) {
            throw new ResourceNotFoundException("Atividade não encontrada");
        }

        if(activity.get().getStatus() != form.getStatus()) {
            project.get().setStatus(ProjectStatusEnum.PROGRESS);

            projectRepository.save(project.get());
        }

        activity.get().setTitle(form.getTitle());
        activity.get().setStatus(form.getStatus());

        if(form.getStatus() == ActivityStatusEnum.DONE) {
            activity.get().setFinishedAt(LocalDateTime.now());
        }


        return entityToDTO(activityRepository.save(activity.get()));
    }

    public void remove(UUID activityId) {
        Optional<Activity> activity = activityRepository.findById(activityId);

        if (activity.isEmpty()) {
            throw new ResourceNotFoundException("Atividade não encontrado");
        }

        activityRepository.delete(activity.get());
    }

    public Long countByCompany(UserAccount user, LocalDate periodStart, LocalDate periodEnd) {
        LocalDate start = LocalDate.of(2000, 1, 1);
        LocalDate end = LocalDate.of(2100, 1, 1);

        if(periodStart != null) {
            start = periodStart;
        }
        if(periodEnd != null) {
            end = periodEnd;
        }
        return activityRepository.countByCompany(user.getCompany(), start, end);
    }

    public Activity findById(UUID activityId) {
        Optional<Activity> activity = activityRepository.findById(activityId);

        if(activity.isEmpty()) {
            throw new ResourceNotFoundException("Atividade não encontrada");
        }

        return activity.get();
    }

    public ActivityDTO show(UUID activityId) {
        Activity activity = findById(activityId);

        return entityToDTO(activity);
    }

    public void updateResponsible(UUID activityId, UpdateActivityResponsibleForm updateActivityResponsibleForm){
        Activity activity = findById(activityId);

        if(updateActivityResponsibleForm.getResponsibleId() == null) {
            activity.setResponsible(null);
        } else {
            UserAccount collaborator = userService.findById(updateActivityResponsibleForm.getResponsibleId());

            activity.setResponsible(collaborator);
        }

        activityRepository.save(activity);
    }


    private ActivityDTO entityToDTO(Activity activity) {
        ActivityDTO dto = ActivityDTO.builder()
                .id(activity.getId())
                .title(activity.getTitle())
                .status(activity.getStatus())
                .createdAt(activity.getCreatedAt())
                .estimatedDate(activity.getEstimatedDate())
                .finishedAt(activity.getFinishedAt())
                .project(ProjectDTO.builder()
                        .id(activity.getProject().getId())
                        .value(activity.getProject().getValue())
                        .title(activity.getProject().getTitle())
                        .estimatedDate(activity.getProject().getEstimatedDate())
                        .build())
                .build();

        if(activity.getResponsible() != null) {
            dto.setResponsible(UserDTO.builder()
                    .id(activity.getResponsible().getId())
                    .name(activity.getResponsible().getName())
                    .active(activity.getResponsible().isActive())
                    .email(activity.getResponsible().getEmail())
                    .office(activity.getResponsible().getOffice())
                    .role(activity.getResponsible().getRoles().iterator().next().getName())
                    .telephone(activity.getResponsible().getTelephone())
                    .document(activity.getResponsible().getDocument())
                    .build());
        }

        return dto;
    }
}
