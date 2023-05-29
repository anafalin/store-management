package ru.lazarenko.storemanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.lazarenko.storemanagement.service.AppUserService;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final AppUserService appUserService;

    @GetMapping("/")
    public String getStartPage() {
        return "index";
    }

    @GetMapping("/login")
    public String getPageLogin() {
        return "/enter/login";
    }

    @GetMapping("/home")
    public String getHomePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        Integer clientId = appUserService.getClientIdByLogin(login);
        model.addAttribute("clientId", clientId);

        return "index";
    }

    @GetMapping("/login/error")
    public String getPageErrorLogin(Model model) {
        model.addAttribute("messageError",
                "Пользователь не найден или неверно введены данные для входа");

        return "/enter/login";
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);

        return "redirect:/";
    }
}
