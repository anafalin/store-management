package ru.lazarenko.storemanagement.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SmtpMailSenderTest {
    @Autowired
    SmtpMailSender underTest;

    @MockBean
    JavaMailSender javaMailSender;

    @Captor
    ArgumentCaptor<SimpleMailMessage> captor;

    @Test
    void send() {
        String emailTo = "user@mail.ru";
        String subject = "Activation code";
        String message = "Hello!";

        doNothing()
                .when(javaMailSender).send(mock(SimpleMailMessage.class));

        underTest.send(emailTo, subject, message);

        verify(javaMailSender).send(captor.capture());
        SimpleMailMessage simpleMailMessage = captor.getValue();

        assertAll(
                () -> assertEquals(subject, simpleMailMessage.getSubject()),
                () -> assertEquals(message, simpleMailMessage.getText()),
                () -> assertNotNull(simpleMailMessage.getTo()),
                () -> assertEquals(emailTo, Objects.requireNonNull(simpleMailMessage.getTo())[0])
        );
    }
}