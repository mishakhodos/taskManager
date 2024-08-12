package com.example.task_manager;

import com.example.task_manager.dto.RegisterUserDto;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.User;
import com.example.task_manager.enums.Priority;
import com.example.task_manager.enums.Status;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;
import com.example.task_manager.service.AuthenticationService;
import com.example.task_manager.service.TaskService;
import com.example.task_manager.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TaskManagerApplicationTests {

	@Test
	void contextLoads() {
	}

}
