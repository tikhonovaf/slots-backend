package ru.ttk.slotsbe.backend.dto;

/**
 * Типы данных для формирования фильтров
 *
 * @author Гончаров Егор
 */
public enum SearchType {
    /**
     * ИД
     */
    ID,

    /**
     * Список строковых значений
     */
    STRING_LIST,

    /**
     * Строка
     */
    STRING,

    /**
     * Дата ПО
     */
    DATE_TO,

    /**
     * Дата С
     */
    DATE_FROM,

    /**
     * Дата ПО (для LocalDate)
     */
    LOCAL_DATE_TO,

    /**
     * Дата С (для LocalDate)
     */
    LOCAL_DATE_FROM,

    /**
     * Дата С
     */
    LOCAL_DATE_TIME_FROM,

    /**
     * Дата ПО
     */
    LOCAL_DATE_TIME_TO,
    /**
     * Число ОТ
     */
    NUMBER_FROM,

    /**
     * Число ДО (включая)
     */
    NUMBER_TO,

    /**
     * Число =
     */
    NUMBER_EQUAL

}
