package ru.ttk.slotsbe.backend.model;

import jakarta.persistence.Column;
import lombok.Getter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

/**
 * Пункт налива
 */
@Entity
@Immutable
@Getter
@Table(name = "v_loading_point")
public class VLoadingPoint {

    /**
     * Идентификатор
     */
    @Id
    @Column(name = "n_loading_point_id")
    Long nLoadingPointId;

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
