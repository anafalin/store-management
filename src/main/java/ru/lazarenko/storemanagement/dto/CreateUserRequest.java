package ru.lazarenko.storemanagement.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    @NotEmpty(message = "Введите логин")
    @Pattern(regexp = "[A-Za-z0-9]{5,15}",
            message = "Логин должен быть длинной от 5 до 15 символов и состоять из букв и цифр")
    private String login;

    @NotEmpty(message = "Введите пароль")
    @Pattern(regexp = "[A-Za-z0-9._]{5,15}",
            message = "Пароль должен содержать от 5 до 15 символов и только заглавные, строчные буквы или цифры")
    private String password;

    @NotEmpty(message = "Введите подтверждение пароля")
    private String passwordConfirm;

    @Email(message = "Неверный формат email (example@mail.com)")
    @NotEmpty(message = "Введите email")
    private String email;

    @NotEmpty(message = "Введите имя")
    private String firstname;

    @NotEmpty(message = "Введите фамилию")
    private String lastname;


}
