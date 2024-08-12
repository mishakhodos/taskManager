package com.example.task_manager;

import com.example.task_manager.dto.CommentDto;
import com.example.task_manager.dto.TaskDto;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment =
        SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentControllerTests {

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
    @DirtiesContext
    void shouldCreateNewComment() {

        User u1 = userService.findUserByUsername("test@test.com");
        String token = jwtService.generateToken(u1);
        assertNotNull(token);
        String taskId = "0e543417-02b0-4681-8516-5955b2150543";
        HttpHeaders headers = new HttpHeaders();
        CommentDto commentDto = new CommentDto();
        commentDto.setText("test comment");
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<CommentDto> requestEntity = new HttpEntity<>(commentDto, headers);

        ResponseEntity<String> response = restTemplate.exchange("/tasks/" + taskId + "/comments", HttpMethod.POST, requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        String id = documentContext.read("$.id");
        String text = documentContext.read("$.text");
        assertThat(id).isNotNull();
        assertThat(text).isEqualTo("test comment");
    }
}
