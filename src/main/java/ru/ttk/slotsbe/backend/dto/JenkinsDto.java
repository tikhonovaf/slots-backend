package ru.ttk.slotsbe.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JenkinsDto {
    private Long id;
    private String taskUrl;
}
