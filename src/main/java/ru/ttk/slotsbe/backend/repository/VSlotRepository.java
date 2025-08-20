package ru.ttk.slotsbe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ttk.slotsbe.backend.model.VSlot;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

public interface VSlotRepository extends JpaRepository<VSlot, Long> {

    @Query(value = "SELECT * FROM v_slot\n" +
            "WHERE (:nStoreIds IS NULL OR n_store_id IN (:nStoreIds) )\n" +
            " AND (:nClientIds IS NULL OR n_client_id IN (:nClientIds))\n" +
            " AND (:vcStatus IS NULL OR vc_status = :vcStatus)\n" +
            " AND (:dDateBegin IS NULL OR d_date >= :dDateBegin)\n" +
            " AND (:dDateEnd IS NULL OR d_date <= :dDateEnd)\n" +
            "   ORDER BY n_store_id, d_date, d_start_time \n"
            , nativeQuery = true)
    List<VSlot> findAllByFilter(List <Long> nStoreIds, List <Long> nClientIds,
                                String vcStatus, @Valid LocalDate dDateBegin, @Valid LocalDate dDateEnd);

    @Query(value = "SELECT * FROM v_slot\n" +
            "WHERE n_client_id = :nClientId" +
            "   ORDER BY n_store_id, d_date, d_start_time \n"
            , nativeQuery = true)
    List<VSlot> findAllByNClientId(Long nClientId);

}
