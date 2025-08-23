package ru.ttk.slotsbe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ttk.slotsbe.backend.model.SlotStatus;

import java.util.Optional;


public interface SlotStatusRepository extends JpaRepository<SlotStatus, Long> {
    Optional <SlotStatus> findByVcCode(String code);
}
