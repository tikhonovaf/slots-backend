package ru.ttk.slotsbe.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.ttk.slotsbe.backend.model.ClientUser;
import ru.ttk.slotsbe.backend.repository.ClientUserRepository;

@Service
@RequiredArgsConstructor
public class ClientUserDetailsService implements UserDetailsService {

    private final ClientUserRepository clientUserRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        ClientUser user = clientUserRepository.findByVcLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + login + " not found!"));

        return User.withUsername(login)
                .password(user.getVcPassword())
                .roles("ADMIN")
                .build();
    }
}
