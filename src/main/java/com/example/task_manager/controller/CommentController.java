package com.example.task_manager.controller;

import com.example.task_manager.dto.CommentDto;
import com.example.task_manager.entity.Comment;
import com.example.task_manager.service.CommentService;
import com.example.task_manager.utils.CommentAuthorChecker;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks/{taskId}/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService, CommentAuthorChecker commentAuthorChecker) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@PathVariable("taskId") UUID taskId) {
        return ResponseEntity.ok(commentService.getAllCommentsForTask(taskId));
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@PathVariable("taskId") UUID taskId,@Valid @RequestBody CommentDto commentDto, Principal principal) {
        return ResponseEntity.ok(commentService.createComment(taskId, commentDto, principal));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@commentAuthorChecker.isAuthor(#commentId,#principal)")
    public ResponseEntity<Comment> updateComment(@PathVariable("taskId") UUID taskId, @PathVariable("id") UUID commentId,@Valid @RequestBody  CommentDto comment, Principal principal) {
        return ResponseEntity.ok(commentService.updateComment(taskId, commentId, comment, principal));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@commentAuthorChecker.isAuthor(#commentId,#principal)")
    public ResponseEntity<Void> deleteComment(@PathVariable("taskId") UUID taskId, @PathVariable("id") UUID commentId, Principal principal) {
        commentService.deleteComment(taskId, commentId, principal);
        return ResponseEntity.noContent().build();
    }
}
