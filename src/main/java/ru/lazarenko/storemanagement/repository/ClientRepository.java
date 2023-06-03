package ru.lazarenko.storemanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.lazarenko.storemanagement.entity.Client;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    @Query(value = "select c from Client c left join fetch c.orders where c.id=:clientId")
    Optional<Client> findClientWithOrdersByClientId(Integer clientId);

    @Query(value = "select c from Client c left join fetch c.user where c.id=:id")
    Optional<Client> findWithUserById(Integer id);

    @Query(value = "select distinct c from Client c left join fetch c.user", countQuery = "select count(c) from Client c")
    Page<Client> findAllWithUser(Pageable pageable);

    @Query(value = "select c from Client c left join fetch c.user")
    List<Client> findAllWithUser();
}
