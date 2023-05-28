package ru.lazarenko.storemanagement.controller;

import lombok.RequiredArgsConstructor;
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

    // clients
    @GetMapping("/clients")
    public String getPageAllClients(Model model){
        List<Client> clients = clientService.getAllClients();
        model.addAttribute("clients", clients);
        return "/client/clients";
    }

    @GetMapping("/clients/{clientId}/orders")
    public String getPageAllClientOrder(Model model, @PathVariable Integer clientId){
        List<Order> orders = orderService.getAllOrdersByClientId(clientId);
        model.addAttribute("orders", orders);
        model.addAttribute("clientId", clientId);
        return "/order/orders";
    }

    // orders
    @GetMapping("/orders")
    public String getPageNewOrders(Model model, @RequestParam(name = "status", required = false) String status) {
        if(status == null || status.isEmpty()) {
            List<Order> allOrders = orderService.getAllOrders();
            model.addAttribute("orders", allOrders);
            return "/order/orders";
        }

        List<Order> orders = orderService.getOrdersByStatus(status);
        model.addAttribute("orders", orders);
        model.addAttribute("status", status);
        return "/order/orders";
    }

    @GetMapping("/orders/{id}/{newStatus}")
    public String acceptOrder(@PathVariable Integer id, @PathVariable(name = "newStatus") String status) {
        orderService.updateStatusById(status, id);
        return "redirect:/orders/all";
    }

    // products
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
}
