package ru.ttk.slotsbe.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.ttk.slotsbe.backend.model.ClientUser;
import ru.ttk.slotsbe.backend.model.SlotRole;
import ru.ttk.slotsbe.backend.repository.ClientUserRepository;
import ru.ttk.slotsbe.backend.api.*;
import ru.ttk.slotsbe.backend.dto.*;
import ru.ttk.slotsbe.backend.repository.SlotRoleRepository;

import java.util.Optional;

@Service
public class SessionApiService implements SessionApiDelegate {
    @Autowired
    private ClientUserRepository clientUserRepository;
    @Autowired
    private SlotRoleRepository slotRoleRepository;

    @Override
    public ResponseEntity<SessionDto> session() {
        SessionDto result = new SessionDto();

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<ClientUser> userOptional = clientUserRepository.findByVcLogin(login);
        if (!userOptional.isPresent()) {
            result.setActual(false);
        } else {
            ClientUser user = userOptional.get();

            result.setActual(true);
            result.setUsername(login);
            result.setName(user.getVcLastName());
            if (user.getNRoleId() != null) {
                Optional <SlotRole> roleOptional = slotRoleRepository.findById(user.getNRoleId());
                if (roleOptional.isPresent()) {
                    SlotRole role = roleOptional.get();
                    result.setRoleId(user.getNRoleId());
                    result.setRole(role.getVcCode());
                }
            }
        }

        return ResponseEntity.ok(result);
    }
}
