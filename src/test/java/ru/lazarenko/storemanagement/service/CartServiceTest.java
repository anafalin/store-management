package ru.lazarenko.storemanagement.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.lazarenko.storemanagement.entity.Cart;
import ru.lazarenko.storemanagement.entity.CartRow;
import ru.lazarenko.storemanagement.repository.CartRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class CartServiceTest {
    @Autowired
    CartService underTest;

    @MockBean
    CartRepository cartRepository;

    @MockBean
    CartRowService cartRowService;

    @Test
    void getCartByClientId_noSuchElementException_clientWithIdNotExist() {
        when(cartRepository.findCartWithCartRowsByClientId(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> underTest.getCartByClientId(1));
    }

    @Test
    void getCartByClientId_returnedCart_clientWithIdExist() {
        Cart cart = Cart.builder()
                .amount(new BigDecimal(15000))
                .build();
        when(cartRepository.findCartWithCartRowsByClientId(anyInt()))
                .thenReturn(Optional.of(cart));

        Cart result = underTest.getCartByClientId(1);

        assertNotNull(result);
        assertEquals(cart.getAmount(), result.getAmount());
    }

    @Test
    void save() {
        underTest.save(new Cart());

        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void changeCountRowById_noSuchElementException_clientWithIdNotExist() {
        when(cartRepository.findCartWithCartRowsByClientId(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> underTest.changeCountRowById(1, 1, 1));
    }

    @Test
    void changeCountRowById_saved_clientWithIdExist() {
        Cart cart = Cart.builder()
                .amount(new BigDecimal(15000))
                .cartRows(List.of())
                .build();

        when(cartRepository.findCartWithCartRowsByClientId(anyInt()))
                .thenReturn(Optional.of(cart));

        underTest.changeCountRowById(1, 1, 1);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void deleteRowById_noSuchElementException_clientWithIdNotExist() {
        when(cartRepository.findCartWithCartRowsByClientId(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> underTest.deleteRowById(1, 1));
    }

    @Test
    void deleteRowById_wasDelete_clientWithIdExist() {
        CartRow cartRow = CartRow.builder()
                .id(1)
                .amount(new BigDecimal(10000))
                .count(1)
                .build();
        Cart cart = Cart.builder()
                .amount(new BigDecimal(10000))
                .cartRows(new ArrayList<>(List.of(cartRow)))
                .build();

        when(cartRepository.findCartWithCartRowsByClientId(anyInt()))
                .thenReturn(Optional.of(cart));

        underTest.deleteRowById(1, 1);

        verify(cartRowService).deleteCartRowById(anyInt());
    }
}