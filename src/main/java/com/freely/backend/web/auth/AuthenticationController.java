package com.freely.backend.web.auth;

import javax.validation.Valid;

import com.freely.backend.authentication.AuthService;
import com.freely.backend.authentication.PasswordEncryption;
import com.freely.backend.authentication.StringHash;
import com.freely.backend.web.auth.dto.ActiveAccountForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freely.backend.authentication.TokenService;
import com.freely.backend.role.Role;
import com.freely.backend.user.UserService;
import com.freely.backend.web.auth.dto.AuthenticateDTO;
import com.freely.backend.web.auth.dto.LoginForm;
import com.freely.backend.web.auth.dto.TokenDTO;
import com.freely.backend.web.user.dto.UserDTO;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @Autowired
    private StringHash stringHash;

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> auth(@RequestBody @Valid LoginForm form) {
        return ResponseEntity.ok(authService.authenticate(form));
    }

    @PostMapping("/active-account")
    public ResponseEntity<?> activeAccount(@RequestBody @Valid ActiveAccountForm activeAccountForm) {
        var email = stringHash.revert(activeAccountForm.getCode());

        userService.activeAccount(email, activeAccountForm.getPassword());

        var loginForm = new LoginForm();
        loginForm.setEmail(email);
        loginForm.setPassword(activeAccountForm.getPassword());

        return ResponseEntity.ok(authService.authenticate(loginForm));
    }

}
