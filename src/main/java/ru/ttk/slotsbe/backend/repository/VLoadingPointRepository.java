package ru.ttk.slotsbe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ttk.slotsbe.backend.model.ClientUser;
import ru.ttk.slotsbe.backend.model.LoadingPoint;
import ru.ttk.slotsbe.backend.model.VLoadingPoint;

import java.util.List;

public interface VLoadingPointRepository extends JpaRepository<VLoadingPoint, Long> {
    @Query(value = "SELECT * FROM v_loading_point\n" +
            "WHERE vc_store_code = :storeCode  AND vc_code = :loadingPointCode "
            , nativeQuery = true)
    List<VLoadingPoint> findAllByStoreCodeAndLoadingPointCode(String  storeCode, String loadingPointCode);
}
