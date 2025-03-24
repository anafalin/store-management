package ru.lazarenko.storemanagement.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.lazarenko.storemanagement.entity.AppUser;
import ru.lazarenko.storemanagement.entity.Role;
import ru.lazarenko.storemanagement.repository.AppUserRepository;

import java.util.List;

@Component
@ConditionalOnProperty(name = "app.initial.setup", havingValue = "true")
public class UserDataInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDataInitializer(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        AppUser applicationUser = new AppUser();
        applicationUser.setLogin("admin");
        applicationUser.setEmail("admin@mail.ru");
        applicationUser.setPassword(passwordEncoder.encode("123"));

        Role role1 = new Role();
        role1.setName("ADMIN");
        Role role2 = new Role();
        role2.setName("USER");

        applicationUser.setRoles(List.of(role1, role2));
        appUserRepository.save(applicationUser);
    }
}
