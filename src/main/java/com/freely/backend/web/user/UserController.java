package com.freely.backend.web.user;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
      @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
    var users = userService.listAll(pageable);

    return ResponseEntity.ok(users);
  }

  @PutMapping
  public ResponseEntity<Object> update(@AuthenticationPrincipal UserAccount user,
      @RequestBody @Valid UpdateUserForm form) {
    UserDTO updatedUser = userService.updateUser(user, form);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(updatedUser);
  }

  @PutMapping("/{userId}/avatar")
  public ResponseEntity<Object> updateAvatar(@AuthenticationPrincipal UserAccount user,
      @RequestParam("image") MultipartFile file) throws IOException {

    String uploadedFilePath = uploadService.uploadImage(file, "users");

    userService.updateUserAvatar(user, uploadedFilePath);

    return ResponseEntity.ok().body(uploadedFilePath);
  }
}
