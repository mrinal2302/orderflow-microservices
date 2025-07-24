package com.notification.service.repository;


import com.notification.service.entity.OrderNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<OrderNotification, String> {

}
