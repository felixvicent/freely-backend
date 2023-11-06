package com.freely.backend.activity;

import com.freely.backend.client.Client;
import com.freely.backend.project.ActivityStatusEnum;
import com.freely.backend.project.Project;
import com.freely.backend.user.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {

    @Query("SELECT COUNT(*) FROM Activity activity LEFT JOIN activity.project project WHERE project.user = :user")
    long countByUser(UserAccount user);

    @Query(value = "SELECT activity.* FROM activities activity LEFT JOIN projects project ON (project.id = activity.project_id) WHERE project.user_id = :userId ORDER BY activity.created_at DESC LIMIT 5", nativeQuery = true)
    List<Activity> findLatest(UUID userId);

    @Query("SELECT activity FROM Activity activity LEFT JOIN activity.project project WHERE project.user = :user and activity.status = :status")
    List<Activity> findByStatusAndUser(ActivityStatusEnum status, UserAccount user);

    void deleteByProject(Project project);
}
