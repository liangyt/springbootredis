package com.liangyt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * 描述：
 *
 * @author tony
 * @创建时间 2017-09-25 16:29
 */
@SpringBootApplication
@EnableConfigurationProperties
@EnableRedisRepositories
public class Starter {
    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }
}