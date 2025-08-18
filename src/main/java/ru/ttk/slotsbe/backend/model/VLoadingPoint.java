package ru.ttk.slotsbe.backend.model;

import jakarta.persistence.Column;
import lombok.Getter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Пункт налива
 */
@Entity
@Getter
@Table(name = "v_loading_point")
public class VLoadingPoint {

    /**
     * Идентификатор
     */
    @Id
    Long n_loading_point_id;

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

    /**
     * Идентификатор нефтебазы
     */
    @Column(name = "n_store_id")
    Long nStoreId;

    /**
     * Код нефтебазы
     */
    String vcStoreCode;

    /**
     * Наименование нефтебазы
     */
    String vcStoreName;

}
