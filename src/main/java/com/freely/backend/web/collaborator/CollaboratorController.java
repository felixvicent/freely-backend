package com.freely.backend.web.collaborator;

import com.freely.backend.user.UserAccount;
import com.freely.backend.user.collaborator.CollaboratorService;
import com.freely.backend.web.collaborator.dto.CreateCollaboratorForm;
import com.freely.backend.web.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/collaborators")
public class CollaboratorController {

    @Autowired
    private CollaboratorService collaboratorService;

    @PostMapping
    public ResponseEntity<UserDTO> create(@AuthenticationPrincipal UserAccount userAccount,
                                          @RequestBody @Valid CreateCollaboratorForm createCollaboratorForm) {
        UserDTO collaborator = collaboratorService.create(createCollaboratorForm, userAccount);

        return ResponseEntity.status(HttpStatus.CREATED).body(collaborator);
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> list(@AuthenticationPrincipal UserAccount userAccount,
                                              @PageableDefault(sort = "name", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<UserDTO> collaborators = collaboratorService.listAll(userAccount, pageable);

        return ResponseEntity.ok(collaborators);
    }


}
