package ru.ttk.slotsbe.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * Created by Tikhonov Arkadiy
 */
@Entity
@Getter
@Setter
public class SlotTemplateTitle {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "slot_seq")
    @SequenceGenerator(name = "slot_seq", sequenceName = "slot_seq", allocationSize = 1)
    Long nSlotTemplateId;

    /**
     * Имя шаблона
     */
    @Column(name = "vc_name")
    String vcName;

}
