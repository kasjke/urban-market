package com.example.urbanmarket;

import com.example.urbanmarket.utils.MailSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MailSenderTest {

    @Autowired
    private MailSender mailSender;

    @Test
    public void testSendMail() {
        String result = mailSender.send("test@example.com");
        assertEquals("Mail sent successfully", result);
    }
}