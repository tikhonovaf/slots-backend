package ru.ttk.slotsbe.backend.service;//package ru.ttk.slotsbe.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.ttk.slotsbe.backend.api.StoresApiDelegate;
import ru.ttk.slotsbe.backend.dto.StoreDto;
import ru.ttk.slotsbe.backend.mapper.StoreMapper;
import ru.ttk.slotsbe.backend.repository.VStoreRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для выполнения функций rest сервисов (GET, POST, PATCH, DELETE)
 *
 * @author Аркадий Тихонов
 */
@Service
@Slf4j
public class StoreApiService implements StoresApiDelegate {

    @Autowired
    private VStoreRepository vStoreRepository;

    @Autowired
    private StoreMapper StoreMapper;

    /**
     * Список нефтебаз
     */
    @Override
    public ResponseEntity<List<StoreDto>> getStores() {
        List<StoreDto>  result =
                vStoreRepository
                            .findAll()
                            .stream()
                            .map(v -> StoreMapper.fromViewToDto(v))
                            .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

}
