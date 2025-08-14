package ru.ttk.slotsbe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ttk.slotsbe.backend.model.Client;
import ru.ttk.slotsbe.backend.model.ClientView;

import java.util.List;


public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findAllByName(String name);
}
