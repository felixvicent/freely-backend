package com.freely.backend.activity;

import com.freely.backend.client.Client;
import com.freely.backend.project.ActivityStatusEnum;
import com.freely.backend.project.Project;
import com.freely.backend.user.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {


    @Query("SELECT activity FROM Activity activity LEFT JOIN activity.project project WHERE project.company = :company and activity.status = :status")
    List<Activity> findByStatusAndCompany(ActivityStatusEnum status, UserAccount company);

    void deleteByProject(Project project);

    @Query("SELECT COUNT(activity) FROM Activity activity LEFT JOIN activity.project project WHERE project.company = :company AND DATE(activity.createdAt) BETWEEN :periodStart AND :periodEnd")
    Long countByCompany(UserAccount company, LocalDate periodStart, LocalDate periodEnd);
}
