package ru.lazarenko.storemanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lazarenko.storemanagement.repository.CartRowRepository;

@Service
@RequiredArgsConstructor
public class CartRowService {

    private final CartRowRepository cartRowRepository;

    @Transactional
    public void deleteCartRowById(Integer id){
        cartRowRepository.deleteById(id);
    }
}
