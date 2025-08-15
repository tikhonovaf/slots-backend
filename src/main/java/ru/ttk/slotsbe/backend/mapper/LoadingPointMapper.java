package ru.ttk.slotsbe.backend.mapper;

import org.springframework.stereotype.Service;
import ru.ttk.slotsbe.backend.dto.*;
import ru.ttk.slotsbe.backend.model.VClient;
import ru.ttk.slotsbe.backend.model.VLoadingPoint;
import ru.ttk.slotsbe.backend.util.CoreUtil;

/**
 * Маппинг:
 * -  между view и dto rest сервиса
 * -  между dto rest сервиса и entity сущности
 */
@Service
public class LoadingPointMapper {
    /**
     * Маппинг из View VClient в DTO
     *
     * @param view - строка из view VLoadingPoint
     * @return Данные в структуре DTO
     */
    public LoadingPointDto fromViewToDto(VLoadingPoint view) {
        LoadingPointDto result = new LoadingPointDto();
        CoreUtil.patch(view, result);

        return result;
    }


}
