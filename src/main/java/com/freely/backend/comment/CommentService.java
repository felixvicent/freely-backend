package com.freely.backend.comment;

import com.freely.backend.activity.Activity;
import com.freely.backend.activity.ActivityService;
import com.freely.backend.user.UserAccount;
import com.freely.backend.web.comment.dto.CommentDTO;
import com.freely.backend.web.comment.dto.CreateCommentForm;
import com.freely.backend.web.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ActivityService activityService;

    public CommentDTO save(UserAccount userAccount, CreateCommentForm createCommentForm) {
        Activity activity = activityService.findById(createCommentForm.getActivityId());

        Comment comment = new Comment();

        comment.setComment(createCommentForm.getComment());
        comment.setActivity(activity);
        comment.setUser(userAccount);
        comment.setCreatedAt(LocalDateTime.now());

        return entityToDTO(commentRepository.save(comment));
    }

    public List<CommentDTO> getByActivity(UUID activityId) {
        Activity activity = activityService.findById(activityId);

        return commentRepository.findByActivityOrderByCreatedAtDesc(activity).stream().map(this::entityToDTO).toList();
    }

    private CommentDTO entityToDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .user(UserDTO.builder()
                        .id(comment.getUser().getId())
                        .name(comment.getUser().getName())
                        .email(comment.getUser().getEmail())
                        .role(comment.getUser().getRoles().iterator().next().getName())
                        .office(comment.getUser().getOffice())
                        .active(comment.getUser().isActive())
                        .document(comment.getUser().getDocument())
                        .telephone(comment.getUser().getTelephone())
                        .createdAt(comment.getUser().getCreatedAt())
                        .build()
                )
                .build();
    }
}
