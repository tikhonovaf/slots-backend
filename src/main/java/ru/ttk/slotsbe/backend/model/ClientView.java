package ru.ttk.slotsbe.backend.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
//// import javax.validation.constraints.NotNull;

/**
 * Created by Tikhonov Arkadiy
 */
@Entity
@Getter
@Setter
public class ClientView {

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
