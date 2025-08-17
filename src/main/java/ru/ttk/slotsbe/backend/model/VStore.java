package ru.ttk.slotsbe.backend.model;

import lombok.Getter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
