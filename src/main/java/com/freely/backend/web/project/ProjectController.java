package com.freely.backend.web.project;

import com.freely.backend.project.ProjectService;
import com.freely.backend.project.ProjectStatusEnum;
import com.freely.backend.user.UserAccount;
import com.freely.backend.web.project.dto.ProjectDTO;
import com.freely.backend.web.project.dto.ProjectForm;
import com.freely.backend.web.project.dto.ProjectStatusForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<Page<ProjectDTO>> index(@PageableDefault(sort = "title", direction = Sort.Direction.DESC) Pageable pageable,
                                                  @RequestParam(required = false, value = "clientIds[]") List<UUID> clientIds,
                                                  @RequestParam(required = false, value = "status[]") List<ProjectStatusEnum> status,
                                                  @AuthenticationPrincipal UserAccount user) {

        Page<ProjectDTO> projects = projectService.findAll(user, clientIds, status, pageable);

        return ResponseEntity.ok(projects);

    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> show(@PathVariable UUID projectId, @AuthenticationPrincipal UserAccount user) {
        ProjectDTO project = projectService.findById(projectId, user);

        return ResponseEntity.ok(project);
    }


    @PostMapping
    public ResponseEntity<ProjectDTO> store(@AuthenticationPrincipal UserAccount user, @RequestBody ProjectForm form) {
        return ResponseEntity.ok(projectService.save(user, form));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> update(
            @AuthenticationPrincipal UserAccount user,
            @RequestBody ProjectForm form,
            @PathVariable UUID id) {

        return ResponseEntity.ok(projectService.update(user, form, id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ProjectDTO> updateStatus(@AuthenticationPrincipal UserAccount user,
                                                   @RequestBody ProjectStatusForm form,
                                                   @PathVariable UUID id) {
        return ResponseEntity.ok(projectService.updateStatus(user, form, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserAccount user, @PathVariable UUID id) {

        projectService.delete(user, id);

        return ResponseEntity.noContent().build();
    }
}
