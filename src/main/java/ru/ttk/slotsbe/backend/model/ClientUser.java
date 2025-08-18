package ru.ttk.slotsbe.backend.model;

import lombok.Getter;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Created by Tikhonov Arkadiy
 */
@Entity
@Getter
public class ClientUser {

    /**
     * Идентификатор
     */
    @Id
    @Column(name = "n_user_id")
    Long nUserId;

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
     * Пароль
     */
    String vcPassword;

    /**
     * Дата начала действия
     */
    LocalDate dStartDate;

    /**
     * Дата окончания действия
     */
    LocalDate dEndDate;

    /**
     * client
     */

    @Column(name = "n_client_id")
    Long nClientId;

    /**
     * email
     */
    String vcEmail;

    /**
     * phone
     */
    String vcPhone;


}
