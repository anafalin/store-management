package ru.lazarenko.storemanagement.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.lazarenko.storemanagement.entity.Client;
import ru.lazarenko.storemanagement.entity.Product;
import ru.lazarenko.storemanagement.repository.ProductRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService underTest;

    @MockBean
    ProductRepository productRepository;

    @Test
    void create() {
        underTest.create(any(Product.class));

        verify(productRepository, Mockito.only()).save(any());
    }

    @Test
    void getAllProducts_emptyList_productNotExist() {
        Pageable paging = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(), paging, 0);

        when(productRepository.findAll(paging))
                .thenReturn(productPage);

        List<Product> result = underTest.getAllProducts(paging).getContent();

        verify(productRepository, Mockito.only()).findAll(paging);

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllProducts_notEmptyList_productExist() {
        Product product1 = Product.builder()
                .id(1)
                .name("Laptop")
                .build();

        Product product2 = Product.builder()
                .id(2)
                .name("Phone")
                .build();

        Pageable paging = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product1, product2), paging, 0);

        when(productRepository.findAll(paging))
                .thenReturn(productPage);

        List<Product> result = underTest.getAllProducts(paging).getContent();

        verify(productRepository, Mockito.only()).findAll(paging);

        assertAll(
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(product1, result.get(0)),
                () -> assertEquals(product1.getName(), result.get(0).getName()),
                () -> assertEquals(product2.getName(), result.get(1).getName())
        );
    }

    @Test
    void getProductById_correctProduct_productWithIdExist() {
        Product product = Product.builder()
                .id(1)
                .name("Laptop")
                .build();

        when(productRepository.findById(anyInt()))
                .thenReturn(Optional.of(product));

        Product result = underTest.getProductById(anyInt());

        verify(productRepository, Mockito.only()).findById(anyInt());

        assertAll(
                () -> assertEquals(product.getName(), result.getName()),
                () -> assertEquals(product.getId(), result.getId())
        );
    }

    @Test
    void getProductById_noSuchElementException_productWithIdNotExist() {
        when(productRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> underTest.getProductById(anyInt()));
    }

}