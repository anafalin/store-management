package ru.lazarenko.storemanagement.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.lazarenko.storemanagement.entity.Cart;
import ru.lazarenko.storemanagement.entity.Order;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Sql("h2-truncate.sql")
@DataJpaTest
class CartRepositoryTest {

    @Autowired
    CartRepository underTest;

    @Autowired
    EntityManager entityManager;

    @AfterEach
    void destroy() {
        entityManager.clear();
    }

    @Test
    void findCartWithCartRowsByClientId_emptyOptional_userNotExist() {
        Optional<Cart> resultOptional = underTest.findCartWithCartRowsByClientId(100);
        assertTrue(resultOptional.isEmpty());
    }

    @Test
    void findCartWithCartRowsByClientId_notEmptyOptional_userExist() {
        Optional<Cart> resultOptional = underTest.findCartWithCartRowsByClientId(1);

        assertFalse(resultOptional.isEmpty());

        Cart result = resultOptional.get();
        assertAll(
                () -> assertEquals(20000, result.getAmount().intValue()),
                () -> assertEquals("User Firstname", result.getClient().getFirstname()),
                () -> assertNotNull(result.getCartRows()),
                () -> assertFalse(result.getCartRows().isEmpty()),
                () -> assertEquals(2, result.getCartRows().size()),
                () -> assertEquals(15000, result.getCartRows().get(0).getAmount().intValue()),
                () -> assertEquals("Laptop", result.getCartRows().get(0).getProduct().getName())
        );
    }
}