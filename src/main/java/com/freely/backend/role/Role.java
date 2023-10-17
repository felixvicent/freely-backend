package com.freely.backend.role;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {

    public static final UUID ADMIN_ROLE_ID = UUID.fromString("04537612-70aa-4439-b679-1b836ef8869d");
    public static final UUID USER_ROLE_ID = UUID.fromString("7e5600f1-62c1-4694-8441-e22e50692f43");
    public static final UUID COMPANY_ROLE_ID = UUID.fromString("2ea5261d-fc99-4146-ac17-3c24a5ed93b9");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    public Role(UUID id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return this.name;
    }
}
