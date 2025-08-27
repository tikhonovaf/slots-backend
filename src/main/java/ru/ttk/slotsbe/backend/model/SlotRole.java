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
@Table(name = "slot_role")
public class SlotRole {

    /**
     * Идентификатор
     */
    @Id
    @Column(name = "n_role_Id")
    Long nRoleId;

    /**
     * Код
     */
    String vcCode;

    /**
     * Наименование
     */
    String vcName;

}
