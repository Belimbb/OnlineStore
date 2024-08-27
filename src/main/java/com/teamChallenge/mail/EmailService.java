package com.teamChallenge.mail;

public interface EmailService {

    void sendLetter(String to, String subject, String text);

    void sendVerificationEmailLetter(String to, String emailVerificationCode);

    void sendVerificationPasswordLetter(String to, String passwordVerificationCode);
}
