package ru.lazarenko.storemanagement;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.lazarenko.storemanagement.entity.AppUser;
import ru.lazarenko.storemanagement.entity.Role;
import ru.lazarenko.storemanagement.repository.AppUserRepository;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class StoreApplication implements CommandLineRunner {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
//        AppUser applicationUser = new AppUser();
//
//        applicationUser.setLogin("admin");
//        applicationUser.setEmail("admin@mail.ru");
//        applicationUser.setPassword(passwordEncoder.encode("123"));
//
//        Role role1 = new Role();
//        role1.setName("ADMIN");
//        Role role2 = new Role();
//        role2.setName("USER");
//
//        applicationUser.setRoles(List.of(role1, role2));
//
//        appUserRepository.save(applicationUser);
    }

}
