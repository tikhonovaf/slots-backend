package ru.ttk.slotsbe.backend.service.email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.ttk.slotsbe.backend.record.ExcelAttachment;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientUsersEmailSendService {
    @Value("${mail.smtp.from}")
    private String from;

    private final JavaMailSender mailSender;

    public String sendEmailToClientUserWithExcelAttachments(
            String toEmail,
            String subject,
            String bodyHTML,
            List<ExcelAttachment> attachments
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(bodyHTML, true); // HTML включён
            helper.setFrom(from);

            for (ExcelAttachment attachment : attachments) {
                helper.addAttachment(attachment.fileName(), new ByteArrayResource(attachment.data()));
            }

            mailSender.send(message);

            String fromText = Optional.ofNullable(message.getFrom())
                    .map(addresses -> addresses.length > 0 ? ((InternetAddress) addresses[0]).getAddress() : null)
                    .map(addr -> "От адреса: " + addr)
                    .orElse("Отправитель не указан");

            String toText = Optional.ofNullable(message.getAllRecipients())
                    .map(recipients -> Arrays.stream(recipients)
                            .map(addr -> ((InternetAddress) addr).getAddress())
                            .collect(Collectors.joining(", ")))
                    .map(list -> " По адресу: " + list)
                    .orElse("Получатели не указаны");

            return (!fromText.contains("не") && !toText.contains("не"))
                    ? "Письмо успешно отправлено " + fromText + toText
                    : "Адрес отправителя или получателя не указан " + fromText + toText;

        } catch (MessagingException e) {
            return "Ошибка при отправке письма: " + e.getMessage();
        }
    }

}
