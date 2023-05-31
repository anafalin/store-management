package ru.lazarenko.storemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.lazarenko.storemanagement.entity.AppUser;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    Optional<AppUser> findAppUsersByLogin(String login);

    AppUser findByActivationCode(String code);

    @Query(value = "select u.client.id from AppUser u left join u.client where u.login=:login")
    Integer findClientIdByLogin(String login);
}
