package com.freely.backend.client;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.freely.backend.user.UserAccount;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    @Query("SELECT client FROM Client client WHERE client.company = :company AND (LOWER(firstName) LIKE LOWER(CONCAT('%',:query,'%')) OR LOWER(lastName) LIKE LOWER(CONCAT('%',:query,'%')) OR LOWER(email) LIKE LOWER(CONCAT('%',:query,'%')))")
    Page<Client> findByCompany(UserAccount company, String query, Pageable pageable);

    Optional<Client> findByIdAndCompany(UUID id, UserAccount company);

    @Query(value = "SELECT client.* FROM clients client WHERE client.company_id = :userId AND (LOWER(first_name) LIKE LOWER(CONCAT('%',:query,'%')) OR LOWER(last_name) LIKE LOWER(CONCAT('%',:query,'%'))) LIMIT 5", nativeQuery = true)
    List<Client> findSuggestions(String query, UUID userId);

    Optional<Client> findByDocumentAndCompany(String document, UserAccount company);

    @Query("SELECT COUNT(client) FROM Client client WHERE client.company = :company AND DATE(client.createdAt) BETWEEN :periodStart AND :periodEnd")
    Long countByCompany(UserAccount company, LocalDate periodStart, LocalDate periodEnd);

    @Query(value = "SELECT client.* FROM clients client WHERE client.company_id = :userId ORDER BY client.created_at DESC LIMIT 5", nativeQuery = true)
    List<Client> findLatest(UUID userId);
}
