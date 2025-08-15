package ru.ttk.slotsbe.backend.model;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Пункт налива
 */
@Entity
@Getter
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
    String vcСomment;

    /**
     * Идентификатор нефтебазы
     */
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
