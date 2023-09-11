package com.example.demo.service.authentication;

import com.example.demo.entities.Account;
import com.example.demo.service.account.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationService implements UserDetailsService {
    @Autowired
    private IAccountService accountService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountService.getAccountByEmail(email);
        if (account == null) {
            throw new UsernameNotFoundException("Could not find account");
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(account.getRole()));
        return User.withUsername(account.getEmail())
                .password(passwordEncoder().encode(account.getPassword()))
                .authorities(authorities)
                .build();
    }

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
