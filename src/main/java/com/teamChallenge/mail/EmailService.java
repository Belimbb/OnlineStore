package com.teamChallenge.mail;

import java.util.UUID;

public interface EmailService {

    void sendLetter(String to, String subject, String text);

    void sendVerificationEmailLetter(String to, UUID emailVerificationCode);

    void sendVerificationPasswordLetter(String to, UUID passwordVerificationCode);
}
