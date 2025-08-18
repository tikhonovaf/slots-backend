package ru.ttk.slotsbe.backend.model;

import jakarta.persistence.Column;
import lombok.Getter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Пункт налива
 */
@Entity
@Getter
public class LoadingPoint {

    /**
     * Идентификатор
     */
    @Id
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
    String vcСomment;

}
