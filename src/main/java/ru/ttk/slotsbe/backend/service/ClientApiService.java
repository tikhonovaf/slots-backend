package ru.ttk.slotsbe.backend.service;//package ru.ttk.slotsbe.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.ttk.slotsbe.backend.mapper.ClientMapper;
import ru.ttk.slotsbe.backend.repository.VClientRepository;
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
public class ClientApiService implements ClientsApiDelegate {

    @Autowired
    private VClientRepository vClientRepository;

    @Autowired
    private ClientMapper clientMapper;

    /**
     * Список клиентов
     */
    @Override
    public ResponseEntity<List<ClientDto>> getlients() {
        List<ClientDto>  result =
                vClientRepository
                            .findAll()
                            .stream()
                            .map(v -> clientMapper.fromViewToDto(v))
                            .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

}
