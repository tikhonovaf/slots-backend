package ru.ttk.slotsbe.backend.service.email;
import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientUsersEmailSendService {
    @Value("${mail.smtp.from}")
    private String from;

    private final JavaMailSender mailSender;

    public String sendEmailToClientUserWithExcelAttachment(
            String toEmail,
            String subject,
            String body,
            byte[] excelData,
            String fileName
    ) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body);
        helper.setFrom(from);

        // Добавляем вложение
        helper.addAttachment(fileName, new ByteArrayResource(excelData));

        mailSender.send(message);
        // Получение текста адреса отправителя
        String fromAddressesText = null;
        Address[] fromAddresses = message.getFrom();
        if (fromAddresses != null && fromAddresses.length > 0) {
            String senderEmail = ((InternetAddress) fromAddresses[0]).getAddress();
            fromAddressesText = "От адреса: " + senderEmail;
        } else {
            fromAddressesText ="Отправитель не указан";
        }

        // Получение текста адресов получателдей
        String recipientsText = null;
        Address[] recipients = message.getAllRecipients();
        if (recipients != null && recipients.length > 0) {
            List<String> recipientEmails = new ArrayList<>();
            for (Address recipient : recipients) {
                recipientEmails.add(((InternetAddress) recipient).getAddress());
            }
            recipientsText = " По адресу: " + String.join(", ", recipientEmails);
        } else {
            recipientsText = "Получатели не указаны";
        }

        if (!fromAddressesText.contains("не") && !recipientsText.contains("не")) {
            return "Письмо успешщно отправлено " + fromAddressesText + recipientsText;
        } else {
            return "Адрес отправителя или получателя не указан " + fromAddressesText + recipientsText;
        }
    }
}