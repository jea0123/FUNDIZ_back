package com.example.funding.mapper;

import com.example.funding.model.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper {
    List<Notification> getNotificationsByUserId(@Param("userId") Long userId);

    Notification getNotificationById(@Param("notificationId") Long notificationId);

    void insertNotification(Notification notification);

    void markAsRead(@Param("notificationId") Long notificationId);

    void deleteNotification(@Param("notificationId") Long notificationId);

    void deleteAllNotificationsByUserId(@Param("userId") Long userId);
}
