package ru.lazarenko.storemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lazarenko.storemanagement.entity.OrderRow;

@Repository
public interface OrderRowRepository extends JpaRepository<OrderRow, Integer> {
}
