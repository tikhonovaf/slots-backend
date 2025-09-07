package ru.ttk.slotsbe.backend.service.email;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.search.FlagTerm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import ru.ttk.slotsbe.backend.model.ClientUser;
import ru.ttk.slotsbe.backend.model.VSlot;
import ru.ttk.slotsbe.backend.record.ExcelAttachment;
import ru.ttk.slotsbe.backend.repository.ClientUserRepository;
import ru.ttk.slotsbe.backend.repository.VSlotRepository;
import ru.ttk.slotsbe.backend.service.excel.ExcelGenerator;
import ru.ttk.slotsbe.backend.service.excel.ExcelUploadService;
import org.thymeleaf.context.Context;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientUsersEmailReaderService {

    private final ExcelUploadService excelUploadService;
    private final ClientUsersEmailSendService emailService;
    private final ClientUserRepository clientUserRepository;
    private final TemplateEngine templateEngine;
    private final VSlotRepository vSlotRepository;

    @Value("${mail.imap.host}")
    private String imapHost;

    @Value("${mail.imap.port}")
    private int imapPort;

    @Value("${mail.imap.username}")
    private String username;

    @Value("${mail.imap.password}")
    private String password;

    public void readEmails() {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imap");
        props.put("mail.imap.ssl.enable", "true");

        try {
            Session session = Session.getInstance(props);
            Store store = session.getStore("imap");
            store.connect(imapHost, imapPort, username, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

//            Message[] messages = inbox.getMessages();
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            for (Message message : messages) {
                message.setFlag(Flags.Flag.SEEN, true);
                String clientName = "клиент";
                if (!message.getSubject().equalsIgnoreCase("Резервирование")) {
                    continue;
                }
                InternetAddress address = new InternetAddress(message.getFrom()[0].toString());
                String senderEmail = address.getAddress();
                // Проверка, что письмо от пользователя клиента
                if (clientUserRepository.findByVcEmail(senderEmail).isEmpty()) {
                    continue;
                } else {
                    ClientUser clientUser = clientUserRepository.findByVcEmail(senderEmail).get(0);
                    if (!clientUser.getVcFirstName().isEmpty() && !clientUser.getVcSecondName().isEmpty()) {
                       clientName=  clientUser.getVcFirstName() + " " + clientUser.getVcSecondName();
                    }
                }

                if (message.getContentType().contains("multipart")) {
                    Multipart multipart = (Multipart) message.getContent();
                    for (int i = 0; i < multipart.getCount(); i++) {
                        BodyPart part = multipart.getBodyPart(i);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            String fileName = part.getFileName();
                            InputStream is = part.getInputStream();
                            processClientReserveExcelAttachment(senderEmail, is, fileName, clientName);
                        }
                    }
                }
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processClientReserveExcelAttachment(String emailTo, InputStream inputStream,
                                                     String fileName, String clientName) {
        List<ExcelAttachment> excelFiles = new ArrayList<>();
        System.out.println("Обрабатываем файл: " + fileName);
        //  Резервируем слоты и формируем Excel для ответа
        byte[] proccesedExcelBytes = excelUploadService.processClientReserveFromExcel(inputStream);

        //  Формируем  файл со свободными лотами
        List<VSlot> slots = vSlotRepository.findAllFreeSlots(LocalDate.now());
        if (!slots.isEmpty()) {
            // Генерируем Excel в памяти
            byte[] freeExcelBytes = null;
            try {
                freeExcelBytes = ExcelGenerator.generateExcelSlots(slots);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //  Добавляем файлы как вложения
            excelFiles = List.of(
                    new ExcelAttachment(proccesedExcelBytes, "Обработка резервирования.xlsx"),
                    new ExcelAttachment(freeExcelBytes, "Свободные слоты.xlsx")
            );
        } else {
            excelFiles = List.of(
                    new ExcelAttachment(proccesedExcelBytes, "Обработка резервирования.xlsx")
            );
        }

        // Генерация HTML из шаблона
        String subject = "Обработка резервирования слотов";
        Context context = new Context();
        context.setVariable("name", clientName);
        String htmlContent = templateEngine.process("reserved-info", context);

        String result = emailService.sendEmailToClientUserWithExcelAttachments(
                emailTo,
                subject,
                htmlContent,
                excelFiles
        );
//                messages.add(result);


    }
}
