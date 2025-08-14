package ru.ttk.slotsbe.backend.dto;

import lombok.Data;

import java.util.List;

/**
 * DTO  постраничной выборки данных
 * @param <T> - тип элемента строки страницы
 */
@Data
public class PageDto<T> {
    /**
     * Поля выборки
     */
    List<T> elements;

    /**
     * Количество элементов ввыборке
     */
    Long total;
}
