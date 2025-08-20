//package ru.ttk.slotsbe.backend.service;//package ru.ttk.slotsbe.backend.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import ru.ttk.slotsbe.backend.api.LoadingPointsApiDelegate;
//import ru.ttk.slotsbe.backend.api.LoadingPointsApiDelegate;
//import ru.ttk.slotsbe.backend.dto.LoadingPointDto;
//import ru.ttk.slotsbe.backend.mapper.LoadingPointMapper;
//import ru.ttk.slotsbe.backend.repository.VLoadingPointRepository;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * Класс для выполнения функций rest сервисов (GET, POST, PATCH, DELETE)
// *
// * @author Аркадий Тихонов
// */
//@Service
//@Slf4j
//public class LoadingPointApiService implements LoadingPointsApiDelegate {
//
//    @Autowired
//    private VLoadingPointRepository vLoadingPointRepository;
//
//    @Autowired
//    private LoadingPointMapper LoadingPointMapper;
//
//    /**
//     * Список пунктов налива
//     */
//    @Override
//    public ResponseEntity<List<LoadingPointDto>> getLoadingPoints() {
//        List<LoadingPointDto>  result =
//                vLoadingPointRepository
//                            .findAll()
//                            .stream()
//                            .map(v -> LoadingPointMapper.fromViewToDto(v))
//                            .collect(Collectors.toList());
//
//        return ResponseEntity.ok(result);
//    }
//
//}
