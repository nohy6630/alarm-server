package com.yeongjin.alarmserver.domain.api.repository;

import com.yeongjin.alarmserver.domain.api.entity.EmailAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAlarmRepository extends JpaRepository<EmailAlarm, Long>{
}
