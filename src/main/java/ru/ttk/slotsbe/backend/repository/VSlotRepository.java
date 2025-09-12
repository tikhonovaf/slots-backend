package ru.ttk.slotsbe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ttk.slotsbe.backend.model.Slot;
import ru.ttk.slotsbe.backend.model.VSlot;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

public interface VSlotRepository extends JpaRepository<VSlot, Long> {

    @Query(value = """
            SELECT * FROM v_slot
            WHERE (:nStoreIds IS NULL OR n_store_id IN (:nStoreIds))
              AND (:nClientIds IS NULL OR n_client_id IN (:nClientIds))
              AND (:nStatusId IS NULL OR n_status_id = :nStatusId)
              AND (:dDateBegin IS NULL OR d_date >= :dDateBegin)
              AND (:dDateEnd IS NULL OR d_date <= :dDateEnd)
            ORDER BY d_date, d_start_time, n_store_id
            """, nativeQuery = true)
    List<VSlot> findAllByFilter(List<Long> nStoreIds, List<Long> nClientIds,
                                Long nStatusId, @Valid LocalDate dDateBegin, @Valid LocalDate dDateEnd);

    @Query(value = """
            SELECT * FROM v_slot
            WHERE n_client_id = :nClientId
            ORDER BY d_date, d_start_time, n_store_id
            """, nativeQuery = true)
    List<VSlot> findAllByNClientId(Long nClientId);

    @Query(value = """
            SELECT * FROM v_slot
            WHERE d_date >= :slotDate
                AND n_status_id  = 1
            """, nativeQuery = true)
    List<VSlot> findAllFreeSlots(LocalDate slotDate);


    @Query(value = """
            SELECT * FROM v_slot
            WHERE (:dDateBegin IS NULL OR d_date >= :dDateBegin)
              AND (:dDateEnd IS NULL OR d_date <= :dDateEnd)
              AND n_status_id = 1
            UNION ALL
            SELECT * FROM v_slot
            WHERE (:dDateBegin IS NULL OR d_date >= :dDateBegin)
              AND (:dDateEnd IS NULL OR d_date <= :dDateEnd)
              AND n_status_id = 2
              AND n_client_id = :clientId
            ORDER BY d_date, d_start_time, n_store_id
            """, nativeQuery = true)
    List<VSlot> findAllByClientIdAndDate(Long clientId, @Valid LocalDate dDateBegin, @Valid LocalDate dDateEnd);

}
