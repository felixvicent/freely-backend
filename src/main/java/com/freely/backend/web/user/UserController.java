package com.freely.backend.web.user;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import com.freely.backend.suggestion.dto.SuggestionDTO;
import com.freely.backend.web.user.dto.CreateUserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.freely.backend.upload.UploadService;
import com.freely.backend.user.UserAccount;
import com.freely.backend.user.UserService;
import com.freely.backend.web.user.dto.UpdateUserForm;
import com.freely.backend.web.user.dto.UserDTO;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UploadService uploadService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<UserDTO>> list(
            @PageableDefault(sort = "name", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, value = "usersIds[]") List<UUID> usersIds) {

        Page<UserDTO> users;
        if (usersIds == null) {
            users = userService.listAll(pageable);
        } else {
            users = userService.listAllByIds(pageable, usersIds);
        }

        return ResponseEntity.ok(users);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> store(@Valid @RequestBody CreateUserForm createUserForm) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(createUserForm));
    }

    @PutMapping
    public ResponseEntity<Object> update(@AuthenticationPrincipal UserAccount user,
                                         @RequestBody @Valid UpdateUserForm form) {
        UserDTO updatedUser = userService.updateUser(user, form);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(updatedUser);
    }

    @PutMapping("/avatar")
    public ResponseEntity<Object> updateAvatar(@AuthenticationPrincipal UserAccount user,
                                               @RequestParam("image") MultipartFile file) throws IOException {

        String uploadedFilePath = uploadService.uploadImage(file, "users");

        userService.updateUserAvatar(user, uploadedFilePath);

        return ResponseEntity.ok().body(uploadedFilePath);
    }

    @GetMapping("/suggestion")
    public ResponseEntity<List<SuggestionDTO>> getSuggestion(@RequestParam String query) {

        List<SuggestionDTO> usersSuggestion = userService.getSuggestion(query);

        return ResponseEntity.status(HttpStatus.OK).body(usersSuggestion);
    }

}
