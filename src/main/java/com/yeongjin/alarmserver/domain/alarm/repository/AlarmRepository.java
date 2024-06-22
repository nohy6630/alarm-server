package com.yeongjin.alarmserver.domain.alarm.repository;

import com.yeongjin.alarmserver.domain.alarm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    @Query("select e.id from Alarm e where e.status = 'SENT'")
    List<Long> findIdsIsSent();

    @Query("select e from Alarm e where e.sendTime <= :sendTime and e.status = 'RESERVED'")
    List<Alarm> findEmailsToSend(LocalDateTime sendTime);
}
