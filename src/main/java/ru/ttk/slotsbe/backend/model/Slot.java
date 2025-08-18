package ru.ttk.slotsbe.backend.model;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Tikhonov Arkadiy
 */
@Entity
@Getter
@Setter
public class Slot {

    /**
     * Идентификатор
     */
    @Id
    Long nSlotId;

    /**
     * Дата слота
     */
    LocalDate dDate;

    /**
     * Идентификатор пункта налива
     */
    @Column(name = "n_loading_point_id")
    Long nLoadingPointId;

    /**
     * Идентификатор клиента
     */
    @Column(name = "n_client_id")
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
