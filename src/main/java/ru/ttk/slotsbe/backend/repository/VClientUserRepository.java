package ru.ttk.slotsbe.backend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.ttk.slotsbe.backend.model.VClientUser;

import java.util.List;
import java.util.Optional;

public interface VClientUserRepository extends JpaRepository<VClientUser, Long> {

}
