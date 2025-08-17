package ru.ttk.slotsbe.backend.model;

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
