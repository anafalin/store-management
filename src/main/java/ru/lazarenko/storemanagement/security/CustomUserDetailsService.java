package ru.lazarenko.storemanagement.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.lazarenko.storemanagement.repository.AppUserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findAppUsersByLogin(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User with username='%s' not found".formatted(username)
                ));
    }
}
