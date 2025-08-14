package ru.ttk.slotsbe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ttk.slotsbe.backend.model.ClientUser;

import java.util.List;
import java.util.Optional;

public interface ClientUserRepository extends JpaRepository<ClientUser, Long> {
    Optional<ClientUser> findByLogin(String login);
    List<ClientUser> findAllByLogin(String login);

}
