package ru.ttk.slotsbe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ttk.slotsbe.backend.model.VStore;

import java.util.List;


public interface VStoreRepository extends JpaRepository<VStore, Long> {
    List<VStore> findAllByVcCode(String vcCode);
}
