package ru.ttk.slotsbe.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

/**
 * Статусы слотов
 */
@Entity
@Immutable
@Getter
@Table(name = "slot_status")
public class SlotStatus {

    /**
     * Идентификатор
     */
    @Id
    @Column(name = "n_status_Id")
    Long nStatusId;

    /**
     * Код
     */
    String vcCode;

    /**
     * Наименование
     */
    String vcName;

}
