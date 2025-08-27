package ru.ttk.slotsbe.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO для запроса авторизации.
 */
public record LoginRequest(

        @JsonProperty("username")
        @NotBlank(message = "Имя пользователя не должно быть пустым")
        String username,

        @JsonProperty("password")
        @NotBlank(message = "Пароль не должен быть пустым")
        @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
        String password
) {}
