package ru.ttk.slotsbe.backend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.ttk.slotsbe.backend.model.Slot;

import java.time.LocalDate;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    @Modifying
    @Transactional
    @Query(value = """
    DELETE FROM slot
    WHERE n_loading_point_id IN (
        SELECT n_loading_point_id FROM loading_point
        WHERE n_store_id = :nStoreId
    )
    AND d_date = :dDate
    """, nativeQuery = true)
    void deleteSlotsByStoreIdAndDate(Long nStoreId, LocalDate dDate);

    @Query(value = """
    SELECT * FROM slot
    WHERE n_loading_point_id IN (
        SELECT n_loading_point_id FROM loading_point
        WHERE n_store_id = :nStoreId
    )
    AND d_date = :dDate AND n_status_id  = 2
    """, nativeQuery = true)
    List<Slot> findReservedSlotsByStoreIdAndDate(Long nStoreId, LocalDate dDate);


}
