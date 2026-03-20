package rycfod.com.the_tracking_guard_dog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rycfod.com.the_tracking_guard_dog.model.NotificationLog;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
}
