package ru.ttk.slotsbe.backend.service;//package ru.ttk.slotsbe.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.ttk.slotsbe.backend.mapper.LoadingPointMapper;
import ru.ttk.slotsbe.backend.repository.VLoadingPointRepository;
import ru.ttk.slotsbe.backend.api.*;
import ru.ttk.slotsbe.backend.dto.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для выполнения функций rest сервисов (GET, POST, PATCH, DELETE)
 *
 * @author Аркадий Тихонов
 */
@Service
@Slf4j
public class LoadingPointApiService implements LoadingPointsApiDelegate {

    @Autowired
    private VLoadingPointRepository vLoadingPointRepository;

    @Autowired
    private LoadingPointMapper loadingPointMapper;

    /**
     * Список пунктов налива
     */
    @Override
    public ResponseEntity<List<LoadingPointDto>> getLoadingPoints(Long storeId) {
        List<LoadingPointDto>  result =
                vLoadingPointRepository
                            .findAllByStoreId(storeId)
                            .stream()
                            .map(v -> loadingPointMapper.fromViewToDto(v))
                            .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

}
