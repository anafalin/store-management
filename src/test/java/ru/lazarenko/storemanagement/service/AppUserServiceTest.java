package ru.lazarenko.storemanagement.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.lazarenko.storemanagement.dto.CreateUserRequest;
import ru.lazarenko.storemanagement.entity.AppUser;
import ru.lazarenko.storemanagement.repository.AppUserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AppUserServiceTest {

    @Autowired
    AppUserService underTest;

    @MockBean
    AppUserRepository appUserRepository;

    @MockBean
    SmtpMailSender smtpMailSender;


    @Test
    void create() {
        CreateUserRequest request = CreateUserRequest.builder()
                .login("login")
                .email("email")
                .firstname("firstname")
                .lastname("lastname")
                .password("123")
                .passwordConfirm("123")
                .build();
        doNothing().when(smtpMailSender).send(anyString(), anyString(), anyString());
        underTest.createUser(request);

        verify(appUserRepository, Mockito.only()).save(any());
    }

    @Test
    void activeUser_false_activationCodeIsIncorrect() {
        when(appUserRepository.findByActivationCode(anyString())).thenReturn(null);

        boolean result = underTest.activeUser("11111");

        assertFalse(result);
    }

    @Test
    void activeUser_true_activationCodeIsCorrect() {
        AppUser appUser = new AppUser();
        when(appUserRepository.findByActivationCode(anyString())).thenReturn(appUser);

        boolean result = underTest.activeUser("11111");

        verify(appUserRepository).save(appUser);
        assertTrue(result);
    }

    @Test
    void getClientIdByLogin_correctId_loginIsExist() {
        when(appUserRepository.findClientIdByLogin(anyString())).thenReturn(anyInt());

        Integer result = underTest.getClientIdByLogin("login");

        verify(appUserRepository).findClientIdByLogin(anyString());
        assertNotNull(result);
    }

}