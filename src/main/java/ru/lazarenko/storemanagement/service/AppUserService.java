package ru.lazarenko.storemanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.lazarenko.storemanagement.dto.CreateUserRequest;
import ru.lazarenko.storemanagement.entity.AppUser;
import ru.lazarenko.storemanagement.entity.Cart;
import ru.lazarenko.storemanagement.entity.Client;
import ru.lazarenko.storemanagement.entity.Role;
import ru.lazarenko.storemanagement.repository.AppUserRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final SmtpMailSender smtpMailSender;

    @Transactional(readOnly = true)
    public boolean checkIsUniqueLogin(String login) {
        return appUserRepository.findAppUsersByLogin(login).isPresent();
    }

    @Transactional
    public void createUser(CreateUserRequest createUserRequest) {
        Role role = new Role();
        role.setName("USER");

        Client client = Client.builder()
                .firstname(createUserRequest.getFirstname())
                .lastname(createUserRequest.getLastname())
                .build();

        client.setCart(new Cart());

        AppUser appUser = AppUser.builder()
                .login(createUserRequest.getLogin())
                .email(createUserRequest.getEmail())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .activationCode(UUID.randomUUID().toString())
                .roles(List.of(role))
                .client(client)
                .isAccountActive(false)
                .isAccountLocked(false)
                .build();

        client.setUser(appUser);

        appUserRepository.save(appUser);

        sendMessage(appUser);
    }

    @Transactional
    public boolean activeUser(String code) {
        AppUser user = appUserRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        user.setIsAccountActive(true);

        appUserRepository.save(user);

        return true;
    }

    private void sendMessage(AppUser appUser) {
        if (StringUtils.hasLength(appUser.getEmail())) {
            String message = String.format(
                    """
                            Hello, %s!\s
                            Welcome to Programmer Store! Please, visit next link: http://localhost:8080/user/activate/%s\s
                            This message was created automatically. There is no need to answer it.\s\s
                            Afalin bot.""",
                    appUser.getLogin(),
                    appUser.getActivationCode()
            );

            smtpMailSender.send(appUser.getEmail(), "Activation code", message);
        }
    }

    @Transactional(readOnly = true)
    public Integer getClientIdByLogin(String login) {
        return appUserRepository.findClientIdByLogin(login);
    }
}
