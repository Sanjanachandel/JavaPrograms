package com.capg.rechargenova.service;

import com.capg.rechargenova.dto.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    @Test
    void testSendRechargeSuccessEmail() throws MessagingException {
        EmailRequest request = new EmailRequest("to@example.com", "User", "1234567890", "Airtel", 299.0, "28 days", "TXN123");
        
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        emailService.sendRechargeSuccessEmail(request);
        
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}
