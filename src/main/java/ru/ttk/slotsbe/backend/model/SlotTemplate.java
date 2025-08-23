package ru.ttk.slotsbe.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by Tikhonov Arkadiy
 */
@Entity
@Getter
@Setter
public class SlotTemplate {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "slot_seq")
    @SequenceGenerator(name = "slot_seq", sequenceName = "slot_seq", allocationSize = 1)
    Long nSlotTemplateId;

    /**
     * Идентификатор пункта налива
     */
    @Column(name = "n_loading_point_id")
    Long nLoadingPointId;

    /**
     * Слот - Время начала
     */
    LocalTime dStartTime;

    /**
     * Слот - Время окончания
     */
    LocalTime dEndTime;

    /**
     * Идентификатор статиса
     */
    @Column(name = "n_status_Id")
    Long nStatusId;

}
