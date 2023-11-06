package com.freely.backend.client;

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
    @Query("SELECT client FROM Client client WHERE client.user = :user AND (LOWER(firstName) LIKE LOWER(CONCAT('%',:query,'%')) OR LOWER(lastName) LIKE LOWER(CONCAT('%',:query,'%')) OR LOWER(email) LIKE LOWER(CONCAT('%',:query,'%')))")
    Page<Client> findByUser(UserAccount user, String query, Pageable pageable);

    Optional<Client> findByIdAndUser(UUID id, UserAccount user);

    @Query(value = "SELECT client.* FROM clients client WHERE client.user_id = :userId AND (LOWER(first_name) LIKE LOWER(CONCAT('%',:query,'%')) OR LOWER(last_name) LIKE LOWER(CONCAT('%',:query,'%'))) LIMIT 5", nativeQuery = true)
    List<Client> findSuggestions(String query, UUID userId);

    Optional<Client> findByDocumentAndUser(String document, UserAccount user);

    Long countByUser(UserAccount user);

    @Query(value = "SELECT client.* FROM clients client WHERE client.user_id = :userId ORDER BY client.created_at DESC LIMIT 5", nativeQuery = true)
    List<Client> findLatest(UUID userId);

    void deleteByUser(UserAccount user);

    List<Client> findByUser(UserAccount user);

}
