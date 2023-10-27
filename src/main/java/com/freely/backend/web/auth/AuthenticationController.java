package com.freely.backend.web.auth;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> auth(@RequestBody @Valid LoginForm form) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(form.getEmail(),
                form.getPassword());

        var authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (!authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String token = tokenService.generateToken(authentication);
        var user = userService.loadForAuthentication(form.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new ResponseEntity<>(AuthenticateDTO.builder()
                .token(TokenDTO.builder().type("Bearer").token(token)
                        .roles(user.getRoles().stream().map(Role::getName).toList()).build())
                .user(UserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .active(user.isActive())
                        .role(user.getRoles().iterator().next().getName())
                        .createdAt(user.getCreatedAt())
                        .build())
                .build(),
                HttpStatus.OK);

    }
}
