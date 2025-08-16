package ru.ttk.slotsbe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ttk.slotsbe.backend.model.VSlot;

import java.util.Date;
import java.util.List;

public interface VSlotRepository extends JpaRepository<VSlot, Long> {

    @Query(value = "SELECT * FROM v_slot\n" +
            "WHERE (:nStoreId IS NULL OR n_store_id = :nStoreId )\n" +
            " AND (:nClientId IS NULL OR n_client_id = :nClientId)\n" +
            " AND (:vcStatus IS NULL OR vc_status = :vcStatus)\n" +
            " AND (:dDate IS NULL OR d_date = :dDate)\n" +
            "   ORDER BY n_store_id, d_date, d_start_time \n"
            , nativeQuery = true)
    List<VSlot> findAllByFilter(Long nStoreId, Long nClientId, String vcStatus, Date dDate);
}
