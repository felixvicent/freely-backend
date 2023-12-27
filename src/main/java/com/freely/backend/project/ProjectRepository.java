package com.freely.backend.project;

import com.freely.backend.client.Client;
import com.freely.backend.user.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Page<Project> findByCompany(UserAccount company, Pageable pageable);

    Optional<Project> findByIdAndCompany(UUID id, UserAccount company);

    void deleteByClient(Client client);

    List<Project> findByClient(Client client);

    @Query("SELECT SUM(p.value) FROM Project p WHERE p.company = :company AND DATE(p.createdAt) BETWEEN :periodStart AND :periodEnd")
    Double countRevenue(UserAccount company, LocalDate periodStart, LocalDate periodEnd);
}
