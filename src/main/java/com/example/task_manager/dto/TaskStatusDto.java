package com.example.task_manager.dto;

import com.example.task_manager.enums.Status;
import com.example.task_manager.validation.ValueOfEnum;

public class TaskStatusDto {
    @ValueOfEnum(enumClass = Status.class)
    private String status;
}
