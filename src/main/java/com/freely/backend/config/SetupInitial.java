package com.freely.backend.config;

import com.freely.backend.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class SetupInitial implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createDefaultAdminIfNotExists();
    }

    private void createDefaultAdminIfNotExists() {
        userService.createAdmin("admin@freely.com", "FReeLY@2023", "Admin Freely");
    }
}
