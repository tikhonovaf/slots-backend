package ru.ttk.slotsbe.backend.model;

import com.sun.istack.NotNull;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
