package ru.lazarenko.storemanagement.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.lazarenko.storemanagement.entity.Cart;
import ru.lazarenko.storemanagement.entity.Client;
import ru.lazarenko.storemanagement.entity.Order;
import ru.lazarenko.storemanagement.model.OrderStatus;
import ru.lazarenko.storemanagement.repository.OrderRepository;
import ru.lazarenko.storemanagement.util.StatusUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService underTest;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    CartService cartService;

    @MockBean
    ClientService clientService;

    @MockBean
    StatusUtils statusUtils;

    static MockedStatic<StatusUtils> mockStatic;

    @BeforeAll
    static void init() {
        mockStatic = mockStatic(StatusUtils.class);
    }

    @Test
    void createOrderByClientId_noSuchElementException_clientWithIdNotFound() {
        doThrow(NoSuchElementException.class)
                .when(cartService).getCartByClientId(anyInt());

        assertThrows(NoSuchElementException.class, () -> underTest.createOrderByClientId(anyInt()));
    }

    @Test
    void createOrderByClientId_successCreate_clientWithIdExist() {
        when(cartService.getCartByClientId(anyInt()))
                .thenReturn(new Cart());

        when(clientService.getClientWithOrdersByClientId(anyInt()))
                .thenReturn(new Client());

        underTest.createOrderByClientId(anyInt());

        verify(orderRepository, Mockito.only()).save(any());
    }

    @Test
    void getOrdersByStatus_emptyList_statusIsNull() {
        mockStatic.when(() -> StatusUtils.getOrderStatus(anyString())).thenReturn(null);

        assertTrue(underTest.getOrdersByStatus("incorrect").isEmpty());
    }

    @Test
    void getOrdersByStatus_notEmptyList_statusIsNotNull() {
        mockStatic.when(() -> StatusUtils.getOrderStatus(anyString())).thenReturn(OrderStatus.NEW);

        when(orderRepository.findByStatus(any(OrderStatus.class)))
                .thenReturn(List.of(new Order()));

        List<Order> result = underTest.getOrdersByStatus("NEW");

        assertFalse(result.isEmpty());
    }

    @Test
    void updateStatusById_noSuchElementException_orderWithIdNotFound() {
        when(orderRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> underTest.updateStatusById("FINISHED", 1));
    }

    @Test
    void updateStatusById_successUpdate_orderWithIdExist() {
        Order order = Order.builder().id(1).amount(new BigDecimal(15000)).status(OrderStatus.NEW).build();

        when(orderRepository.findById(anyInt()))
                .thenReturn(Optional.of(order));

        mockStatic.when(() -> StatusUtils.getOrderStatus(anyString())).thenReturn(OrderStatus.NEW);

        underTest.updateStatusById("NEW", 1);

        verify(orderRepository).save(any());
    }

    @Test
    void getOrderWithRowsById_noSuchElementException_orderWithIdNotFound() {
        when(orderRepository.findWithRowsByClientId(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> underTest.getOrderWithRowsById(anyInt()));
    }

    @Test
    void getOrderWithRowsById_returnedOrder_orderWithIdExist() {
        Order order = Order.builder().id(1).amount(new BigDecimal(15000)).status(OrderStatus.NEW).build();

        when(orderRepository.findWithRowsByClientId(anyInt()))
                .thenReturn(Optional.of(order));

        Order result = underTest.getOrderWithRowsById(anyInt());

        verify(orderRepository, Mockito.only()).findWithRowsByClientId(anyInt());

        assertEquals(order.getAmount(), result.getAmount());
        assertEquals(order.getStatus(), result.getStatus());
    }

    @Test
    void getAllOrders_emptyList_ordersNotExist() {
        when(orderRepository.findAll())
                .thenReturn(List.of());

        List<Order> result = underTest.getAllOrders();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllOrders_notEmptyList_orderExist() {
        Order order = Order.builder().id(1).amount(new BigDecimal(15000)).status(OrderStatus.NEW).build();

        when(orderRepository.findAll())
                .thenReturn(List.of(order));

        List<Order> result = underTest.getAllOrders();

        assertFalse(result.isEmpty());
        assertEquals(order.getAmount(), result.get(0).getAmount());
    }

    @Test
    void getAllOrdersByClientId_emptyList_clientsHasNotOrders() {
        when(orderRepository.findAllByClientId(anyInt()))
                .thenReturn(List.of());

        List<Order> result = underTest.getAllOrdersByClientId(anyInt());

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllOrdersByClientId_notEmptyList_clientsHasOrders() {
        Order order = Order.builder().id(1).amount(new BigDecimal(15000)).status(OrderStatus.NEW).build();

        when(orderRepository.findAllByClientId(anyInt()))
                .thenReturn(List.of(order));

        List<Order> result = underTest.getAllOrdersByClientId(anyInt());

        assertFalse(result.isEmpty());
        assertEquals(order.getAmount(), result.get(0).getAmount());
    }

}