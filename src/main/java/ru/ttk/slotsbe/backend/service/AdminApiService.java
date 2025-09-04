package ru.ttk.slotsbe.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ttk.slotsbe.backend.api.*;
import ru.ttk.slotsbe.backend.dto.*;
import ru.ttk.slotsbe.backend.exception.ValidateException;
import ru.ttk.slotsbe.backend.mapper.ClientUserMapper;
import ru.ttk.slotsbe.backend.model.ClientUser;
import ru.ttk.slotsbe.backend.repository.ClientUserRepository;
import ru.ttk.slotsbe.backend.security.Sha512PasswordEncoder;
import ru.ttk.slotsbe.backend.util.CoreUtil;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminApiService implements AdminApiDelegate {

    private final ClientUserRepository clientUserRepository;
    private final ExcelUploadService excelUploadService;
    private final ClientUserMapper clientUserMapper;
    private final Sha512PasswordEncoder sha512PasswordEncoder;

    @Override
    public ResponseEntity<Void> addClientUser(ClientUserInDto dto) {
        validateUniqueLogin(dto.getVcLogin(), null);

        ClientUser clientUser = clientUserMapper.fromDtoToEntity(dto);
        encodePasswordIfPresent(dto.getVcPassword(), clientUser);

        clientUserRepository.save(clientUser);
        return ResponseEntity.status(201).build(); // Явно указываем статус создания
    }

    @Override
    public ResponseEntity<Void> modifyClientUser(Long id, ClientUserInDto dto) {
        ClientUser existingUser = clientUserRepository.findById(id)
                .orElse(null);

        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        validateUniqueLogin(dto.getVcLogin(), id);

        ClientUser updatedUser = clientUserMapper.fromDtoToEntity(dto);
        CoreUtil.patch(updatedUser, existingUser);
        encodePasswordIfPresent(dto.getVcPassword(), existingUser);

        clientUserRepository.save(existingUser);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<String>> clientUsersUpload(MultipartFile file) {
        List<String> messages = excelUploadService.saveClientUsersFromExcel(file);
        return ResponseEntity.ok(messages);
    }

    // 🔒 Проверка уникальности логина
    private void validateUniqueLogin(String login, Long currentUserId) {
        clientUserRepository.findByVcLogin(login).ifPresent(existing -> {
            if (currentUserId == null || !existing.getNUserId().equals(currentUserId)) {
                throw ValidateException.exceptionSimple("There is a user with this login");
            }
        });
    }

    // 🔐 Хеширование пароля
    private void encodePasswordIfPresent(String rawPassword, ClientUser user) {
        if (rawPassword != null && !rawPassword.isBlank()) {
            user.setVcPassword(sha512PasswordEncoder.encode(rawPassword));
        }
    }
}
