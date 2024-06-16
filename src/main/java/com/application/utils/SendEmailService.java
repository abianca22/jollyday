package com.application.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendEmailService {
    private final JavaMailSender sender;

    public void sendMail(String toAddress,
                         String title,
                         String content
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("bianca.a22f@gmail.com");
        message.setTo(toAddress);
        message.setText(content);
        message.setSubject(title);

        sender.send(message);
    }
}
