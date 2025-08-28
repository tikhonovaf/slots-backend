package ru.ttk.slotsbe.backend.model;

import lombok.Getter;

import jakarta.persistence.*;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;

/**
 * Created by Tikhonov Arkadiy
 */
@Entity
@Getter
@Setter
public class ClientUser {

    /**
     * Идентификатор
     */
    @Id
    @Column(name = "n_user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "slot_seq")
    @SequenceGenerator(name = "slot_seq", sequenceName = "slot_seq", allocationSize = 1)
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
    @Column(name = "d_start_date")
    LocalDate dStartDate;

    /**
     * Дата окончания действия
     */
    @Column(name = "d_end_date")
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

    /**
     * role
     */

    @Column(name = "n_role_id")
    Long nRoleId;

}
