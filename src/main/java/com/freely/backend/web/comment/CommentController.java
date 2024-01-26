package com.freely.backend.web.comment;

import com.freely.backend.comment.CommentService;
import com.freely.backend.user.UserAccount;
import com.freely.backend.web.comment.dto.CommentDTO;
import com.freely.backend.web.comment.dto.CreateCommentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<?> store(@AuthenticationPrincipal UserAccount userAccount,
                                   @RequestBody @Valid CreateCommentForm createCommentForm) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(userAccount, createCommentForm));
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable UUID activityId){
        return ResponseEntity.ok(commentService.getByActivity(activityId));
    }
}
