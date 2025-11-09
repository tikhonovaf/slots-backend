package ru.ttk.slotsbe.backend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ttk.slotsbe.backend.model.SlotTemplateDetail;
import ru.ttk.slotsbe.backend.model.VSlotTemplateDetail;

import java.util.List;

public interface VSlotTemplateDetailRepository extends JpaRepository<VSlotTemplateDetail, Long> {
    @Query(value = "SELECT * FROM v_slot_template_detail\n  " +
            " WHERE n_slot_template_id = :nSlotTemplateId "
            , nativeQuery = true)
    List<VSlotTemplateDetail> findAllByNSlotTemplateId(Long nSlotTemplateId);
    }
