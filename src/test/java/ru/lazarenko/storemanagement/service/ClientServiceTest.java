package ru.lazarenko.storemanagement.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.lazarenko.storemanagement.dto.UpdateUserRequest;
import ru.lazarenko.storemanagement.entity.AppUser;
import ru.lazarenko.storemanagement.entity.Client;
import ru.lazarenko.storemanagement.entity.Order;
import ru.lazarenko.storemanagement.repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ClientServiceTest {
    @Autowired
    ClientService underTest;

    @MockBean
    ClientRepository clientRepository;

    @MockBean
    CartService cartService;

    @MockBean
    ProductService productService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Test
    void getAllClients_emptyList_clientsNotExist() {
        when(clientRepository.findAllWithUser()).thenReturn(List.of());

        List<Client> result = underTest.getAllClients();
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllClients_notEmptyList_clientsExist() {
        AppUser appUser = AppUser.builder()
                .email("user@mail.ru")
                .login("user")
                .password("123")
                .build();

        Client client = Client.builder()
                .firstname("Mike")
                .lastname("Petrov")
                .user(appUser)
                .build();

        when(clientRepository.findAllWithUser()).thenReturn(List.of(client));

        List<Client> result = underTest.getAllClients();
        assertFalse(result.isEmpty());
    }

    @Test
    void getClientWithOrdersByClientId_noSuchElementException_clientWithIdNotExist() {
        when(clientRepository.findClientWithOrdersByClientId(anyInt())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> underTest.getClientWithOrdersByClientId(1));
    }

    @Test
    void getClientWithOrdersByClientId_returnClient_clientWithIdExist() {
        Client client = Client.builder()
                .firstname("Mike")
                .lastname("Petrov")
                .orders(new ArrayList<>(List.of(new Order())))
                .build();

        when(clientRepository.findClientWithOrdersByClientId(anyInt())).thenReturn(Optional.of(client));

        Client result = underTest.getClientWithOrdersByClientId(1);

        assertAll(
                () -> assertEquals(client.getFirstname(), result.getFirstname()),
                () -> assertFalse(client.getOrders().isEmpty())
        );
    }

    @Test
    void getClientWithUserById_noSuchElementException_clientWithIdNotExist() {
        when(clientRepository.findWithUserById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> underTest.getClientWithUserById(1));
    }

    @Test
    void getClientWithUserById_returnClient_clientWithIdExist() {
        AppUser appUser = AppUser.builder()
                .email("user@mail.ru")
                .login("user")
                .password("123")
                .build();

        Client client = Client.builder()
                .firstname("Mike")
                .lastname("Petrov")
                .user(appUser)
                .build();

        when(clientRepository.findWithUserById(anyInt())).thenReturn(Optional.of(client));

        Client result = underTest.getClientWithUserById(1);

        assertAll(
                () -> assertEquals(client.getFirstname(), result.getFirstname()),
                () -> assertEquals(client.getUser().getLogin(), result.getUser().getLogin())
        );
    }

    @Test
    void updateUser_noSuchElementException_clientWithIdNotExist() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .email("new email")
                .firstname("new name")
                .lastname("name lastname")
                .build();

        when(clientRepository.findWithUserById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> underTest.updateUser(request, 1));
    }

    @Test
    void updateUser_successUpdate_clientWithIdExist() {
        AppUser appUser = AppUser.builder()
                .email("user@mail.ru")
                .login("user")
                .password("123")
                .build();

        Client client = Client.builder()
                .id(1)
                .firstname("Mike")
                .lastname("Petrov")
                .user(appUser)
                .build();

        UpdateUserRequest request = UpdateUserRequest.builder()
                .email("new email")
                .firstname("new name")
                .lastname("name lastname")
                .password("111")
                .build();

        when(clientRepository.findWithUserById(anyInt())).thenReturn(Optional.of(client));

        underTest.updateUser(request, 1);

        verify(clientRepository).save(client);
    }

}