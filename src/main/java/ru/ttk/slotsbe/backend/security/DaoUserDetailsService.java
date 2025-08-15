package ru.ttk.slotsbe.backend.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.ttk.slotsbe.backend.model.ClientUser;
import ru.ttk.slotsbe.backend.repository.ClientUserRepository;

import javax.inject.Inject;

@Component
public class DaoUserDetailsService implements UserDetailsService {
    @Inject
    private ClientUserRepository clientUserRepository;

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
