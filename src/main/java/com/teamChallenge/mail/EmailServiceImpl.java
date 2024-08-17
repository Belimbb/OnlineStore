package com.teamChallenge.mail;

import com.teamChallenge.exception.LogEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailUsername;

    public static final String OBJECT_NAME = "Letter";

    private String subject;
    private String text;

    @Override
    public void sendLetter(String to, String subject, String text) {
        sendSimpleMessage(to, subject, text);
        log.info("{}: " + OBJECT_NAME + " (to: {}) was send", LogEnum.SERVICE, to);
    }

    @Override
    public void sendVerificationEmailLetter(String to, String verificationCode) {
        subject = "Email verification in the Online Store";
        text = "You need to verify your account. Your verification code: %s";

        sendSimpleMessage(to, subject, String.format(text, verificationCode));
        log.info("{}: Email verification " + OBJECT_NAME + " (to: {}) was send", LogEnum.SERVICE, to);
    }

    @Override
    public void sendVerificationPasswordLetter(String to, String verificationCode) {
        subject = "Verify password update in the Online Store";
        text = "You need to verify your account password update. Your verification code: %s";

        sendSimpleMessage(to, subject, String.format(text, verificationCode));
        log.info("{}: Password verification " + OBJECT_NAME + " (to: {}) was send", LogEnum.SERVICE, to);
    }

    private void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailUsername);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
