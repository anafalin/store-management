package ru.lazarenko.storemanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lazarenko.storemanagement.entity.Product;
import ru.lazarenko.storemanagement.repository.ProductRepository;

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
    public Page<Product> getAllProducts(Pageable paging) {
        return productRepository.findAll(paging);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Integer productId) {
        return productRepository
                .findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product by id='%d' not found".formatted(productId)));
    }

    @Transactional
    public void addCountProductById(Integer productId, int count) {
        Product product = getProductById(productId);
        product.setCount(product.getCount() + count);

        productRepository.save(product);
    }
}
