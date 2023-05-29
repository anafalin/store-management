package ru.lazarenko.storemanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import ru.lazarenko.storemanagement.entity.Order;
import ru.lazarenko.storemanagement.service.AppUserService;
import ru.lazarenko.storemanagement.service.OrderService;

@Controller
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
@RequiredArgsConstructor
public class OrdersController {
    private final AppUserService appUserService;
    private final OrderService orderService;

    @GetMapping("/orders/{id}/show")
    public String getPageOrder(@PathVariable Integer id, Model model) {
        System.out.println(id);
        Order order = orderService.getOrderWithRowsById(id);
        model.addAttribute("order", order);
        model.addAttribute("orderRows", order.getOrderRows());

        return "/order/order";
    }

    @ModelAttribute
    public void addUserAttribute(Authentication authentication, Model model) {
        String login = authentication.getName();
        Integer clientId = appUserService.getClientIdByLogin(login);
        String username = authentication.getName();

        model.addAttribute("clientId", clientId);
        model.addAttribute("username", username);
    }
}
