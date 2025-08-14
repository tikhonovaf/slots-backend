package ru.ttk.slotsbe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ttk.slotsbe.backend.model.ClientView;

import java.util.List;


public interface ClientViewRepository extends JpaRepository<ClientView, Long> {
    List<ClientView> findAllByName(String name);
}
