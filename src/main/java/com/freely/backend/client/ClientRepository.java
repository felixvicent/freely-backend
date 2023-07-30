package com.freely.backend.client;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import com.freely.backend.user.UserAccount;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
  List<Client> findByUser(UserAccount user);

  Optional<Client> findByIdAndUser(UUID id, UserAccount user);
}
