package ru.ttk.slotsbe.backend.service.email;

import jakarta.mail.*;
import jakarta.mail.search.FlagTerm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.ttk.slotsbe.backend.service.excel.ExcelUploadService;

import java.io.InputStream;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientUsersEmailReaderService {

    private final ExcelUploadService excelUploadService;
    private final ClientUsersEmailSendService emailService;

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
            inbox.open(Folder.READ_ONLY);

//            Message[] messages = inbox.getMessages();
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            for (Message message : messages) {
                message.setFlag(Flags.Flag.SEEN, true);
                String senderEmail = message.getFrom()[0].toString();
                Address[] fromAddresses = message.getFrom();
//                if (fromAddresses != null && fromAddresses.length > 0) {
//                    String senderEmail = fromAddresses[0].toString();
//                    System.out.println("Отправитель: " + senderEmail);
//                    // TODO Проверка, что письмо от пользователя клиента
//                }
                if (message.getContentType().contains("multipart")) {
                    Multipart multipart = (Multipart) message.getContent();
                    for (int i = 0; i < multipart.getCount(); i++) {
                        BodyPart part = multipart.getBodyPart(i);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            String fileName = part.getFileName();
                            InputStream is = part.getInputStream();
                            processClientReserveExcelAttachment(senderEmail, is, fileName);
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

    private void processClientReserveExcelAttachment(String emailTo, InputStream inputStream, String fileName) {
        System.out.println("Обрабатываем файл: " + fileName);
        //  Резервируем слоты и формируем Excel для ответа

        byte[] excelBytes = excelUploadService.processClientReserveFromExcel(inputStream);
//        ByteArrayResource resource = new ByteArrayResource(excelBytes);

//        try (Workbook workbook = excelUploadService.processClientReserveFromExcel(inputStream);
//             ByteArrayOutputStream excelBytes = new ByteArrayOutputStream())
//        {
//            workbook.write(excelBytes);
//            log.info("Excel-отчет успешно сгенерирован.");
            try {
                String result = emailService.sendEmailToClientUserWithExcelAttachment(
                        emailTo,
                        "Customer Report",
                        "Please find attached the customer report.",
                        excelBytes,
                        "client_user_reserve.xlsx"
                );
//                messages.add(result);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }


//        } catch (IOException e) {
//            log.error("Ошибка при генерации Excel-отчета", e);
//            throw new RuntimeException("Ошибка при генерации Excel-файла", e);
//        }


    }
}
