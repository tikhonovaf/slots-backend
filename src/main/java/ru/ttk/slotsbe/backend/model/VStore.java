package ru.ttk.slotsbe.backend.model;

import jakarta.persistence.Column;
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

}
