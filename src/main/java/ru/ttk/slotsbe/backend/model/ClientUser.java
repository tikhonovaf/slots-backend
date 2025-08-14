package ru.ttk.slotsbe.backend.model;

import lombok.Getter;

import javax.persistence.*;
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
     * Логин
     */
    String login;

    /**
     * Пароль
     */
    String password;

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
    @ManyToOne
    Client client;

    /**
     * email
     */
    String email;

    /**
     * phone
     */
    String phone;


}
