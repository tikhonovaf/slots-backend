package ru.ttk.slotsbe.backend.model;

import jakarta.persistence.*;
import lombok.Getter;

import org.hibernate.annotations.Immutable;

/**
 * Клиент
 */
@Entity
@Immutable
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
