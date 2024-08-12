package com.example.task_manager;

import com.example.task_manager.dto.LoginUserDto;
import com.example.task_manager.dto.RegisterUserDto;
import com.example.task_manager.entity.Task;
import com.example.task_manager.entity.User;
import com.example.task_manager.enums.Priority;
import com.example.task_manager.enums.Status;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.service.AuthenticationService;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment =
        SpringBootTest.WebEnvironment.RANDOM_PORT)

public class UserControllerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    @DirtiesContext
    void shouldRegisterANewUser() {
        RegisterUserDto user = new RegisterUserDto();
        user.setEmail("new@example.com");
        user.setPassword("testpassword");
        user.setFullName("new User");
        ResponseEntity<Void> createResponse = restTemplate.postForEntity("/auth/signup", user, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldLoginUser() {
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("test@test.com");
        loginUserDto.setPassword("pass_252");
        ResponseEntity<Void> response = restTemplate.postForEntity("/auth/login", loginUserDto, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @BeforeAll
    static void init(@Autowired AuthenticationService authService) {
        authService.signup(new RegisterUserDto("login@test.com", "testpassword", "Test User"));
    }
}
