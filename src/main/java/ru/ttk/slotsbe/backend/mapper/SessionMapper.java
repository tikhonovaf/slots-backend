package ru.ttk.slotsbe.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ttk.slotsbe.backend.dto.SessionDto;
import ru.ttk.slotsbe.backend.model.ClientUser;
import ru.ttk.slotsbe.backend.model.SlotRole;

@Mapper(componentModel = "spring")
public interface SessionMapper {

    @Mapping(target = "actual", constant = "true")
    @Mapping(source = "user.vcLogin", target = "username")
    @Mapping(source = "user.vcLastName", target = "name")
    @Mapping(source = "user.NRoleId", target = "roleId")
    @Mapping(source = "role.vcCode", target = "role")
    SessionDto toDto(ClientUser user, SlotRole role);

    default SessionDto notActual() {
        SessionDto dto = new SessionDto();
        dto.setActual(false);
        return dto;
    }
}
