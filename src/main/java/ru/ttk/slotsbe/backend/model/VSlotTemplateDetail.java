package ru.ttk.slotsbe.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Tikhonov Arkadiy
 */
@Entity
@Getter
@Immutable
@Table(name = "v_slot_template_detail")
public class VSlotTemplateDetail {

    /**
     * Идентификатор
     */
    @Id
    @Column(name = "n_slot_template_line_id")
    Long nSlotTemplateLineId;

    /**
     * Идентификатор шаблона
     */
    @Column(name = "n_slot_template_id")
    Long nSlotTemplateId;

    /**
     * Идентификатор пункта налива
     */
    @Column(name = "n_loading_point_id")
    Long nLoadingPointId;

    /**
     * Код пункта налива
     */
    @Column(name = "vc_loading_point_code")
    String vcLoadingPointCode;

    /**
     * Код нефтебазы
     */
    @Column(name = "vc_store_code")
    String vcStoreCode;

    /**
     * Тип строки
     */
    @Column(name = "vc_type")
    String vcType;

    /**
     * Дата слота
     */
    @Column(name = "d_date")
    LocalDate dDate;

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
