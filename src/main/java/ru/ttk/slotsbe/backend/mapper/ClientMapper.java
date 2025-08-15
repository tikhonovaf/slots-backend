package ru.ttk.slotsbe.backend.mapper;

import org.springframework.stereotype.Service;
import ru.ttk.slotsbe.backend.dto.ClientDto;
import ru.ttk.slotsbe.backend.model.VClient;
import ru.ttk.slotsbe.backend.model.VSlot;
import ru.ttk.slotsbe.backend.util.CoreUtil;

/**
 * Маппинг:
 * -  между view и dto rest сервиса
 * -  между dto rest сервиса и entity сущности
 */
@Service
public class ClientMapper {
    /**
     * Маппинг из View VClient в DTO
     *
     * @param view - строка из view SlotView
     * @return Данные в структуре DTO
     */
    public ClientDto fromViewToDto(VClient view) {
        ClientDto result = new ClientDto();
        CoreUtil.patch(view, result);

        return result;
    }


}
