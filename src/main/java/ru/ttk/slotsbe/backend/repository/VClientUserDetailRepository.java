package ru.ttk.slotsbe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ttk.slotsbe.backend.model.Slot;
import ru.ttk.slotsbe.backend.model.VClientUserDetail;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface VClientUserDetailRepository extends JpaRepository<VClientUserDetail, Long> {
    @Query(value = """
            SELECT * FROM v_client_user_detail
            WHERE n_client_id = :nClientId
            """, nativeQuery = true)

    List<VClientUserDetail> findByClientId(Long nClientId);
}
