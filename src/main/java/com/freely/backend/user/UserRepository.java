package com.freely.backend.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.freely.backend.client.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, UUID> {
    Optional<UserAccount> findByEmail(String email);

    @Query("SELECT user FROM UserAccount user INNER JOIN user.roles roles WHERE 'COMPANY' IN (roles.name)")
    Page<UserAccount> findAll(Pageable pageable);

    @Query("SELECT user FROM UserAccount user INNER JOIN user.roles roles WHERE 'COMPANY' IN (roles.name) AND user.id IN :usersIds")
    Page<UserAccount> findAllByIds(List<UUID> usersIds, Pageable pageable);

    @Query(value = "SELECT us.* FROM users us " +
            "INNER JOIN users_roles user_role ON (user_role.user_account_id = us.id) " +
            "INNER JOIN roles role ON (role.id = user_role.roles_id)" +
            " WHERE role.name = 'COMPANY' AND (LOWER(us.name) LIKE LOWER(CONCAT('%',:query,'%')) OR LOWER(us.email) LIKE LOWER(CONCAT('%',:query,'%'))) LIMIT 5", nativeQuery = true)
    List<UserAccount> findSuggestions(String query);

    Optional<UserAccount> findByDocument(String document);
}
