package ru.ttk.slotsbe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ClientUserFullViewRepository extends JpaRepository<ClientUserView, Long> {
    List<ClientUserView> findAll();

    List<ClientUserView> findAllById(Long id);
}
