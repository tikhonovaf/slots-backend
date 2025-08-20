package ru.ttk.slotsbe.backend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.ttk.slotsbe.backend.model.SlotTemplate;

import java.util.List;

public interface SlotTemplateRepository extends JpaRepository<SlotTemplate, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM slot_template\n  " +
            " WHERE n_loading_point_id IN (" +
            " SELECT n_loading_point_id FROM loading_point " +
            " WHERE n_store_id = :nStoreId )"
            , nativeQuery = true)
    void deleteAllByStoreId(Long storeId);

    @Query(value = "SELECT * FROM slot_template\n  " +
            " WHERE n_loading_point_id IN (" +
            " SELECT n_loading_point_id FROM loading_point " +
            " WHERE n_store_id = :nStoreId )"
            , nativeQuery = true)
    List<SlotTemplate> findAllByStoreId(Long nStoreId);

}
