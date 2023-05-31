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
class AppUserRepositoryTest {

    @Autowired
    AppUserRepository underTest;

    @Autowired
    EntityManager entityManager;

    @AfterEach
    void destroy() {
        entityManager.clear();
    }

    @Test
    void findAppUsersByLogin_notEmptyOptional_userExist() {
        String login = "user";

        Optional<AppUser> foundUser = underTest.findAppUsersByLogin(login);

        assertTrue(foundUser.isPresent());

        AppUser actual = foundUser.get();

        assertAll(
                () -> assertEquals(1, actual.getId()),
                () -> assertEquals("user", actual.getLogin()),
                () -> assertEquals("user@mail.ru", actual.getEmail())
        );
    }

    @Test
    void findByActivationCode_foundUser_userWithActivationCodeExist() {
        String code = "123456789";

        AppUser actual = underTest.findByActivationCode(code);

        assertAll(
                () -> assertEquals("user@mail.ru", actual.getEmail()),
                () -> assertEquals("user", actual.getLogin()),
                () -> assertEquals("123", actual.getPassword())
        );
    }

    @Test
    void findByActivationCode_null_userWithActivationCodeNotExist() {
        String code = "00000";

        AppUser actual = underTest.findByActivationCode(code);

        assertNull(actual);
    }

    @Test
    void findClientIdByLogin_correctId_loginExist() {
        String login = "user";

        Integer actual = underTest.findClientIdByLogin(login);
        Integer expected = 1;

        assertEquals(expected, actual);
    }

    @Test
    void findClientIdByLogin_null_loginNotExist() {
        String login = "not_exist_login";

        Integer actual = underTest.findClientIdByLogin(login);
        Integer expected = null;

        assertEquals(expected, actual);
    }
}