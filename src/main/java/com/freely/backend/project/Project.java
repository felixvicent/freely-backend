package com.freely.backend.project;

import com.freely.backend.activity.Activity;
import com.freely.backend.client.Client;
import com.freely.backend.user.UserAccount;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    private UserAccount company;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false)
    private double value;

    @Column(name = "estimated_date", nullable = false)
    private LocalDateTime estimatedDate;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OrderBy("created_at")
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<Activity> activities = new HashSet<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectStatusEnum status = ProjectStatusEnum.PENDING;

    @PrePersist
    public void setCreatedAt() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void setUpdatedAt() {
        updatedAt = LocalDateTime.now();
    }
}
