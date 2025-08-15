package ru.ttk.slotsbe.backend.model;

import com.sun.istack.NotNull;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Клиент
 */
@Entity
@Getter
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
