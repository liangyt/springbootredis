package com.liangyt.test;

import com.liangyt.Starter;
import com.liangyt.service.TestRedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 描述：
 *
 * @author tony
 * @创建时间 2017-09-26 13:49
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Starter.class)
public class RedisStringTest {

    @Autowired
    private TestRedisService testRedisService;

    @Test
    public void saveTest() {
        testRedisService.save("hello", "hello");
        System.out.println(testRedisService.get("hello"));
    }
}
