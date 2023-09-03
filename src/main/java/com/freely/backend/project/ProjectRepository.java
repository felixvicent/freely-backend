package com.freely.backend.project;

import com.freely.backend.user.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Page<Project> findByUser(UserAccount user, Pageable pageable);

    Optional<Project> findByIdAndUser(UUID id, UserAccount user);
}
