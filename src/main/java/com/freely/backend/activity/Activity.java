package com.freely.backend.activity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.freely.backend.comment.Comment;
import com.freely.backend.project.Project;
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
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityStatusEnum status;
    
    @Column(nullable = false)
    private LocalDateTime estimatedDate;

    private LocalDateTime finishedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    private UserAccount company;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "responsible_id")
    private UserAccount responsible;

    @OrderBy("created_at DESC")
    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void setCreatedAt() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void setUpdatedAt() {
        updatedAt = LocalDateTime.now();
    }
}
