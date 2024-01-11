package com.freely.backend.activity;

import com.freely.backend.user.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {


    @Query("SELECT activity FROM Activity activity " +
            "WHERE activity.company = :company " +
            "AND activity.status = :status "  +
            "AND (activity.responsible.id IN :collaboratorsIds OR :filterByCollaboratorIds = false)")
    List<Activity> findByStatusAndCompany(ActivityStatusEnum status, UserAccount company, List<UUID> collaboratorsIds, boolean filterByCollaboratorIds);

    @Query("SELECT activity FROM Activity activity " +
            "LEFT JOIN activity.project project " +
            "WHERE project.id = :projectId " +
            "AND activity.company = :company " +
            "AND activity.status = :status " +
            "AND (activity.responsible.id IN :collaboratorsIds OR :filterByCollaboratorIds = false)")
    List<Activity> findByStatusAndCompanyAndProject(ActivityStatusEnum status, UUID projectId, UserAccount company, List<UUID> collaboratorsIds, boolean filterByCollaboratorIds);

    @Query("SELECT COUNT(activity) FROM Activity activity " +
            "WHERE activity.company = :company " +
            "AND DATE(activity.createdAt) BETWEEN :periodStart AND :periodEnd")
    Long countByCompany(UserAccount company, LocalDate periodStart, LocalDate periodEnd);
}
