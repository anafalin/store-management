package ru.lazarenko.storemanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lazarenko.storemanagement.entity.Product;
import ru.lazarenko.storemanagement.repository.ProductRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public void create(Product product) {
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product getProductById(Integer productId) {
        return productRepository
                .findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product by id='%d' not found".formatted(productId)));
    }
}
