package com.freely.backend.project;

import com.freely.backend.client.Client;
import com.freely.backend.user.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Page<Project> findByUser(UserAccount user, Pageable pageable);

    Optional<Project> findByIdAndUser(UUID id, UserAccount user);

    Long countByUser(UserAccount user);

    @Query(value = "SELECT project.* FROM projects project WHERE project.user_id = :userId ORDER BY project.created_at DESC LIMIT 5", nativeQuery = true)
    List<Project> findLatest(UUID userId);

    void deleteByClient(Client client);

    List<Project> findByClient(Client client);
}
