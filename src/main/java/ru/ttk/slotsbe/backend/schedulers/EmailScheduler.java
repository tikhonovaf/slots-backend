package ru.ttk.slotsbe.backend.schedulers;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.ttk.slotsbe.backend.service.email.ClientUsersEmailReaderService;

@Component
@RequiredArgsConstructor
public class EmailScheduler {

    private final ClientUsersEmailReaderService clientUsersEmailReaderService;

    @Scheduled(fixedRate = 60000) // каждые 60 секунд
    public void checkInbox() {
        clientUsersEmailReaderService.readEmails();
    }
}
