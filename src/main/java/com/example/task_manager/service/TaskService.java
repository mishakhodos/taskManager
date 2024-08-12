package com.example.task_manager.service;

import com.example.task_manager.dto.StatusDto;
import com.example.task_manager.dto.TaskDto;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.User;
import com.example.task_manager.enums.Status;
import com.example.task_manager.exception.NotFoundException;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;
import com.example.task_manager.specification.TaskSpecification;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.UUID;


@Service
public class TaskService {

    private ModelMapper modelMapper;
    private TaskRepository taskRepository;
    private UserRepository userRepository;

    public TaskService(ModelMapper modelMapper, TaskRepository taskRepository, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Page<TaskDto> getAllTasks(String title, String status, String priority, Long executorId, Long ownerId, Pageable pageable) {


        Specification<Task> spec = Specification.where(null);
        if (title!= null) {
            spec = spec.and(TaskSpecification.hasTitle(title));
        }
        if (status!= null) {
            spec = spec.and(TaskSpecification.hasStatus(status));
        }
        if (priority!= null) {
            spec = spec.and(TaskSpecification.hasPriority(priority));
        }

        if (executorId!= null) {
            spec = spec.and(TaskSpecification.hasExecutor(executorId.toString()));
        }
        if (ownerId!= null) {
            spec = spec.and(TaskSpecification.hasOwner(ownerId));
        }

        Page<Task> tasks = taskRepository.findAll(spec, pageable);

        return tasks.map(this::convertEntityToDto);
    }
    public TaskDto getTaskById(UUID id) {
        Task task = findTaskById(id);
        return convertEntityToDto( task );
    }

    @Transactional
    public TaskDto saveTask(TaskDto taskDto, Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));
        Task task = convertDtoToEntity(taskDto);
        task.setOwner(currentUser);
        task.setUpdatedBy(currentUser);
        if(taskDto.getExecutorId() != null) {
            task.setExecutor(userRepository.findById(taskDto.getExecutorId())
                    .orElseThrow(() -> new NotFoundException("User not found")));
        }
        return convertEntityToDto(taskRepository.save(task));
    }

    public void updateTask(UUID id, TaskDto taskDto, Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Task not found");
        }
        Task task = findTaskById(id);
        Task updatedTask = convertDtoToEntity(taskDto);
        updatedTask.setOwner(task.getOwner());
        updatedTask.setUpdatedBy(currentUser);
        updatedTask.setId(id);
        taskRepository.save(updatedTask);
    }

    public void updateTaskStatus(UUID id, StatusDto status, Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException("User not found"));
        Task task = findTaskById(id);
        task.setUpdatedBy(currentUser);
        task.setStatus(Status.valueOf(status.getStatus()));
        taskRepository.save(task);
    }

    public void deleteTask(UUID id) {
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Task not found");
        }
        taskRepository.deleteById(id);
    }
    private Task findTaskById(UUID id) {
        return taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));
    }

    private TaskDto convertEntityToDto(Task task) {
        return modelMapper.map(task, TaskDto.class);
    }

    private Task convertDtoToEntity(TaskDto taskDto) {
        return modelMapper.map(taskDto, Task.class);
    }

}
