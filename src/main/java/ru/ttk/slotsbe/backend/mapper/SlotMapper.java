package ru.ttk.slotsbe.backend.mapper;

import org.springframework.stereotype.Service;
import ru.ttk.slotsbe.backend.model.*;
import ru.ttk.slotsbe.backend.util.CoreUtil;
import ru.ttk.slotsbe.backend.dto.*;

import java.util.Optional;

/**
 * Маппинг:
 * -  между view и dto rest сервиса
 * -  между dto rest сервиса и entity сущности
 */
@Service
public class SlotMapper {
    /**
     * Маппинг из View SlotView в DTO
     *
     * @param view - строка из view SlotView
     * @return Данные в структуре DTO
     */
    public SlotDto fromViewToDto(SlotView view) {
        SlotDto result = new SlotDto();
        CoreUtil.patch(view, result);

        return result;
    }


}
