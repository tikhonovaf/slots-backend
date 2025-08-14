package ru.ttk.slotsbe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ttk.slotsbe.backend.model.ClientUserFullView;

import java.util.List;


public interface ClientUserFullViewRepository extends JpaRepository<ClientUserFullView, Long> {
    List<ClientUserFullView> findAll();

    List<ClientUserFullView> findAllById(Long id);
}
