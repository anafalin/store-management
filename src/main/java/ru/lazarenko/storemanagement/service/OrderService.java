package ru.lazarenko.storemanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lazarenko.storemanagement.entity.*;
import ru.lazarenko.storemanagement.model.OrderStatus;
import ru.lazarenko.storemanagement.repository.OrderRepository;
import ru.lazarenko.storemanagement.util.StatusUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ClientService clientService;
    private final CartRowService cartRowService;
    private final ProductService productService;

    @Transactional
    public void createOrderByClientId(Integer clientId) {
        Cart cart = cartService.getCartByClientId(clientId);
        Client client = clientService.getClientWithOrdersByClientId(clientId);

        Order order = new Order();

        for (CartRow cartRow : cart.getCartRows()) {
            Product productInRow = cartRow.getProduct();
            OrderRow orderRow = OrderRow.builder()
                    .product(productInRow)
                    .count(cartRow.getCount())
                    .amount(cartRow.getAmount())
                    .order(order)
                    .build();
            order.addOrderRow(orderRow);
            productInRow.setCount(productInRow.getCount() - cartRow.getCount());
            cartRowService.deleteCartRowById(cartRow.getId());
        }
        order.setAmount(cart.getAmount());
        client.addOrder(order);
        cart.setAmount(new BigDecimal(0));
        cart.setCartRows(new ArrayList<>());
        cartService.save(cart);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(String status) {
        OrderStatus orderStatus = StatusUtils.getOrderStatus(status);
        if (Objects.isNull(orderStatus)) {
            return List.of();
        }
        return orderRepository.getByStatus(orderStatus);
    }

    @Transactional
    public void updateStatusById(String status, Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order by id='%d' not found".formatted(orderId)));
        OrderStatus newOrderStatus = StatusUtils.getOrderStatus(status);
        if (!Objects.isNull(newOrderStatus)) {
            order.setStatus(newOrderStatus);
            orderRepository.save(order);
        }
    }

    @Transactional(readOnly = true)
    public Order getOrderWithRowsById(Integer orderId) {
        return orderRepository.findWithRowsById(orderId)
                .orElseThrow(() -> new RuntimeException("Order by id='%d' not found".formatted(orderId)));
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByClientId(Integer clientId) {
        return orderRepository.findOrdersByClientId(clientId);
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrdersByClientId(Integer id) {
        return orderRepository.getAllOrdersByClientId(id);
    }
}
