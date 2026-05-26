package com.keskin.gymanalyzer.users.infrastructure.security;

import com.keskin.gymanalyzer.users.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final User user;

    @Override
    public String getUsername() {
        // unique identifier, must use email instead of name
        return user.getEmail().getValue();
    }

    @Override
    public String getPassword() {
        return user.getPassword().getValue();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public boolean isEnabled() {
        return !user.isDeleted();
    }


    // lock account after 3 wrong attempts.
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    public User getDomainUser() {
        return user;
    }
}