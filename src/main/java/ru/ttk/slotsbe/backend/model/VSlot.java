package ru.ttk.slotsbe.backend.model;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    LocalDateTime dStartTime;

    /**
     * Слот - Время окончания
     */
    LocalDateTime dEndTime;

    /**
     * Статус
     */
    String vcStatus;

    /**
     * Идентификатор пункта налива
     */
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
