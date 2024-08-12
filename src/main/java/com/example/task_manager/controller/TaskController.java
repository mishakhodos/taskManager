package com.example.task_manager.controller;

import com.example.task_manager.utils.OwnerChecker;
import com.example.task_manager.dto.StatusDto;
import com.example.task_manager.dto.TaskDto;
import com.example.task_manager.enums.Priority;
import com.example.task_manager.enums.Status;
import com.example.task_manager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;
    private OwnerChecker ownerChecker;

    public TaskController(TaskService taskService, OwnerChecker ownerChecher) {
        this.taskService = taskService;
        this.ownerChecker = ownerChecker;
    }

    @GetMapping
    @Operation(summary = "Get a list of tasks with filtering", description = "Returns a tasks filtered by optional parameters")
    public ResponseEntity<Page> getAllTasks(
            @RequestParam(required=false) String title,
            @RequestParam(required=false) String priority,
            @RequestParam(required=false) String status,
            @RequestParam(required=false) Long executorId,
            @RequestParam(required=false) Long ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(taskService.getAllTasks(title, status, priority, executorId, ownerId, PageRequest.of(page, size, Sort.by("title"))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable UUID id) {
        return  ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    public ResponseEntity<Void> saveTask(@Valid @RequestBody TaskDto newTask, Principal principal, UriComponentsBuilder ucb) {
        TaskDto task = taskService.saveTask(newTask, principal);
        URI locationOfNewTask = ucb.path("tasks/{id}").buildAndExpand(task.getId()).toUri();
        return ResponseEntity.created(locationOfNewTask).build();
    }

    @Operation(summary = "Change task's status", description = "Change status of the task")
    @PatchMapping("/{id}")
    @PreAuthorize("@ownerChecker.isExecutor(#id,#principal)")
    public ResponseEntity<Void> updateTaskStatus(@PathVariable UUID id, @Valid @RequestBody StatusDto newStatus, Principal principal) {
        taskService.updateTaskStatus(id, newStatus, principal);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("@ownerChecker.isOwner(#id,#principal)")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id, Principal principal) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("@ownerChecker.isOwner(#id,#principal)")
    public ResponseEntity<Void> updateTask(@PathVariable UUID id, @Valid @RequestBody TaskDto task, Principal principal) {
        taskService.updateTask(id, task, principal);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get available statuses for tasks", description = "Returns a task statuses")
    @GetMapping("/statuses")
    public ResponseEntity< List<Status>> getAvailableStatuses() {
        return ResponseEntity.ok(Arrays.asList(Status.values()));
    }

    @Operation(summary = "Get available values of priority for tasks", description = "Returns a task priorities")
    @GetMapping("/priorities")
    public ResponseEntity< List<Priority>> getAvailablePriorities() {
        return ResponseEntity.ok(Arrays.asList(Priority.values()));
    }
}
