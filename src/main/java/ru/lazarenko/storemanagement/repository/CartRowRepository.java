package ru.lazarenko.storemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lazarenko.storemanagement.entity.CartRow;

@Repository
public interface CartRowRepository extends JpaRepository<CartRow, Integer> {

}
