package ru.ttk.slotsbe.backend.dto;

import lombok.Data;

/**
 * DTO для описания фильтров
 *
 * @author Гончаров Егор
 */
@Data
public class SearchDto {
    /**
     * Наименование поля для фильтрации
     */
    private String field;

    /**
     * Значение поля(ей) для фильтрации
     */
    private String value;

    /**
     * Тип поля фильтрации
     */
    private SearchType type;

    /**
     * Конструктор
     * @param field - поле
     * @param value - значение
     * @param type - тип
     */
    public SearchDto(String field, String value, SearchType type) {
        this.field = field;
        this.value = value;
        this.type = type;
    }
}
