package ru.ttk.slotsbe.backend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.ttk.slotsbe.backend.model.LoadingPoint;

public interface LoadingPointRepository extends JpaRepository<LoadingPoint, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM loading_point\n  " +
            " WHERE n_store_id = :nStoreId "
            , nativeQuery = true)
    void deleteAllByStoreId(Long nStoreId);

}
