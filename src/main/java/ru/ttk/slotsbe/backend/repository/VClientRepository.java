package ru.ttk.slotsbe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ttk.slotsbe.backend.model.VClient;

import java.util.List;


public interface VClientRepository extends JpaRepository<VClient, Long> {
    List<VClient> findAllByVcCode(String vcCode);
}
