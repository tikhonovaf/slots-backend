package ru.ttk.slotsbe.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.ttk.slotsbe.backend.api.SessionApiDelegate;
import ru.ttk.slotsbe.backend.dto.SessionDto;
import ru.ttk.slotsbe.backend.mapper.SessionMapper;
import ru.ttk.slotsbe.backend.model.ClientUser;
import ru.ttk.slotsbe.backend.model.SlotRole;
import ru.ttk.slotsbe.backend.repository.ClientUserRepository;
import ru.ttk.slotsbe.backend.repository.SlotRoleRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionApiService implements SessionApiDelegate {

    private final ClientUserRepository clientUserRepository;
    private final SlotRoleRepository slotRoleRepository;
    private final SessionMapper sessionMapper;

    @Override
    public ResponseEntity<SessionDto> session() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String login = auth.getName();
        Optional<ClientUser> userOpt = clientUserRepository.findByVcLogin(login);
        if (userOpt.isEmpty()) {
            return ResponseEntity.ok(sessionMapper.notActual());
        }

        ClientUser user = userOpt.get();
        SlotRole role = null;
        if (user.getNRoleId() != null) {
            role = slotRoleRepository.findById(user.getNRoleId()).orElse(null);
        }

        SessionDto dto = sessionMapper.toDto(user, role);
        return ResponseEntity.ok(dto);
    }
}
