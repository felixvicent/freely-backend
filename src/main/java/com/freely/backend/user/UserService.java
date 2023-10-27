package com.freely.backend.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import com.freely.backend.suggestion.dto.SuggestionDTO;
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
import org.springframework.web.bind.annotation.RequestParam;

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
        return userRepository.findAll(pageable).map(this::entityToDTO);
    }

    public Page<UserDTO> listAllByIds(Pageable pageable, List<UUID> usersIds) {
        return userRepository.findAllByIds(usersIds, pageable).map(this::entityToDTO);
    }

    @Transactional
    public UserDTO updateUser(UserAccount user, UpdateUserForm form) {
        Optional<UserAccount> userToUpdate = userRepository.findById(user.getId());

        if (userToUpdate.isEmpty()) {
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

        if (userToUpdate.isEmpty()) {
            throw new ResourceNotFoundException("Usuário não existe");
        }

        var userAccount = new UserAccount();

        BeanUtils.copyProperties(userToUpdate.get(), userAccount);

        userAccount.setAvatar(avatarUrl);

        userRepository.save(userAccount);
    }

    public void createAdmin(String email, String password, String name) {
        Optional<UserAccount> userExists = userRepository.findByEmail(email);

        if (userExists.isPresent()) {
            return;
        }

        UserAccount user = new UserAccount();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(encoder.encode(password));

        var role = new Role(Role.ADMIN_ROLE_ID);

        user.getRoles().add(role);
        user.setActive(true);

        userRepository.save(user);
    }

    public List<SuggestionDTO> getSuggestion(String query) {
        return userRepository.findSuggestions(query).stream().map(user -> SuggestionDTO.builder().label(user.getName()).value(user.getId()).build()).toList();
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
