package com.freely.backend.authentication;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.freely.backend.user.UserService;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final UserService userService;

    public TokenAuthenticationFilter(TokenService tokenService, UserService userService) {
        super();
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String tokenFromHeader = getTokenFromHeader(request);
        boolean tokenValid = tokenService.isTokenValid(tokenFromHeader);

        if (tokenValid) {
            this.authenticate(tokenFromHeader);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        return token.substring(7);
    }

    private void authenticate(String tokenFromHeader) {
        UUID id = tokenService.getTokenId(tokenFromHeader);

        var optionalUser = userService.loadForAuthenticationById(id);

        if (optionalUser.isPresent()) {
            var user = optionalUser.get();

            var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null,
                    user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }
}
