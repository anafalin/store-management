package ru.lazarenko.storemanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.lazarenko.storemanagement.entity.Product;
import ru.lazarenko.storemanagement.repository.ProductRepository;
import ru.lazarenko.storemanagement.service.AppUserService;
import ru.lazarenko.storemanagement.service.ProductService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
@RequestMapping("/products")
public class ProductController {
    private final AppUserService appUserService;
    private final ProductService productService;
    private final ProductRepository productRepository;

    @GetMapping("/all")
    public String getAllProducts(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "4") int size, Model model) {
        List<Product> products;
        Pageable paging = PageRequest.of(page - 1, size);

        Page<Product> pageProducts = productService.getAllProducts(paging);

        products = pageProducts.getContent();

        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageProducts.getNumber() + 1);
        model.addAttribute("totalItems", pageProducts.getTotalElements());
        model.addAttribute("totalPages", pageProducts.getTotalPages());
        model.addAttribute("pageSize", size);

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
