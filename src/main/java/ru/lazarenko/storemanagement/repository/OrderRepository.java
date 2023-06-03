package ru.lazarenko.storemanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.lazarenko.storemanagement.entity.Order;
import ru.lazarenko.storemanagement.model.OrderStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    @Query(value = "select o from Order o left join fetch o.orderRows where o.id=:clientId")
    Optional<Order> findWithRowsByClientId(Integer clientId);

    @Query(value = "select o from Order o left join fetch o.client where o.client.id=:clientId")
    List<Order> findAllByClientId(Integer clientId);

}
