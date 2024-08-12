package com.example.task_manager.service;

import com.example.task_manager.dto.CommentDto;
import com.example.task_manager.entity.Comment;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.User;
import com.example.task_manager.exception.NotFoundException;
import com.example.task_manager.repository.CommentRepository;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private TaskRepository taskRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }
    @Transactional
    public Comment createComment(UUID taskId, CommentDto comment, Principal principal) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found"));

        Comment newComment = new Comment();
        newComment.setText(comment.getText());
        User author = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new NotFoundException("User not found"));
        newComment.setAuthor(author);
        newComment.setTask(task);
        return commentRepository.save(newComment);
    }

    public Comment updateComment(UUID taskId, UUID commentId, CommentDto comment, Principal principal) {
        Comment existingComment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        existingComment.setText(comment.getText());
        return commentRepository.save(existingComment);
    }

    public List<Comment> getAllCommentsForTask(UUID taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found"));
        return task.getComments();
    }

    public void deleteComment(UUID taskId, UUID commentId, Principal principal) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found")).getComments().remove(comment);
        commentRepository.delete(comment);
    }
}
