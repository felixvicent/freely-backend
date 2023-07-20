package com.freely.backend.user;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.freely.backend.exceptions.ResourceAlreadyExistsException;
import com.freely.backend.exceptions.ResourceNotFoundException;
import com.freely.backend.role.Role;
import com.freely.backend.web.auth.dto.CreateUserForm;
import com.freely.backend.web.user.dto.UpdateUserForm;
import com.freely.backend.web.user.dto.UserDTO;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder encoder;

  public Optional<UserAccount> loadForAuthentication(String email) {
    return userRepository.findByEmail(email);
  }

  public Optional<UserAccount> loadForAuthenticationById(UUID id) {
    return userRepository.findById(id);
  }

  @Transactional
  public UserDTO createUser(CreateUserForm form) {
    if (this.loadForAuthentication(form.getEmail()).isPresent()) {
      throw new ResourceAlreadyExistsException("Usuário já existe");
    }

    var userAccount = new UserAccount();
    BeanUtils.copyProperties(form, userAccount);
    userAccount.setPassword(encoder.encode(form.getPassword()));

    var role = new Role(Role.USER_ROLE_ID);

    userAccount.getRoles().add(role);
    userAccount.setActive(true);

    return entityToDTO(userRepository.save(userAccount));
  }

  public Page<UserDTO> listAll(Pageable pageable) {
    Page<UserDTO> users = userRepository.findAll(pageable).map(user -> entityToDTO(user));

    return users;
  }

  @Transactional
  public UserDTO updateUser(UserAccount user, UpdateUserForm form) {
    Optional<UserAccount> userToUpdate = userRepository.findById(user.getId());

    if (!userToUpdate.isPresent()) {
      throw new ResourceNotFoundException("Usuário não existe");
    }

    var userAccount = new UserAccount();

    BeanUtils.copyProperties(form, userAccount);
    userAccount.setId(user.getId());

    var role = new Role(form.getRole().getId());

    userAccount.getRoles().add(role);
    userAccount.setPassword(userToUpdate.get().getPassword());

    return entityToDTO(userRepository.save(userAccount));
  }

  public void updateUserAvatar(UserAccount user, String avatarUrl) {
    Optional<UserAccount> userToUpdate = userRepository.findById(user.getId());

    if (!userToUpdate.isPresent()) {
      throw new ResourceNotFoundException("Usuário não existe");
    }

    var userAccount = new UserAccount();

    BeanUtils.copyProperties(userToUpdate.get(), userAccount);

    userAccount.setAvatar(avatarUrl);

    userRepository.save(userAccount);
  }

  private UserDTO entityToDTO(UserAccount user) {
    return UserDTO.builder()
        .id(user.getId())
        .name(user.getName())
        .email(user.getEmail())
        .active(user.isActive())
        .role(user.getRoles().iterator().next().getName())
        .createdAt(user.getCreatedAt())
        .build();
  }

}
