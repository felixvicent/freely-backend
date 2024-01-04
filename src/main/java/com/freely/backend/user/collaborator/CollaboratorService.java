package com.freely.backend.user.collaborator;

import com.freely.backend.authentication.StringHash;
import com.freely.backend.exceptions.ResourceAlreadyExistsException;
import com.freely.backend.mail.MailService;
import com.freely.backend.role.Role;
import com.freely.backend.user.UserAccount;
import com.freely.backend.user.UserRepository;
import com.freely.backend.user.UserService;
import com.freely.backend.web.collaborator.dto.CreateCollaboratorForm;
import com.freely.backend.web.user.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Page<UserDTO> listAll(UserAccount company, Pageable pageable){
        return collaboratorRepository.findAll(company, pageable).map(collaborator -> userService.entityToDTO(collaborator));
    }

}
