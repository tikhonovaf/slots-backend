package ru.ttk.slotsbe.backend.service;//package ru.ttk.slotsbe.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.ttk.slotsbe.backend.dto.*;
import ru.ttk.slotsbe.backend.mapper.SlotMapper;
import ru.ttk.slotsbe.backend.model.*;
import ru.ttk.slotsbe.backend.repository.*;
import ru.ttk.slotsbe.backend.api.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для выполнения функций rest сервисов (GET, POST, PATCH, DELETE)
 *
 * @author Аркадий Тихонов
 */
@Service
@Slf4j
public class SlotApiService implements SlotsApiDelegate {

    @Autowired
    private VSlotRepository vSlotRepository;

    @Autowired
    private SlotMapper slotMapper;

    /**
     * Список слотов
     */
    @Override
    public ResponseEntity<List<SlotDto>> getSlotsByFilters(SlotSearchFilter slotSearchFilter) {

//      Формирование выборки из View по заданным фильтрам уточненное - тест
        List<SlotDto>  result =
                vSlotRepository
                        .findAll()
                        .stream()
                        .map(v -> slotMapper.fromViewToDto(v))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(result);

    }

    /**
     * Установка для слота статуса и указание клиента
     *
     */
    @Override
    public ResponseEntity<List<SlotDto>> modifySlots(List<ModifiedSlotDto> modifiedSlotDto) {
//        if (slotRepository.findById(id).isPresent()) {
//            Slot entity = slotRepository.findById(id).get();
//            entity.setActual(false);
//            slotRepository.save(entity);
//            //  Аудит
//            auditService.saveAudit(ResourceId.CLUSTER.getId(), AuditActionId.DELETE.getId(), "Кластер " + entity.getName(),
//                    entity.getId(), entity.getName());
//        }

        return ResponseEntity.noContent().build();
    }

}
