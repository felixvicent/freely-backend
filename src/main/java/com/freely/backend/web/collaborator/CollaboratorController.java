package com.freely.backend.web.collaborator;

import com.freely.backend.suggestion.dto.SuggestionDTO;
import com.freely.backend.user.UserAccount;
import com.freely.backend.user.UserService;
import com.freely.backend.user.collaborator.CollaboratorService;
import com.freely.backend.web.collaborator.dto.CreateCollaboratorForm;
import com.freely.backend.web.collaborator.dto.UpdateCollaboratorForm;
import com.freely.backend.web.user.dto.UserDTO;
import org.apache.catalina.User;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/collaborators")
public class CollaboratorController {

    @Autowired
    private CollaboratorService collaboratorService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> create(@AuthenticationPrincipal UserAccount userAccount,
                                          @RequestBody @Valid CreateCollaboratorForm createCollaboratorForm) {
        UserDTO collaborator = collaboratorService.create(createCollaboratorForm, userAccount);

        return ResponseEntity.status(HttpStatus.CREATED).body(collaborator);
    }

    @PutMapping("/{collaboratorId}")
    public ResponseEntity<UserDTO> update(@AuthenticationPrincipal UserAccount userAccount,
                                          @RequestBody @Valid UpdateCollaboratorForm form,
                                          @PathVariable UUID collaboratorId) {
        UserDTO updatedCollaborator = collaboratorService.update(form, collaboratorId, userAccount);

        return ResponseEntity.status(HttpStatus.OK).body(updatedCollaborator);
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> list(@AuthenticationPrincipal UserAccount userAccount,
                                              @PageableDefault(sort = "name", direction = Sort.Direction.DESC) Pageable pageable,
                                              @RequestParam(required = false, value = "collaboratorIds[]") List<UUID> collaboratorIds) {

        Page<UserDTO> collaborators = collaboratorService.listAll(userAccount, collaboratorIds, pageable);

        return ResponseEntity.ok(collaborators);
    }

    @GetMapping("/suggestion")
    public ResponseEntity<List<SuggestionDTO>> getSuggestion(
            @AuthenticationPrincipal UserAccount user,
            @RequestParam String query,
            @RequestParam(required = false) UUID selectedCollaboratorId) {

        if (selectedCollaboratorId != null) {
            UserDTO collaborator = userService.getById(selectedCollaboratorId);

            List<SuggestionDTO> clients = collaboratorService.getSuggestion(collaborator.getName(), user);

            return ResponseEntity.status(HttpStatus.OK).body(clients);
        }
        List<SuggestionDTO> collaborators = collaboratorService.getSuggestion(query, user);

        return ResponseEntity.status(HttpStatus.OK).body(collaborators);
    }

    @DeleteMapping("/{collaboratorId}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserAccount userAccount,
                                    @PathVariable UUID collaboratorId) {
        collaboratorService.delete(collaboratorId, userAccount);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll(@AuthenticationPrincipal UserAccount userAccount) {
        return ResponseEntity.ok(collaboratorService.listAll(userAccount));
    }

}
