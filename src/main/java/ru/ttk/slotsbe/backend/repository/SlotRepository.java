package ru.ttk.slotsbe.backend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.ttk.slotsbe.backend.model.Slot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

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

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM slot
             WHERE (:loadingPointIds IS NULL OR n_loading_point_id IN (:loadingPointIds))
             AND d_date = :dDate
            """, nativeQuery = true)
    void deleteSlotsByloadingPointIdsIdAndDate(List<Long> loadingPointIds, LocalDate dDate);

    @Query(value = """
            SELECT * FROM slot
            WHERE n_loading_point_id IN (
                SELECT n_loading_point_id FROM loading_point
                WHERE n_store_id = :nStoreId
            )
            AND d_date = :dDate AND n_status_id  <> 1
            """, nativeQuery = true)
    List<Slot> findReservedSlotsByStoreIdAndDate(Long nStoreId, LocalDate dDate);

    @Query(value = """
            SELECT * FROM slot
            WHERE n_loading_point_id = :loadingPointId
                AND d_date = :slotDate
                AND d_start_time  = :startTime
                AND n_status_id  = 1
            """, nativeQuery = true)
    List<Slot> findAllFreeSlotsByPointId(Long loadingPointId, LocalDate slotDate, LocalTime startTime);

    @Query(value = """
            SELECT * FROM slot
            WHERE n_loading_point_id = :nLoadingPointId
            AND d_date = :dDate AND n_status_id  <> 1
            """, nativeQuery = true)
    List<Slot> findReservedSlotsByLoadingPointAndDate(Long nLoadingPointId, LocalDate dDate);

    @Query(value = """
            SELECT slot.n_loading_point_id FROM slot
             WHERE (n_loading_point_id IN (:loadingPointIds))
            AND d_date = :dDate AND n_status_id  <> 1
            """, nativeQuery = true)
    List<Long> findReservedSlotsByloadingPointIdsAndDate(Set loadingPointIds, LocalDate dDate);


}
