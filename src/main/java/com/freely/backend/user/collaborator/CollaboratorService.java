package com.freely.backend.user.collaborator;

import com.freely.backend.authentication.StringHash;
import com.freely.backend.exceptions.ResourceAlreadyExistsException;
import com.freely.backend.exceptions.ResourceNotFoundException;
import com.freely.backend.mail.MailService;
import com.freely.backend.role.Role;
import com.freely.backend.suggestion.dto.SuggestionDTO;
import com.freely.backend.user.UserAccount;
import com.freely.backend.user.UserRepository;
import com.freely.backend.user.UserService;
import com.freely.backend.web.collaborator.dto.CreateCollaboratorForm;
import com.freely.backend.web.collaborator.dto.UpdateCollaboratorForm;
import com.freely.backend.web.user.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CollaboratorService {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    private StringHash stringHash;

    @Autowired
    MailService mailService;

    public UserDTO create(CreateCollaboratorForm createCollaboratorForm, UserAccount userAccount) {
        Optional<UserAccount> user = userRepository.findByEmail(createCollaboratorForm.getEmail());

        if(user.isPresent()) {
            throw new ResourceAlreadyExistsException("Email já cadastrado");
        }

        Optional<UserAccount> userWithDocument = userRepository.findByDocument(createCollaboratorForm.getDocument());

        if (userWithDocument.isPresent()) {
            throw new ResourceAlreadyExistsException("Documento já cadastrado");
        }

        var collaborator = new UserAccount();
        BeanUtils.copyProperties(createCollaboratorForm, collaborator);
        collaborator.setPassword("PASSWORD");
        collaborator.setCompany(userAccount);

        var role = new Role(Role.USER_ROLE_ID);

        collaborator.getRoles().add(role);
        collaborator.setActive(false);

        var activeLink = frontendUrl + "/activeAccount?code=" + stringHash.hash(collaborator.getEmail());

        mailService.sendMail(
                collaborator.getEmail(),
                "Sua conta foi criada, faça a ativação",
                "Ative sua conta acessando o <a href='" + activeLink + "'>link</a>"
        );

        return userService.entityToDTO(userRepository.save(collaborator));
    }

    @Transactional
    public UserDTO update(@Valid UpdateCollaboratorForm form, UUID userId, UserAccount userAccount) {
        Optional<UserAccount> collaboratorToUpdate = userRepository.findById(userId);

        if (collaboratorToUpdate.isEmpty()) {
            throw new ResourceNotFoundException("Usuário não existe");
        }

        Optional<UserAccount> userWithDocument = userRepository.findByDocument(form.getDocument());

        if (userWithDocument.isPresent() && userWithDocument.get().getId() != userId) {
            throw new ResourceAlreadyExistsException("Documento já cadastrado");
        }

        var collaborator = new UserAccount();

        BeanUtils.copyProperties(form, collaborator);
        collaborator.setId(userId);

        collaborator.getRoles().addAll(collaboratorToUpdate.get().getRoles());
        collaborator.setActive(collaboratorToUpdate.get().isActive());
        collaborator.setPassword(collaboratorToUpdate.get().getPassword());
        collaborator.setCompany(userAccount);


        return userService.entityToDTO(userRepository.save(collaborator));
    }

    public Page<UserDTO> listAll(UserAccount user, List<UUID> collaboratorIds, Pageable pageable){
        boolean filterById = collaboratorIds != null;

        return collaboratorRepository.findByCompany(user.getCompany(), collaboratorIds, filterById, pageable).map(collaborator -> userService.entityToDTO(collaborator));
    }

    public List<UserDTO> listAll(UserAccount user){
        return collaboratorRepository.listAllByCompany(user.getCompany()).stream().map(collaborator -> userService.entityToDTO(collaborator)).toList();
    }

    public List<SuggestionDTO> getSuggestion(String query, UserAccount user) {
        return collaboratorRepository.findSuggestions(query, user.getCompany().getId()).stream().map(client -> SuggestionDTO.builder().label(client.getName()).value(client.getId()).build()).toList();
    }

    public void delete(UUID collaboratorId, UserAccount userAccount){
        Optional<UserAccount> userToDelete = userRepository.findById(collaboratorId);

        if (userToDelete.isEmpty()) {
            throw new ResourceNotFoundException("Usuário não existe");
        }

        userRepository.delete(userToDelete.get());
    }

}
