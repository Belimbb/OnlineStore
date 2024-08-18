package com.teamChallenge.mail;

import com.teamChallenge.exception.LogEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailUsername;

    public static final String OBJECT_NAME = "Letter";
    @Value("${server.host}")
    private String URI_HOST;
    public static final String URI_VERIFICATION = "/auth/verification/";
    private String subject;
    private String text;

    @Override
    public void sendLetter(String to, String subject, String text) {
        sendSimpleMessage(to, subject, text);
        log.info("{}: " + OBJECT_NAME + " (to: {}) was send", LogEnum.SERVICE, to);
    }

    @Override
    public void sendVerificationEmailLetter(String to, UUID verificationCode) {
        subject = "Email verification in the Online Store";
        text = "You need to verify your account. Please go to the following link: %s";

        sendSimpleMessage(to, subject, String.format(text, URI_HOST + URI_VERIFICATION + "email/" + verificationCode));
        log.info("{}: Email verification " + OBJECT_NAME + " (to: {}) was send", LogEnum.SERVICE, to);
    }

    @Override
    public void sendVerificationPasswordLetter(String to, UUID verificationCode) {
        subject = "Verify password update in the Online Store";
        text = "You need to verify your account password update. Please go to the following link: %s";

        sendSimpleMessage(to, subject, String.format(text, URI_HOST + URI_VERIFICATION + "password/" + verificationCode));
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
