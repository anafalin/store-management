package ru.lazarenko.storemanagement.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.lazarenko.storemanagement.entity.AppUser;
import ru.lazarenko.storemanagement.entity.Role;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class SecurityUser implements UserDetails {
    private final AppUser appUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Role> roles = appUser.getRoles();
        return roles
                .stream()
                .map(SecurityRole::new)
                .toList();
    }

    @Override
    public String getPassword() {
        return appUser.getPassword();
    }

    @Override
    public String getUsername() {
        return appUser.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !appUser.getIsAccountLocked();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
