package com.liangyt.test.object;

import com.liangyt.Starter;
import com.liangyt.config.redis.RedisConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;

/**
 * 描述：尝试对象存储到Redis
 *
 * @author tony
 * @创建时间 2017-10-19 16:41
 */
@SuppressWarnings("all")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Starter.class)
public class ObjectToRedis {
    @Autowired
    private RedisTemplate redisTemplate;
    private HashOperations<String, String, UserInfo> hashOperations;
    private ValueOperations<String, UserInfo> valueOperations;

    @Before
    public void before() {
        // 配置支持序列化
        RedisConfig.setForEntity(redisTemplate);

        hashOperations = redisTemplate.opsForHash();
        valueOperations = redisTemplate.opsForValue();
    }

    @Test
    public void save() {
        UserInfo userInfo = new UserInfo(1, 10, "objectTest");
        hashOperations.put("user", "123", userInfo);
        valueOperations.set("user:01", userInfo);


    }

    @Test
    public void query() {
//        UserInfo userInfo = (UserInfo)hashOperations.get("user", "123");
//        System.out.println(userInfo.getName());
        System.out.println(hashOperations.get("user", "123").getAge());
        System.out.println(valueOperations.get("user:01").getName());
    }
}
