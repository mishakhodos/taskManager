package com.example.task_manager.utils;

import com.example.task_manager.dto.TaskDto;
import com.example.task_manager.entity.User;
import com.example.task_manager.service.TaskService;
import com.example.task_manager.service.UserService;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.UUID;

@Component
public class OwnerChecker {
    private final TaskService taskService;
    private final UserService userService;

    public OwnerChecker(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    public boolean isOwner(UUID taskId, Principal principal) {

        TaskDto taskDto = taskService.getTaskById(taskId);
        Long ownerId = taskDto.getOwnerId();
        User authUser = userService.findUserByUsername(principal.getName());
        return ownerId != null && ownerId.equals(authUser.getId());
    }

    public boolean isExecutor(UUID taskId, Principal principal) {

        User authUser = userService.findUserByUsername(principal.getName());
        TaskDto taskDto = taskService.getTaskById(taskId);
        Long ownerId = taskDto.getOwnerId();
        if(ownerId.equals(authUser.getId())) {
            return true;
        }
        Long executorId = taskDto.getExecutorId();
        if(executorId != null) {
            return executorId.equals(authUser.getId());
        }
        return false;
    }
}
