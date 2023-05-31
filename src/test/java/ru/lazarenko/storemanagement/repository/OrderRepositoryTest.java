package ru.lazarenko.storemanagement.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.lazarenko.storemanagement.entity.Order;
import ru.lazarenko.storemanagement.model.OrderStatus;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@Sql("h2-truncate.sql")
@DataJpaTest
class OrderRepositoryTest {
    @Autowired
    OrderRepository underTest;

    @Autowired
    EntityManager entityManager;

    @AfterEach
    void destroy() {
        entityManager.clear();
    }

    @Test
    void getByStatus_emptyList_finishedOrdersNotExist() {
        List<Order> result = underTest.findByStatus(OrderStatus.FINISHED);
        assertTrue(result.isEmpty());
    }

    @Test
    void getByStatus_notEmptyList_newOrdersExist() {
        List<Order> result = underTest.findByStatus(OrderStatus.NEW);
        assertFalse(result.isEmpty());
        assertEquals(10000, result.get(0).getAmount().intValue());
        assertEquals(OrderStatus.NEW, result.get(0).getStatus());
    }

    @Test
    void findOrdersByClientId_emptyList_userOrdersNotExist() {
        List<Order> result = underTest.findAllByClientId(100);
        assertTrue(result.isEmpty());
    }

    @Test
    void findOrdersByClientId_notEmptyList_userOrdersExist() {
        List<Order> result = underTest.findAllByClientId(1);
        assertFalse(result.isEmpty());
        assertEquals(10000, result.get(0).getAmount().intValue());
        assertEquals(OrderStatus.NEW, result.get(0).getStatus());
    }

    @Test
    void findAllByClientId_emptyList_userOrdersNotExist() {
        List<Order> result = underTest.findAllByClientId(100);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllByClientId_notEmptyList_userOrdersExist() {
        List<Order> result = underTest.findAllByClientId(1);
        assertFalse(result.isEmpty());
        assertEquals(1 ,result.size());
        assertEquals(10000, result.get(0).getAmount().intValue());
        assertEquals(OrderStatus.NEW, result.get(0).getStatus());
    }

    @Test
    void findWithRowsByClientId_emptyList_userOrdersNotExist() {
        Optional<Order> resultOptional = underTest.findWithRowsByClientId(100);
        assertTrue(resultOptional.isEmpty());
    }

    @Test
    void findWithRowsByClientId_notEmptyList_userOrdersExist() {
        Optional<Order> resultOptional = underTest.findWithRowsByClientId(1);

        assertFalse(resultOptional.isEmpty());

        Order result = resultOptional.get();

        assertAll(
                () -> assertEquals(OrderStatus.NEW, result.getStatus()),
                () -> assertEquals(10000, result.getAmount().intValue()),
                () -> assertNotNull(result.getOrderRows()),
                () -> assertFalse(result.getOrderRows().isEmpty()),
                () -> assertEquals(1, result.getOrderRows().get(0).getOrder().getId()),
                () -> assertEquals(2, result.getOrderRows().get(0).getProduct().getId()),
                () -> assertEquals("Intel i-5", result.getOrderRows().get(0).getProduct().getName())
        );
    }
}