package com.yeongjin.alarmserver.domain.repository;

import com.yeongjin.alarmserver.domain.api.entity.EmailAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailAlarmRepository extends JpaRepository<EmailAlarm, Long> {
    @Query("select e.id from EmailAlarm e where e.isSent = true")
    List<Long> findIdsIsSent();

    @Query("select e from EmailAlarm e where e.sendTime <= :sendTime and e.isSent = false")
    List<EmailAlarm> findEmailsToSend(LocalDateTime sendTime);
}
