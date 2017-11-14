package com.liangyt.test.hash;

import com.liangyt.Starter;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：散列
 *
 * @author tony
 * @创建时间 2017-10-10 10:49
 */
@SuppressWarnings("all")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Starter.class)
public class Test {
    @Autowired
    private RedisTemplate redisTemplate;

    private HashOperations hashOperations;

    @Before
    public void before() {
        hashOperations = redisTemplate.opsForHash();
    }

    @org.junit.Test
    public void test() {
        hashOperations.put("hkey", "name", "liangyt");

        Map map = new HashMap();
        map.put("age", 1);
        map.put("price", 10.2);
        map.put("sex", "男");
        map.put("height", 150);
        hashOperations.putAll("hkey", map);

        System.out.println(hashOperations.entries("hkey")); // {name=liangyt, price=10.2, height=150, sex=男, age=1}

        // 删除该散列中的某个键
        hashOperations.delete("hkey", "sex");
        System.out.println(hashOperations.entries("hkey")); // {name=liangyt, height=150, price=10.2, age=1}

        // 检查该散列中的某个键是否存在
        System.out.println(hashOperations.hasKey("hkey", "name")); // true

        // 从该散列中取得某个键的值
        System.out.println(hashOperations.get("hkey", "name")); // liangyt

        // 根据列表组成的键取回对应的值
        List list = new ArrayList();
        list.add("age");
        list.add("price");
        System.out.println(hashOperations.multiGet("hkey", list)); // [1, 10.2]

        hashOperations.increment("hkey", "age", 2);
        hashOperations.increment("hkey", "price", 4.6);
//        hashOperations.increment("hkey", "name", 10); // 异常  // name 存的不是整数
        System.out.println(hashOperations.entries("hkey")); // {name=liangyt, height=150, age=3, price=14.8}

        System.out.println(hashOperations.keys("hkey")); // [name, price, age, height]
        System.out.println(hashOperations.values("hkey")); // [liangyt, 14.8, 3, 150]
        System.out.println(hashOperations.size("hkey")); // 4

        Cursor<Map.Entry> cursor = hashOperations.scan("hkey", ScanOptions.NONE);
        while (cursor.hasNext()) {
            Map.Entry entry = cursor.next();
            System.out.println(entry.getKey() + ":" + entry.getValue());

//            name:liangyt
//            price:14.8
//            age:3
//            height:150
        }
    }
}
