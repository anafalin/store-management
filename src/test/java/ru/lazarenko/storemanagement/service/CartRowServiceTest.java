package ru.lazarenko.storemanagement.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.lazarenko.storemanagement.repository.CartRowRepository;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CartRowServiceTest {
    @Autowired
    CartRowService underTest;

    @MockBean
    CartRowRepository cartRowRepository;

    @Test
    void deleteCartRowById() {
        underTest.deleteCartRowById(1);

        verify(cartRowRepository, only()).deleteById(anyInt());
    }

}