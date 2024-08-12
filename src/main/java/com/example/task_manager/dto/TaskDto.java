package com.example.task_manager.dto;

import com.example.task_manager.entity.Comment;
import com.example.task_manager.enums.Priority;
import com.example.task_manager.enums.Status;
import com.example.task_manager.validation.ValueOfEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private String id;

    @NotBlank(message = "Title is required")
    @Length(min = 2, max = 500)
    private String title;

    @Length(min = 2, max = 5000)
    private String description;

    @NotNull(message = "Status is required")
    @ValueOfEnum(enumClass = Status.class)
    private String status;

    @ValueOfEnum(enumClass = Priority.class)
    @NotNull(message = "Priority is required")
    private String priority;

    private List<Comment> comments;
    private Long executorId;
    @Schema(name = "filled in on the server, not required", example = "11", required = false)
    private Long ownerId;
    private String createdAt;
    private String updatedAt;
}
