package com.capg.rechargenova.controller;

import com.capg.rechargenova.dto.NotificationResponse;
import com.capg.rechargenova.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private JavaMailSender mailSender;

    @MockBean
    private com.capg.rechargenova.service.EmailService emailService;

    @Test
    void testGetAllNotifications() throws Exception {
        NotificationResponse res = new NotificationResponse(1L, 1L, "msg", "type", "status", LocalDateTime.now());
        when(notificationService.getAllNotifications()).thenReturn(List.of(res));

        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("msg"));
    }

    @Test
    void testGetNotificationById() throws Exception {
        NotificationResponse res = new NotificationResponse(1L, 1L, "msg", "type", "status", LocalDateTime.now());
        when(notificationService.getNotificationById(1L)).thenReturn(res);

        mockMvc.perform(get("/api/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("msg"));
    }

    @Test
    void testGetNotificationsByUserId() throws Exception {
        NotificationResponse res = new NotificationResponse(1L, 1L, "msg", "type", "status", LocalDateTime.now());
        when(notificationService.getNotificationsByUserId(1L)).thenReturn(List.of(res));

        mockMvc.perform(get("/api/notifications/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testSendOtp() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/notifications/send-otp")
                .param("email", "test@example.com")
                .param("otp", "123456"))
                .andExpect(status().isOk());
    }
}
