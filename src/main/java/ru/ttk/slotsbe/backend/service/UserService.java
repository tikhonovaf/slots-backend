package ru.ttk.slotsbe.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.ttk.slotsbe.backend.model.ClientUser;
import ru.ttk.slotsbe.backend.model.VClient;
import ru.ttk.slotsbe.backend.repository.ClientUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private ClientUserRepository clientUserRepository;

    /**
     * Определение текущего пользователя
     */
    public ClientUser getCurrentUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        SecurityContextImpl sci = (SecurityContextImpl) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        ClientUser user;
        if (sci != null && sci.getAuthentication() != null) {
            Optional<ClientUser> usrOptional = clientUserRepository.findByVcLogin(sci.getAuthentication().getName());
            if (usrOptional.isPresent()) {
                user = usrOptional.get();
                return user;
            }
        }

        return null;
    }


    /**
     * Определение организации текущего пользователя
     */
    public VClient getCurrentUserClient() {
        ClientUser clientUser = getCurrentUser();
//        if (clientUser != null) {
//            return clientUser.getOrg();
//        } else

        return null;
    }

    /**
     * Определение признака истечения срока пароля для пользователя
     */
    public Boolean getExpired() {
        Boolean expired = false;
        ClientUser user = getCurrentUser();
        if (user == null) {
            return expired;
        }
        LocalDate currentDate = LocalDate.now();
//        if ((user.getStartDate() != null && currentDate.isBefore(user.getStartDate()))
//                || (user.getEndDate() != null && currentDate.isAfter(user.getEndDate()))) {
//            expired = true;
//        }
        return expired;
    }
}