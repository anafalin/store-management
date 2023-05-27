package ru.lazarenko.storemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import ru.lazarenko.storemanagement.entity.Cart;

import java.util.Optional;

@Service
public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query(value = "select c from Cart c " +
            "left join fetch c.cartRows " +
            "where c.client.id = :clientId")
    Optional<Cart> findCartWithCartRowsByClientId(Integer clientId);
}
