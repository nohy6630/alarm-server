package com.yeongjin.alarmserver.domain.emailalarm.repository;

import com.yeongjin.alarmserver.domain.emailalarm.entity.EmailAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAlarmRepository extends JpaRepository<EmailAlarm, Long>{
}
