package ru.lazarenko.storemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.lazarenko.storemanagement.entity.CartRow;

@Service
public interface CartRowRepository extends JpaRepository<CartRow, Integer> {

}
