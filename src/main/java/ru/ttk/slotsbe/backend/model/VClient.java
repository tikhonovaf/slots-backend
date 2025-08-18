package ru.ttk.slotsbe.backend.model;

import jakarta.persistence.Column;
import lombok.Getter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Клиент
 */
@Entity
@Getter
@Table(name = "v_client")
public class VClient {

    /**
     * Идентификатор
     */
    @Id
    @Column(name = "n_client_id")
    Long nClientId;

    /**
     * Код
     */
    String vcCode;

    /**
     * Наименование
     */
    String vcName;

}
