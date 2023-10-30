package com.freely.backend.authentication;

import com.freely.backend.exceptions.UnauthorizedException;
import com.freely.backend.role.Role;
import com.freely.backend.user.UserService;
import com.freely.backend.web.auth.dto.AuthenticateDTO;
import com.freely.backend.web.auth.dto.LoginForm;
import com.freely.backend.web.auth.dto.TokenDTO;
import com.freely.backend.web.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;


    public AuthenticateDTO authenticate(LoginForm loginForm) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginForm.getEmail(),
                loginForm.getPassword());

        var authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (!authentication.isAuthenticated()) {
            throw new UnauthorizedException("Não tem autorização");
        }

        String token = tokenService.generateToken(authentication);
        var user = userService.loadForAuthentication(loginForm.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return AuthenticateDTO.builder()
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
                .build();
    }
}
