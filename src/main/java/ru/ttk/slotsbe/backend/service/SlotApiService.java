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
import java.util.Date;
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
    private SlotRepository slotRepository;

    @Autowired
    private SlotMapper slotMapper;

    /**
     * Список слотов
     */
    @Override
    public ResponseEntity<List<SlotDto>> getSlotsByFilters(SlotSearchFilter slotSearchFilter) {
//      Формирование выборки из View по заданным фильтрам уточненное - тест
        List<SlotDto> result =
                vSlotRepository
                        .findAllByFilter(slotSearchFilter.getnStoreId(), slotSearchFilter.getnClientId(),
                                slotSearchFilter.getVcStatus(), slotSearchFilter.getdDate())
                        .stream()
                        .map(v -> slotMapper.fromViewToDto(v))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(result);

    }

    /**
     * Установка для слота статуса и указание клиента
     */
    @Override
    public ResponseEntity<Void> reserveSlots(List<ModifiedSlotDto> modifiedSlotDtos) {

        for (ModifiedSlotDto slot : modifiedSlotDtos) {
            if (slotRepository.findById(slot.getnSlotId()).isPresent()) {
                Slot modifiedSlot = slotRepository.findById(slot.getnSlotId()).get();
                modifiedSlot.setVcStatus(slot.getVcStatus());
                modifiedSlot.setNClientId(slot.getnClientId());
                slotRepository.save(modifiedSlot);
            }
        }
        return ResponseEntity.noContent().build();
    }

}
