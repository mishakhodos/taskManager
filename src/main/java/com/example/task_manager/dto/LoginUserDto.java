package com.example.task_manager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDto {
    @NotBlank(message = "email cannot be blank")
    @Email
    private String email;

    @Length(min = 4, max = 256)
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
