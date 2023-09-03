package com.freely.backend.web.project;

import com.freely.backend.project.ProjectService;
import com.freely.backend.user.UserAccount;
import com.freely.backend.web.project.dto.ProjectDTO;
import com.freely.backend.web.project.dto.ProjectForm;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<Page<ProjectDTO>> index(@PageableDefault(sort = "title", direction = Sort.Direction.DESC) Pageable pageable,
                                                  @AuthenticationPrincipal UserAccount user) {

        Page<ProjectDTO> projects = projectService.findAll(user, pageable);

        return ResponseEntity.ok(projects);

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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserAccount user, @PathVariable UUID id) {

        projectService.delete(user, id);

        return ResponseEntity.noContent().build();
    }
}
