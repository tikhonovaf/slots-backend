package ru.ttk.slotsbe.backend.model;

import jakarta.persistence.Column;
import lombok.Getter;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by Tikhonov Arkadiy
 */
@Entity
@Getter
@Table(name = "v_slot")
public class VSlot {


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
     * Слот - Время начала
     */
    LocalTime dStartTime;

    /**
     * Слот - Время окончания
     */
    LocalTime dEndTime;

    /**
     * Статус
     */
    String vcStatus;

    /**
     * Идентификатор пункта налива
     */
    @Column(name = "n_loading_point_id")
    Long nLoadingPointId;

    /**
     * Код пункта налива
     */
    String vcLoadingPointCode;

    /**
     * Наименование пункта налива
     */
    String vcLoadingPointName;

    /**
     * Комментарий пункта налива
     */
    String vcLoadingPointComment;

    /**
     * Идентификатор нефтебазы
     */
    @Column(name = "n_store_id")
    Long nStoreId;

    /**
     * Код нефтебазы
     */
    String vcStoreCode;

    /**
     * Наименование нефтебазы
     */
    String vcStoreName;
    /**
     * Идентификатор клиента
     */
    @Column(name = "n_client_id")
    Long nClientId;

    /**
     * Код клиента
     */
    String vcClientCode;

    /**
     * Наименование клиента
     */
    String vcClientName;

}
