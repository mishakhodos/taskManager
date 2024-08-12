package com.example.task_manager.service;

import com.example.task_manager.dto.TaskDto;
import com.example.task_manager.dto.UserDto;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.User;
import com.example.task_manager.exception.NotFoundException;
import com.example.task_manager.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByUsername(String username) {
        return userRepository.findByEmail(username).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }


}
