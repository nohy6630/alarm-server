package com.yeongjin.alarmserver.global.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedisConfigTest {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    @DisplayName("RedisTemplate을 사용하여 데이터를 저장하고 조회한다.")
    void redisTemplate() {
        //given
        String key = "testKey";
        String value = "testValue";

        //when
        redisTemplate.opsForValue().set(key, value);

        //then
        String savedValue = redisTemplate.opsForValue().get(key);
        assertThat(savedValue).isEqualTo(value);
    }
}