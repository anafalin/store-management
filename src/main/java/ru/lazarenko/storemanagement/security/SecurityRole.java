package ru.lazarenko.storemanagement.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import ru.lazarenko.storemanagement.entity.Role;

@RequiredArgsConstructor
public class SecurityRole implements GrantedAuthority {

    private final Role role;

    @Override
    public String getAuthority() {
        String prefix = "ROLE_";
        String name = role.getName().toUpperCase();
        if(!name.startsWith(prefix)){
            name = prefix.concat(name);
        }
        return name;
    }
}
