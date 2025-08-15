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
public class Slot {

    /**
     * Идентификатор
     */
    @Id
    Long nSlotId;

    /**
     * Дата слота
     */
    LocalDate slotDate;

    /**
     * Идентификатор пункта налива
     */
    Long nLoadingPointId;

    /**
     * Идентификатор клиента
     */
    Long nClientId;

    /**
     * Слот - Время начала
     */
    LocalDateTime dStartTime;

    /**
     * Слот - Время окончания
     */
    LocalDateTime dEndTime;

    /**
     * Статус
     */
    String vcStatus;

}
