package ru.ttk.slotsbe.backend.service;//package ru.intelsource.s3traf.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.ttk.slotsbe.backend.api.AdminApiDelegate;
import ru.ttk.slotsbe.backend.dto.ClientUserInDto;
import ru.ttk.slotsbe.backend.exception.ValidateException;
import ru.ttk.slotsbe.backend.mapper.ClientUserMapper;
import ru.ttk.slotsbe.backend.model.ClientUser;
import ru.ttk.slotsbe.backend.repository.ClientUserRepository;
import ru.ttk.slotsbe.backend.security.Sha512PasswordEncoder;
import ru.ttk.slotsbe.backend.util.CoreUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Класс для выполнения функций rest сервисов (GET, POST, PATCH, DELETE)
 *
 * @author Аркадий Тихонов
 */
@Service
public class AdminApiService implements AdminApiDelegate {
    @Autowired
    private ClientUserRepository clientUserRepository;

    @Autowired
    private ClientUserMapper clientUserMapper;

    @Autowired
    private Sha512PasswordEncoder sha512PasswordEncoder;

    /**
     * Добавление пользователя
     *
     * @param clientUserInDto данные по миграции  (в виде массива)
     */
    @Override
    //  @UserAccess(action = ActionId.FULL, resource = ResourceId.ADMINISTRATION)
    public ResponseEntity<Void> addClientUser(ClientUserInDto clientUserInDto) {
        //  Проверяем уникальность логина
        Optional<ClientUser> optionalClientUser = clientUserRepository.findByVcLogin(clientUserInDto.getVcLogin());
        if (optionalClientUser.isPresent()) {
            throw ValidateException.exceptionSimple("There is a user with this login");
        }

        ClientUser clientUser = clientUserMapper.fromDtoToEntity(clientUserInDto);
        if (clientUserInDto.getVcPassword() != null) {
            clientUser.setVcPassword(sha512PasswordEncoder.encode(clientUserInDto.getVcPassword()));
        }

        clientUserRepository.save(clientUser);
        return ResponseEntity.noContent().build();

    }

    /**
     * Изменение пользователя
     */

    @Override
    //  @UserAccess(action = ActionId.FULL, resource = ResourceId.ADMINISTRATION)
    public ResponseEntity<Void> modifyClientUser(Long id, ClientUserInDto clientUserInDto) {
        if (clientUserRepository.findById(id).isPresent()) {
            //  Проверяем уникальность логина
            Optional<ClientUser> optionalClientUser = clientUserRepository.findByVcLogin(clientUserInDto.getVcLogin());
            if (optionalClientUser.isPresent()) {
                throw ValidateException.exceptionSimple("There is a user with this login");
            }

            ClientUser entity = clientUserRepository.findById(id).get();
            ClientUser entityNew = clientUserMapper.fromDtoToEntity(clientUserInDto);
            CoreUtil.patch(entityNew, entity);

            if (clientUserInDto.getVcPassword() != null) {
                entity.setVcPassword(sha512PasswordEncoder.encode(clientUserInDto.getVcPassword()));
            }
            clientUserRepository.save(entity);
        }
        return ResponseEntity.noContent().build();
    }

}
