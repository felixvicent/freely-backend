package com.freely.backend.user.collaborator;

import com.freely.backend.client.Client;
import com.freely.backend.user.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CollaboratorRepository extends JpaRepository<UserAccount, UUID> {
    @Query(value = "SELECT collaborator.* FROM users collaborator " +
            "WHERE collaborator.company_id = :userId " +
            "AND LOWER(collaborator.name) LIKE LOWER(CONCAT('%',:query,'%')) LIMIT 5", nativeQuery = true)
    List<UserAccount> findSuggestions(String query, UUID userId);

    @Query("SELECT collaborator FROM UserAccount collaborator " +
            "INNER JOIN collaborator.roles roles " +
            "WHERE collaborator.company = :company " +
            "AND 'USER' IN (roles.name) " +
            "AND (collaborator.id IN :collaboratorIds " +
            "OR :filterById = false)")
    Page<UserAccount> findByCompany(UserAccount company, List<UUID> collaboratorIds, boolean filterById, Pageable pageable);

    @Query("SELECT collaborator FROM UserAccount collaborator " +
            "WHERE collaborator.company = :company")
    List<UserAccount> listAllByCompany(UserAccount company);
}
