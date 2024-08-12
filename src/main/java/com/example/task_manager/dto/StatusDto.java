package com.example.task_manager.dto;

import com.example.task_manager.enums.Status;
import com.example.task_manager.validation.ValueOfEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusDto {
    @ValueOfEnum(enumClass = Status.class)
    private String status;
}
