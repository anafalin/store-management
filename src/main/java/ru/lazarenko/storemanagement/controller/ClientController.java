package ru.lazarenko.storemanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.lazarenko.storemanagement.dto.UpdateUserRequest;
import ru.lazarenko.storemanagement.entity.Cart;
import ru.lazarenko.storemanagement.entity.Client;
import ru.lazarenko.storemanagement.entity.Order;
import ru.lazarenko.storemanagement.service.AppUserService;
import ru.lazarenko.storemanagement.service.CartService;
import ru.lazarenko.storemanagement.service.ClientService;
import ru.lazarenko.storemanagement.service.OrderService;

import javax.validation.Valid;
import java.util.List;

@Controller
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
@RequiredArgsConstructor
public class ClientController {
    private final AppUserService appUserService;
    private final ClientService clientService;
    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping("/clients/{clientId}/cart")
    public String getClientCard(@PathVariable Integer clientId, Model model) {
        Cart cart = cartService.getCartByClientId(clientId);
        model.addAttribute("cartRows", cart.getCartRows());
        model.addAttribute("cart", cart);
        model.addAttribute("clientId", clientId);

        return "/client/cart";
    }

    @PostMapping("/clients/{clientId}/add-product-in-cart/{productId}")
    public String addProductInCart(@PathVariable Integer clientId, @PathVariable Integer productId,
                                   @RequestParam Integer count) {
        clientService.addProductInCart(clientId, productId, count);
        return "redirect:/clients/{clientId}/cart";
    }

    @GetMapping("/clients/{clientId}/order/create")
    public String createOrder(@PathVariable Integer clientId) {
        orderService.createOrderByClientId(clientId);
        return "redirect:/home";
    }

    @GetMapping("/clients/{clientId}/orders/change-row/{cartRowId}")
    public String changeCartRow(@PathVariable Integer clientId, @PathVariable Integer cartRowId,
                                @RequestParam Integer newCount) {
        cartService.changeCountRowById(clientId, cartRowId, newCount);
        return "redirect:/clients/{clientId}/cart";
    }

    @GetMapping("/clients/{clientId}/orders/delete-row/{cartRowId}")
    public String deleteCartRow(@PathVariable Integer clientId, @PathVariable Integer cartRowId) {
        cartService.deleteRowById(clientId, cartRowId);
        return "redirect:/clients/{clientId}/cart";
    }

    @GetMapping("/clients/{clientId}/orders")
    public String getClientOrder(@PathVariable Integer clientId, Model model) {
        List<Order> orders = orderService.getAllOrdersByClientId(clientId);
        model.addAttribute("orders", orders);
        return "/order/orders";
    }

    @GetMapping("/clients/{clientId}/orders/{orderId}/show")
    public String getPageOrder(@PathVariable Integer orderId, Model model, @PathVariable String clientId) {
        Order order = orderService.getOrderWithRowsById(orderId);
        model.addAttribute("order", order);
        model.addAttribute("orderRows", order.getOrderRows());
        return "/order/order";
    }

    @GetMapping("/clients/{clientId}/profile")
    public String getProfilePage(Model model, @PathVariable Integer clientId) {
        UpdateUserRequest request = new UpdateUserRequest();
        Client client = clientService.getClientWithUserById(clientId);
        request.setFirstname(client.getFirstname());
        request.setLastname(client.getLastname());
        request.setEmail(client.getUser().getEmail());
        request.setPassword(client.getUser().getPassword());
        request.setPasswordConfirm(client.getUser().getPassword());
        model.addAttribute("request", request);
        return "/client/profile";
    }

    @PostMapping("/clients/{clientId}/update-profile")
    public String updateUser(@ModelAttribute("request") @Valid UpdateUserRequest request, BindingResult errors,
                             Model model, @PathVariable Integer clientId) {
        System.out.println(request);
        if (errors.hasErrors()) {
            return "/client/profile";
        }

        if (request.getPassword() != null
                && !request.getPassword().isEmpty()
                && !request.getPassword().equals(request.getPasswordConfirm())) {
            model.addAttribute("passwordConfirm", "Введенные пароли не совпадают");
            return "/client/profile";
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            model.addAttribute("messageAboutConfirmEmail",
                    "Пожалуйста, подтвердите свой email по ссылке из отправленного Вам сообщения");
        }

        clientService.updateUser(request, clientId);
        model.addAttribute("messageAboutUpdate", "Новые данные сохранены");
        return "/client/profile";

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
