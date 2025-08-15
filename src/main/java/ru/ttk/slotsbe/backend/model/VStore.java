package ru.ttk.slotsbe.backend.model;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Нефтебаза
 */
@Entity
@Getter
@Table(name = "v_store")
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
