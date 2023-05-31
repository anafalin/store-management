package ru.lazarenko.storemanagement.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.lazarenko.storemanagement.entity.AppUser;
import ru.lazarenko.storemanagement.entity.Cart;
import ru.lazarenko.storemanagement.entity.Client;
import ru.lazarenko.storemanagement.entity.Role;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Sql("h2-truncate.sql")
@DataJpaTest
class ClientRepositoryTest {
    @Autowired
    ClientRepository underTest;

    @Autowired
    EntityManager entityManager;

    @AfterEach
    void destroy() {
        entityManager.clear();
    }

    @Test
    void findWithUserById_notEmptyOptional_userExist() {
        Optional<Client> resultOptional = underTest.findWithUserById(1);

        assertTrue(resultOptional.isPresent());

        Client result = resultOptional.get();

        assertAll(
                () -> assertEquals("user", result.getUser().getLogin()),
                () -> assertEquals("user@mail.ru", result.getUser().getEmail()),
                () -> assertEquals("User Firstname", result.getFirstname()),
                () -> assertEquals("User LastName", result.getLastname())
        );
    }

    @Test
    void findWithUserById_emptyOptional_userNotExist() {
        Optional<Client> actualOptional = underTest.findWithUserById(0);

        assertTrue(actualOptional.isEmpty());
    }

    @Test
    void findAllWithUser_notEmptyList_userExist() {
        List<Client> result = underTest.findAllWithUser();

        assertFalse(result.isEmpty());
        assertAll(
                () -> assertEquals("User Firstname", result.get(0).getFirstname()),
                () -> assertEquals("user", result.get(0).getUser().getLogin())
        );
    }

    @Test
    void findClientWithOrdersByClientId_notEmptyOptionalAndOrderListIsNotEmpty_clientExistAndOrdersNotExist() {
        Optional<Client> resultOptional = underTest.findClientWithOrdersByClientId(1);

        assertTrue(resultOptional.isPresent());
        assertFalse(resultOptional.get().getOrders().isEmpty());
    }
}