package ru.ttk.slotsbe.backend.model;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Tikhonov Arkadiy
 */
@Entity
@Getter
public class SlotView {

    /**
     * Идентификатор
     */
    @Id
    Long id;

    /**
     * Статус
     */
    String status;

    /**
     * Адрес
     */

    /**
     * Клиент - Идентификатор
     */
    Long clientId;

    /**
     * Клиент - Наименование
     */
    String clientName;

    /**
     * Дата слота
     */
    LocalDate slotDate;

    /**
     * Слот - Дата начала
     */
    LocalDateTime slotDateTimeBegin;

    /**
     * Слот - Дата окончания
     */
    LocalDateTime slotDateTimeEnd;

}
