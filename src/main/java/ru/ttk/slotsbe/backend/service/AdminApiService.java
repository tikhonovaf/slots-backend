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
    public ResponseEntity<Void> addClientUser(ClientUserInDto clientUserInDto) {
        if (clientUserRepository.findByVcLogin(clientUserInDto.getVcLogin()).isPresent()) {
            throw ValidateException.exceptionSimple("There is a user with this login");
        }

        ClientUser clientUser = clientUserMapper.fromDtoToEntity(clientUserInDto);
        if (clientUserInDto.getVcPassword() != null) {
            clientUser.setVcPassword(sha512PasswordEncoder.encode(clientUserInDto.getVcPassword()));
        }

        clientUserRepository.save(clientUser);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> modifyClientUser(Long id, ClientUserInDto clientUserInDto) {
        Optional<ClientUser> existingUserOpt = clientUserRepository.findById(id);
        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ClientUser existingUser = existingUserOpt.get();

        Optional<ClientUser> userWithSameLogin = clientUserRepository.findByVcLogin(clientUserInDto.getVcLogin());
        if (userWithSameLogin.isPresent() && !userWithSameLogin.get().getNUserId().equals(id)) {
            throw ValidateException.exceptionSimple("There is a user with this login");
        }

        ClientUser updatedUser = clientUserMapper.fromDtoToEntity(clientUserInDto);
        CoreUtil.patch(updatedUser, existingUser);

        if (clientUserInDto.getVcPassword() != null) {
            existingUser.setVcPassword(sha512PasswordEncoder.encode(clientUserInDto.getVcPassword()));
        }

        clientUserRepository.save(existingUser);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<String>> clientUsersUpload(MultipartFile file) {
        List<String> messages = excelUploadService.saveClientUsersFromExcel(file);
        return ResponseEntity.ok(messages);
    }
}
