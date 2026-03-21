package com.example.recharge_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String phoneNumber;
    private LocalDateTime createdAt;
}
