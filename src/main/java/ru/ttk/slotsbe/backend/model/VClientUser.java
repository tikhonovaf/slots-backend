package ru.ttk.slotsbe.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Created by Tikhonov Arkadiy
 */
@Entity
@Getter
@Table(name = "v_client_user")
public class VClientUser {

    /**
     * Идентификатор
     */
    @Id
    @Column(name = "n_user_id")
    Long nUserId;

    /**
     * Наименование клиента
     */
    String vcInfo;


    /**
     * email
     */
    String vcEmail;


}
