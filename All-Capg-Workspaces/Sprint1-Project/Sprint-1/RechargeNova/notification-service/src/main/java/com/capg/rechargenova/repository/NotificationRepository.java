package com.capg.rechargenova.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capg.rechargenova.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
}
