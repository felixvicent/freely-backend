package com.freely.backend.activity;

import com.freely.backend.exceptions.ResourceNotFoundException;
import com.freely.backend.project.ActivityStatusEnum;
import com.freely.backend.project.Project;
import com.freely.backend.project.repositories.ProjectRepository;
import com.freely.backend.user.UserAccount;
import com.freely.backend.web.activity.dto.ActivityForm;
import com.freely.backend.web.project.dto.ActivityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ActivityDTO create(ActivityForm form, UserAccount user) {
        Optional<Project> project = projectRepository.findByIdAndUser(form.getProjectId(), user);

        if (project.isEmpty()) {
            throw new ResourceNotFoundException("Projeto não encontrado");
        }

        Activity activity = new Activity();

        activity.setTitle(form.getTitle());
        activity.setStatus(ActivityStatusEnum.PENDING);
        activity.setProject(project.get());

        return entityToDTO(activityRepository.save(activity));
    }

    public ActivityDTO update(ActivityForm form, UUID activityId, UserAccount user) {
        Optional<Project> project = projectRepository.findByIdAndUser(form.getProjectId(), user);

        if (project.isEmpty()) {
            throw new ResourceNotFoundException("Projeto não encontrado");
        }

        Optional<Activity> activity = activityRepository.findById(activityId);

        if (activity.isEmpty()) {
            throw new ResourceNotFoundException("Atividade não encontrada");
        }

        activity.get().setTitle(form.getTitle());
        activity.get().setStatus(form.getStatus());

        return entityToDTO(activityRepository.save(activity.get()));
    }

    public void remove(UUID activityId) {
        Optional<Activity> activity = activityRepository.findById(activityId);

        if (activity.isEmpty()) {
            throw new ResourceNotFoundException("Atividade não encontrado");
        }

        activityRepository.delete(activity.get());
    }

    private ActivityDTO entityToDTO(Activity activity) {
        return ActivityDTO.builder()
                .id(activity.getId())
                .title(activity.getTitle())
                .status(activity.getStatus())
                .build();
    }
}
