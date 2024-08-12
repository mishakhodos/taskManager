package com.example.task_manager;

import com.example.task_manager.dto.TaskDto;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.User;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.service.AuthenticationService;
import com.example.task_manager.service.JwtService;
import com.example.task_manager.service.UserService;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;


import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment =
        SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    JwtService jwtService;

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationService authService;


    @Test
    void shouldReturnAllTasks() {
        User u1 = userService.findUserByUsername("test@test.com");
        String token = jwtService.generateToken(u1);
        assertNotNull(token);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        ResponseEntity<String> response = restTemplate.exchange("/tasks", HttpMethod.GET, new HttpEntity<Void>(headers), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Number totalElements = JsonPath.parse(response.getBody()).read("$.totalElements");
        assertThat(totalElements).isEqualTo(3);

        String firstTaskTitle = JsonPath.parse(response.getBody()).read("$.content[0].title");
        assertThat(firstTaskTitle).isEqualTo("task1");
    }

    @Test
    @DirtiesContext
    void shouldCreateNewTask() {

        User u1 = userService.findUserByUsername("test@test.com");
        String token = jwtService.generateToken(u1);
        assertNotNull(token);
        HttpHeaders headers = new HttpHeaders();
        TaskDto taskDto = new TaskDto();

        taskDto.setTitle("testTaskTitle");
        taskDto.setStatus("PENDING");
        taskDto.setPriority("LOW");
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<TaskDto> requestEntity = new HttpEntity<>(taskDto, headers);

        ResponseEntity<String> response = restTemplate.exchange("/tasks", HttpMethod.POST, requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        URI locationOfNewTask = response.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate.exchange(locationOfNewTask, HttpMethod.GET, new HttpEntity<Void>(headers), String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        String id = documentContext.read("$.id");
        String title = documentContext.read("$.title");
        assertThat(id).isNotNull();
        assertThat(title).isEqualTo("testTaskTitle");
    }

    @Test
    @DirtiesContext
    void shouldUpdateNewTask() {

        User u1 = userService.findUserByUsername("test@test.com");
        String token = jwtService.generateToken(u1);
        String taskId = "0e543417-02b0-4681-8516-5955b2150543";
        assertNotNull(token);
        HttpHeaders headers = new HttpHeaders();
        TaskDto taskDto = new TaskDto();

        taskDto.setTitle("updatedTaskTitle");
        taskDto.setStatus("PENDING");
        taskDto.setPriority("LOW");
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<TaskDto> requestEntity = new HttpEntity<>(taskDto, headers);

        ResponseEntity<String> response = restTemplate.exchange("/tasks" + "/" + taskId, HttpMethod.PUT, requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Task updatedTask = taskRepository.findById(UUID.fromString(taskId)).get();
        assertThat(updatedTask.getTitle()).isEqualTo("updatedTaskTitle");

    }

    @Test
    @DirtiesContext
    void shouldNotUpdateNewTaskByNotOw() {

        User u1 = userService.findUserByUsername("test@test.com");
        String token = jwtService.generateToken(u1);
        String taskId = "0e543417-02b0-4681-8516-2955b2150541";
        assertNotNull(token);
        HttpHeaders headers = new HttpHeaders();
        TaskDto taskDto = new TaskDto();

        taskDto.setTitle("updatedTaskTitle");
        taskDto.setStatus("PENDING");
        taskDto.setPriority("LOW");
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<TaskDto> requestEntity = new HttpEntity<>(taskDto, headers);

        ResponseEntity<String> response = restTemplate.exchange("/tasks" + "/" + taskId, HttpMethod.PUT, requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);


    }

    @Test
    @DirtiesContext
    void shouldDeleteTask() {

        User u1 = userService.findUserByUsername("test@test.com");
        String token = jwtService.generateToken(u1);
        String taskId = "0e543417-02b0-4681-8516-5955b2150543";
        assertNotNull(token);
        HttpHeaders headers = new HttpHeaders();
        TaskDto taskDto = new TaskDto();

        taskDto.setTitle("updatedTaskTitle");
        taskDto.setStatus("PENDING");
        taskDto.setPriority("LOW");
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<TaskDto> requestEntity = new HttpEntity<>(taskDto, headers);

        ResponseEntity<String> response = restTemplate.exchange("/tasks" + "/" + taskId, HttpMethod.DELETE, requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        boolean taskExists = taskRepository.existsById(UUID.fromString(taskId));
        assertThat(taskExists).isEqualTo(false);

    }
}
