package ru.ttk.slotsbe.backend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuditActionId {
    CREATE(1l),
    MODIFY(2l),
    DELETE(3l),
    ;

    private Long id;
}
