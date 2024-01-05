package com.freely.backend.user.collaborator;

import com.freely.backend.user.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CollaboratorRepository extends JpaRepository<UserAccount, UUID> {

    @Query("SELECT collaborator FROM UserAccount collaborator INNER JOIN collaborator.roles roles WHERE 'USER' IN (roles.name) AND collaborator.company = :company")
    Page<UserAccount> findAll(UserAccount company, Pageable pageable);
}
