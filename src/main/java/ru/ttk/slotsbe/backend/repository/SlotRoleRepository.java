package ru.ttk.slotsbe.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ttk.slotsbe.backend.model.SlotRole;

import java.util.Optional;


public interface SlotRoleRepository extends JpaRepository<SlotRole, Long> {
    Optional <SlotRole> findByVcCode(String code);

    @Query(value = """
    SELECT * FROM slot_role
      WHERE n_role_id = :nRoleId
    """, nativeQuery = true)

    Optional <SlotRole> findByRoleId(Long nRoleId);
}
