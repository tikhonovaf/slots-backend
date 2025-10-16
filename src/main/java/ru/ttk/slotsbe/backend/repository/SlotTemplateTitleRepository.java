package ru.ttk.slotsbe.backend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.ttk.slotsbe.backend.model.ClientUser;
import ru.ttk.slotsbe.backend.model.SlotTemplateTitle;

import java.util.List;

public interface SlotTemplateTitleRepository extends JpaRepository<SlotTemplateTitle, Long> {

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM slot_template_title
            WHERE n_slot_template_id  IN (:ids)
            """, nativeQuery = true)
    void deleteAllByIds(List<Long> ids);


}
