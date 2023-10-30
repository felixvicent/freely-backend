package com.freely.backend.authentication;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class StringHash {
    public String hash(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    public String revert(String code) {
        byte[] bytes = Base64.getDecoder().decode(code);

        return new String(bytes, StandardCharsets.UTF_8);

    }
}
