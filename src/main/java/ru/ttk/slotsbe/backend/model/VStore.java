package ru.ttk.slotsbe.backend.model;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Нефтебаза
 */
@Entity
@Getter
public class VStore {

    /**
     * Идентификатор
     */
    @Id
    Long nStoreId;

    /**
     * Код
     */
    String vcCode;

    /**
     * Наименование
     */
    String vcName;

}
