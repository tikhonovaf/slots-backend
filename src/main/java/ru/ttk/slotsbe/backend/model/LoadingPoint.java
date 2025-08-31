package ru.ttk.slotsbe.backend.model;

import jakarta.persistence.*;
import lombok.Getter;

import lombok.Setter;

/**
 * Пункт налива
 */
@Entity
@Getter
@Setter
public class LoadingPoint {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "slot_seq")
    @SequenceGenerator(name = "slot_seq", sequenceName = "slot_seq", allocationSize = 1)
    Long n_loading_point_id;

    /**
     * Идентификатор нефтебазы
     */
    @Column(name = "n_store_id")
    Long nStoreId;

    /**
     * Код
     */
    String vcCode;

    /**
     * Наименование
     */
    String vcName;

    /**
     * Комментарий
     */
    String vcComment;

}
