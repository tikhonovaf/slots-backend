package ru.ttk.slotsbe.backend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.ttk.slotsbe.backend.model.SlotTemplateDetail;

import java.util.List;

public interface SlotTemplateDetailRepository extends JpaRepository<SlotTemplateDetail, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM slot_template_detail\n  " +
            " WHERE n_loading_point_id IN (" +
            " SELECT n_loading_point_id FROM loading_point " +
            " WHERE n_store_id = :nStoreId )"
            , nativeQuery = true)
    void deleteAllByStoreId(Long nStoreId);

    @Query(value = "SELECT * FROM slot_template_detail\n  " +
            " WHERE n_loading_point_id IN (" +
            " SELECT n_loading_point_id FROM loading_point " +
            " WHERE n_store_id = :nStoreId )"
            , nativeQuery = true)
    List<SlotTemplateDetail> findAllByStoreId(Long nStoreId);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM slot_template_detail
            WHERE n_slot_template_id  IN (:ids)
            """, nativeQuery = true)
    void deleteAllByTitleIds(List<Long> ids);

}
