package com.teamChallenge.mail;

import java.util.UUID;

public interface EmailService {

    void sendLetter(String to, String subject, String text);

    void sendVerificationLetter(String to, UUID verificationCode);
}
