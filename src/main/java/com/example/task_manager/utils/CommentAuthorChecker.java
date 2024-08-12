package com.example.task_manager.utils;

import com.example.task_manager.entity.Comment;
import com.example.task_manager.entity.User;
import com.example.task_manager.exception.NotFoundException;
import com.example.task_manager.repository.CommentRepository;
import com.example.task_manager.service.UserService;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.UUID;

@Component
public class CommentAuthorChecker {
    private final CommentRepository commentRepository;
    private final UserService userService;

    public CommentAuthorChecker(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;

        this.userService = userService;
    }

    public boolean isAuthor(UUID CommentId, Principal principal) {

        Comment comment = commentRepository.findById(CommentId).orElseThrow(() -> new NotFoundException("Not found comment"));
        Long authorId = comment.getAuthor().getId();
        User authUser = userService.findUserByUsername(principal.getName());
        return authorId != null && authorId.equals(authUser.getId());
    }

}
