package ru.lazarenko.storemanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.lazarenko.storemanagement.entity.Product;
import ru.lazarenko.storemanagement.service.AppUserService;
import ru.lazarenko.storemanagement.service.ProductService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
@RequestMapping("/products")
public class ProductController {
    private final AppUserService appUserService;
    private final ProductService productService;

    @GetMapping("/all")
    public String getAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);

        return "/product/products";
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
