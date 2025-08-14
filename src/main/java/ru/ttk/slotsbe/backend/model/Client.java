package ru.ttk.slotsbe.backend.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Created by Tikhonov Arkadiy
 */
@Entity
@Getter
public class Client {

    /**
     * Идентификатор
     */
    @Id
    Long id;

    /**
     * Наименование
     */
    @NotNull
    String name;

}
