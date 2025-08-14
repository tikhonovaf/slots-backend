package ru.ttk.slotsbe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ttk.slotsbe.backend.model.Slot;

import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {

}
