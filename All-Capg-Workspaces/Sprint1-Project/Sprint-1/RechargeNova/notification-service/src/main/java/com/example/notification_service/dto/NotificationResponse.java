package com.example.notification_service.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {
    private Long id;
    private Long userId;
    private String message;
    private String type;
    private String status;
    private LocalDateTime createdAt;
}
