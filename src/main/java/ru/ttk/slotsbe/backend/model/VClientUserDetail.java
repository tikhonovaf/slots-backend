package ru.ttk.slotsbe.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Created by Tikhonov Arkadiy
 */
@Entity
@Getter
@Table(name = "v_client_user_detail")
public class VClientUserDetail {

    /**
     * Идентификатор
     */
    @Id
    @Column(name = "n_user_id")
    Long nUserId;

    /**
     * client
     */

    @Column(name = "n_client_id")
    Long nClientId;

    /**
     * Код клиента
     */
    String vcClientCode;

    /**
     * имя
     */
    String vcFirstName;

    /**
     * Фамилия
     */
    String vcLastName;

    /**
     * Отчество
     */
    String vcSecondName;

    /**
     * Логин
     */
    String vcLogin;

    /**
     * email
     */
    String vcEmail;

    /**
     * phone
     */
    String vcPhone;


}
