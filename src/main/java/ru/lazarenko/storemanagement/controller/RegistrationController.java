package ru.lazarenko.storemanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.lazarenko.storemanagement.dto.CreateUserRequest;
import ru.lazarenko.storemanagement.service.AppUserService;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final AppUserService appUserService;

    @GetMapping("/reg")
    public String getPageRegistration(Model model) {
        model.addAttribute("request", new CreateUserRequest());
        return "/enter/registration";
    }

    @PostMapping("/reg")
    public String createNewClient(@ModelAttribute("request") @Valid CreateUserRequest request, BindingResult errors, Model model) {

        if (errors.hasErrors()) {
            return "/enter/registration";
        }

        if (appUserService.checkIsUniqueLogin(request.getLogin())) {
            model.addAttribute("usernameError", "Пользователь с таким логином уже существует");
            return "/enter/registration";
        }

        if (request.getPassword() != null && !request.getPassword().equals(request.getPasswordConfirm())) {
            model.addAttribute("passwordConfirm", "Введенные пароли не совпадают");
            return "/enter/registration";
        }

        appUserService.createUser(request);

        model.addAttribute("messageAboutConfirmEmail", "Пожалуйста, подтвердите свой email по ссылке из отправленного Вам сообщения");
        return "/enter/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = appUserService.activeUser(code);

        if (isActivated) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "Пользователь успешно активирован!");
        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Некорректный код активации!");
        }
        return "/enter/login";
    }
}
