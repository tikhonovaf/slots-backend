package ru.ttk.slotsbe.backend.repository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.ttk.slotsbe.backend.model.ClientUser;
import ru.ttk.slotsbe.backend.model.VSlot;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ClientUserRepository extends JpaRepository<ClientUser, Long> {
    Optional<ClientUser> findByVcLogin(String login);

    @Query(value = "SELECT * FROM client_user\n" +
            "WHERE (:ids IS NULL OR n_user_id IN (:ids) )"
            , nativeQuery = true)

    List<ClientUser> findAllByNUserIds(List <Long> ids);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM client_user\n  " +
            " WHERE n_client_id = :nClientId "
            , nativeQuery = true)
    void deleteAllByClientId(Long nClientId);

}
