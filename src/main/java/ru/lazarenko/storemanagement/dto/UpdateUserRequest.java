package ru.lazarenko.storemanagement.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    private String password;

    private String passwordConfirm;

    @NotEmpty(message = "Введите email")
    private String email;

    @NotEmpty(message = "Введите имя")
    private String firstname;

    @NotEmpty(message = "Введите фамилию")
    private String lastname;


}
