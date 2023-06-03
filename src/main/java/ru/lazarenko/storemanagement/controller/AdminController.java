package ru.lazarenko.storemanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.lazarenko.storemanagement.entity.Client;
import ru.lazarenko.storemanagement.entity.Order;
import ru.lazarenko.storemanagement.entity.Product;
import ru.lazarenko.storemanagement.service.ClientService;
import ru.lazarenko.storemanagement.service.OrderService;
import ru.lazarenko.storemanagement.service.ProductService;
import ru.lazarenko.storemanagement.util.FileUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ClientService clientService;
    private final OrderService orderService;
    private final ProductService productService;

    @GetMapping("/clients")
    public String getPageAllClients(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int size, Model model) {
        List<Client> clients;
        Pageable paging = PageRequest.of(page - 1, size);

        Page<Client> pageClients = clientService.getAllClients(paging);

        clients = pageClients.getContent();

        model.addAttribute("clients", clients);
        model.addAttribute("currentPage", pageClients.getNumber() + 1);
        model.addAttribute("totalItems", pageClients.getTotalElements());
        model.addAttribute("totalPages", pageClients.getTotalPages());
        model.addAttribute("pageSize", size);
        return "/client/clients";
    }

    @GetMapping("/clients/{clientId}/orders")
    public String getPageAllClientOrder(Model model, @PathVariable Integer clientId) {
        List<Order> orders = orderService.getAllOrdersByClientId(clientId);
        model.addAttribute("orders", orders);
        model.addAttribute("clientId", clientId);

        return "/order/orders";
    }

    @GetMapping("/orders")
    public String getPageNewOrders(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(name = "status", required = false) String status,
                                   Model model) {
        List<Order> orders;
        Pageable paging = PageRequest.of(page - 1, size);

        if (status == null || status.isEmpty()) {
            Page<Order> pageOrders = orderService.getAllOrders(paging);
            orders = pageOrders.getContent();

            model.addAttribute("orders", orders);
            model.addAttribute("currentPage", pageOrders.getNumber() + 1);
            model.addAttribute("totalItems", pageOrders.getTotalElements());
            model.addAttribute("totalPages", pageOrders.getTotalPages());
            model.addAttribute("pageSize", size);

            return "/order/orders";
        }
        Page<Order> pageOrders = orderService.getOrdersByStatus(status, paging);
        orders = pageOrders.getContent();

        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", pageOrders.getNumber() + 1);
        model.addAttribute("totalItems", pageOrders.getTotalElements());
        model.addAttribute("totalPages", pageOrders.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("status", status);

        return "/order/orders";
    }

    @GetMapping("/orders/{id}/{newStatus}")
    public String acceptOrder(@PathVariable Integer id, @PathVariable(name = "newStatus") String status, Model model) {
        orderService.updateStatusById(status, id);

        return "redirect:/admin/orders";
    }

    @GetMapping("/products/new")
    public String getPageForCreateProduct(Model model) {
        model.addAttribute("product", new Product());

        return "/product/create-form";
    }

    @PostMapping("/products/new/add")
    public String addProduct(@ModelAttribute @Valid Product product, Errors errors, Model model,
                             @RequestParam("file") MultipartFile file) throws IOException {
        if (errors.hasErrors()) {
            return "/product/create-form";
        }

        FileUtils.saveFile(product, file);

        productService.create(product);

        model.addAttribute("infoMessage", "Продукт успешно сохранен");
        model.addAttribute("product", new Product());

        return "/product/create-form";
    }

    @PostMapping("/products/{productId}/change-count")
    public String addProduct(@PathVariable Integer productId, @RequestParam Integer count) {
        productService.addCountProductById(productId, count);
        return "redirect:/products/all";
    }
}
