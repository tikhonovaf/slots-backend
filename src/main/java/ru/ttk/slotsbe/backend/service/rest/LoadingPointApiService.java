package ru.ttk.slotsbe.backend.service.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ttk.slotsbe.backend.api.LoadingPointsApiDelegate;
import ru.ttk.slotsbe.backend.dto.LoadingPointDto;
import ru.ttk.slotsbe.backend.mapper.LoadingPointMapper;
import ru.ttk.slotsbe.backend.repository.VLoadingPointRepository;
import ru.ttk.slotsbe.backend.service.excel.ExcelUploadService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для обработки REST-запросов, связанных с пунктами налива.
 *
 * @author Аркадий Тихонов
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LoadingPointApiService implements LoadingPointsApiDelegate {

    private final VLoadingPointRepository vLoadingPointRepository;
    private final LoadingPointMapper loadingPointMapper;
    private final ExcelUploadService excelUploadService;

    /**
     * Получает список пунктов налива по ID склада.
     *
     * @param storeId ID склада
     * @return Список DTO пунктов налива или соответствующий HTTP статус
     */
    @Override
    public ResponseEntity<List<LoadingPointDto>> getLoadingPoints(Long storeId) {
        if (storeId == null) {
            log.warn("Получен запрос с пустым storeId");
            return ResponseEntity.badRequest().build();
        }

        List<LoadingPointDto> result = vLoadingPointRepository
                .findAllByStoreId(storeId)
                .stream()
                .map(loadingPointMapper::fromViewToDto)
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            log.info("Пункты налива не найдены для storeId {}", storeId);
            return ResponseEntity.noContent().build();
        }

        log.info("Найдено {} пунктов налива для storeId {}", result.size(), storeId);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /loadingPoints/upload : Загрузка файла c данными по пунктам налива
     *
     * @param file Файл для загрузки (optional)
     * @return Список сообщений о загрузке (status code 200)
     */
    @Override
    public  ResponseEntity<List<String>> loadingPointsUpload(MultipartFile file)  {
        List<String> messages = null;
        try {
            messages = excelUploadService.saveLoadingPointsFromExcel(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(messages);

    }


}
