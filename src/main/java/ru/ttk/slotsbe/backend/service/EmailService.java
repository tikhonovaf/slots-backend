package ru.ttk.slotsbe.backend.service;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmailWithExcelAttachment(
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
        helper.setFrom("tikhonovafZel@yandex.ru"); // обязательно!

        // Добавляем вложение
        helper.addAttachment(fileName, new ByteArrayResource(excelData));

        System.out.println("От адреса " + message.getFrom() + " По адресу " + message.getAllRecipients() );

        mailSender.send(message);
        System.out.println("От адреса " + message.getFrom() + " По адресу " + message.getAllRecipients() + " Письмо успешно отправлено!");

    }
}