package ru.ttk.slotsbe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ttk.slotsbe.backend.model.SlotView;

import java.util.List;

public interface SlotViewRepository extends JpaRepository<SlotView, Long> {
}
