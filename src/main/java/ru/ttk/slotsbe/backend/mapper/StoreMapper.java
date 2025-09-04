package ru.ttk.slotsbe.backend.mapper;

import org.springframework.stereotype.Service;
import ru.ttk.slotsbe.backend.dto.StoreDto;
import ru.ttk.slotsbe.backend.model.VStore;
import ru.ttk.slotsbe.backend.util.CoreUtil;

/**
 * Маппинг:
 * -  между view и dto rest сервиса
 * -  между dto rest сервиса и entity сущности
 */
@Service
public class StoreMapper {
    /**
     * Маппинг из View VStore в DTO
     *
     * @param view - строка из view SlotView
     * @return Данные в структуре DTO
     */
    public StoreDto fromViewToDto(VStore view) {
        StoreDto result = new StoreDto();
        CoreUtil.patch(view, result);

        return result;
    }


}
