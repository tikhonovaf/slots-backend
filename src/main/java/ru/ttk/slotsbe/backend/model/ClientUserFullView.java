package ru.ttk.slotsbe.backend.model;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Tikhonov Arkadiy
 */
@Entity
@Getter
public class ClientUserFullView {

    /**
     * Идентификатор
     */
    @Id
    Long id;

    /**
     * имя
     */
    String firstName;

    /**
     * Фамилия
     */
    String lastName;

    /**
     * Отчество
     */
    String secondName;

    /**
     * email
     */
    String email;

    /**
     * Дата начала действия
     */
    LocalDate startDate;

    /**
     * Дата окончания действия
     */
    LocalDate endDate;

    /**
     * client
     */
    Long clientId;

    /**
     * clientname
     */
    String clientName;

    /**
     * phone
     */
    String phone;

    /**
     * Логин
     */
    String login;

    /**
     * Активнгость
     */
    String activity;

}
