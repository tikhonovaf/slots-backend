package ru.ttk.slotsbe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ttk.slotsbe.backend.model.LoadingPoint;

public interface LoadingPointRepository extends JpaRepository<LoadingPoint, Long> {

}
