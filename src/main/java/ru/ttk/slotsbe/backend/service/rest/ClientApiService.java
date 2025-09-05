package ru.ttk.slotsbe.backend.service.rest;//package ru.ttk.slotsbe.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.ttk.slotsbe.backend.api.ClientsApiDelegate;
import ru.ttk.slotsbe.backend.dto.ClientDto;
import ru.ttk.slotsbe.backend.dto.ClientUsersDto;
import ru.ttk.slotsbe.backend.mapper.ClientMapper;
import ru.ttk.slotsbe.backend.repository.VClientRepository;
import ru.ttk.slotsbe.backend.repository.VClientUserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для выполнения функций rest сервисов (GET, POST, PATCH, DELETE)
 *
 * @author Аркадий Тихонов
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ClientApiService implements ClientsApiDelegate {

    private  final VClientRepository vClientRepository;
    private  final VClientUserRepository vClientUserRepository;
    private  final ClientMapper clientMapper;

    /**
     * GET /clients : Выборка списка клиентов
     *
     * @return Список клиентов (status code 200)
     */
    @Override
    public ResponseEntity<List<ClientDto>> getClients() {
        List<ClientDto> result =
                vClientRepository
                        .findAll()
                        .stream()
                        .map(clientMapper::fromViewToDto)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * GET /clients/users : Выборка списка пользователей клиентов
     *
     * @return Список пользователей клиентов (status code 200)
     */
    public ResponseEntity<List<ClientUsersDto>> getClientsUsers() {

        List<ClientUsersDto> result =
                vClientUserRepository
                        .findAll()
                        .stream()
                        .map(clientMapper::fromViewToDto)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(result);

    }
}
