package ru.lazarenko.storemanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lazarenko.storemanagement.dto.UpdateUserRequest;
import ru.lazarenko.storemanagement.entity.Cart;
import ru.lazarenko.storemanagement.entity.CartRow;
import ru.lazarenko.storemanagement.entity.Client;
import ru.lazarenko.storemanagement.entity.Product;
import ru.lazarenko.storemanagement.repository.ClientRepository;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final CartService cartService;
    private final ProductService productService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<Client> getAllClients(Pageable pageable) {
        return clientRepository.findAllWithUser(pageable);
    }

    @Transactional
    public void addProductInCart(Integer clientId, Integer productId, Integer count) {
        Cart cart = cartService.getCartByClientId(clientId);
        Product product = productService.getProductById(productId);

        for (CartRow cartRow : cart.getCartRows()) {
            if(cartRow.getProduct().equals(product)) {
                cartRow.setCount(cartRow.getCount() + count);
                cartRow.setAmount(new BigDecimal(cartRow.getCount()).multiply(product.getPrice()));
                cartService.save(cart);

                updateAmountCart(cart);
                return;
            }
        }

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
        return clientRepository.findClientWithOrdersByClientId(id)
                .orElseThrow(() -> new NoSuchElementException("Client with id = '%d' not found".formatted(id)));
    }

    @Transactional(readOnly = true)
    public Client getClientWithUserById(Integer id) {
        return clientRepository.findWithUserById(id)
                .orElseThrow(() -> new NoSuchElementException("Client with id = '%d' not found".formatted(id)));
    }

    @Transactional
    public void updateUser(UpdateUserRequest request, Integer clientId) {
        Client client = clientRepository.findWithUserById(clientId)
                .orElseThrow(() -> new NoSuchElementException("Client with id = '%d' not found".formatted(clientId)));

        client.setLastname(request.getLastname());
        client.setFirstname(request.getFirstname());

        client.getUser().setEmail(request.getEmail());
        if(!request.getPassword().isEmpty()) {
            client.getUser().setPassword(passwordEncoder.encode(request.getPassword()));
        }

        clientRepository.save(client);
    }

    private void updateAmountCart(Cart cart) {
        BigDecimal amount = new BigDecimal(0);
        for (CartRow cartRow : cart.getCartRows()) {
            amount = amount.add(new BigDecimal(cartRow.getCount()).multiply(cartRow.getProduct().getPrice()));
        }
        cart.setAmount(amount);
    }
}
