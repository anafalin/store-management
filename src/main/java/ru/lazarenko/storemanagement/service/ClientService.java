package ru.lazarenko.storemanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lazarenko.storemanagement.entity.Cart;
import ru.lazarenko.storemanagement.entity.CartRow;
import ru.lazarenko.storemanagement.entity.Client;
import ru.lazarenko.storemanagement.entity.Product;
import ru.lazarenko.storemanagement.repository.ClientRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final CartService cartService;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public List<Client> getAllClients() {
        return clientRepository.findAllWithUserInfo();
    }

    @Transactional
    public void addProductInCart(Integer clientId, Integer productId, Integer count) {
        Cart cart = cartService.getCartByClientId(clientId);
        Product product = productService.getProductById(productId);

        BigDecimal amountRow = new BigDecimal(count).multiply(product.getPrice());
        CartRow cartRow = CartRow.builder()
                .product(product)
                .count(count)
                .amount(amountRow)
                .build();

        cart.addCartRow(cartRow);
        cart.setAmount(cart.getAmount().add(amountRow));
        cartService.save(cart);
    }

    @Transactional
    public Client getClientWithOrdersByClientId(Integer id) {
        return clientRepository.getClientWithOrdersByClientId(id)
                .orElseThrow(() -> new RuntimeException("Client with id = '%d' not found".formatted(id)));
    }

    @Transactional(readOnly = true)
    public Client getClientFullInfoById(Integer id) {
        return clientRepository.findWithUserById(id)
                .orElseThrow(() -> new RuntimeException("Client with id = '%d' not found".formatted(id)));
    }
}
