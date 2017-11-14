package com.liangyt.test.string;

import com.liangyt.Starter;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 描述：字符串
 *
 * @author tony
 * @创建时间 2017-10-09 11:02
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Starter.class)
public class Test {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /** 字符串操作对象 */
    private ValueOperations<String, String> valueOperations;

    @Before
    public void before() {
        valueOperations = stringRedisTemplate.opsForValue();
    }

    @org.junit.Test
    public void setTest() {
        valueOperations.set("key1", "看看能不能存中文");

        valueOperations.set("key2", "真的是这样的吗？");
        // 使用这个方法，需要是字符串的序列化进行处理
        valueOperations.set("key2", "偏移量存储测试", 3);

        valueOperations.set("key3", "保存时间", 15, TimeUnit.SECONDS); // 秒过期

        System.out.println(valueOperations.get("key1")); // 看看能不能存中文
        System.out.println(valueOperations.get("key2")); // 真偏移量存储测试
        System.out.println(valueOperations.size("key2")); // 24
        System.out.println(valueOperations.get("key2", 3, 5)); // 偏  // 字符串中有中文的时候，使用这个方法需要特别注意一下，很容易就取到乱码了
        System.out.println(valueOperations.get("key3")); // 15秒内取出 保存时间 15秒外取出 null
    }

    @org.junit.Test
    public void setIfAbsentTest() {
        System.out.println(valueOperations.setIfAbsent("key1", "setIfAbsent1")); // false // setTest 这个方法已经存进去了
        System.out.println(valueOperations.setIfAbsent("key4", "setIfAbsent2")); // true // 之前未存过
        System.out.println(valueOperations.get("key1")); // 看看能不能存中文
        System.out.println(valueOperations.get("key4")); // setIfAbsent2
    }

    @org.junit.Test
    public void multiSetTest() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("mkey1", "mkey1");
        map.put("mkey2", "mkey2");
        valueOperations.multiSet(map);

        List<String> keys = new ArrayList<String>();
        keys.add("mkey1");
        keys.add("mkey2");
        keys.add("nokey");
        System.out.println(valueOperations.multiGet(keys)); // [mkey1, mkey2, null]
    }

    @org.junit.Test
    public void multiSetIfAbsendTest() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("mkey1", "mkey12");
        map.put("mkey2", "mkey22");
        map.put("mkey3", "mkey32");
        System.out.println(valueOperations.multiSetIfAbsent(map)); // false // 有一个出现过则全部失败

        List<String> keys = new ArrayList<String>();
        keys.add("mkey1");
        keys.add("mkey2");
        keys.add("mkey3");
        System.out.println(valueOperations.multiGet(keys)); // [mkey1, mkey2, null]  // 取出的值还是之前的值



        Map<String, String> mapN = new HashMap<String, String>();
        mapN.put("mkey11", "mkey12");
        mapN.put("mkey22", "mkey22");
        mapN.put("mkey33", "mkey32");
        System.out.println(valueOperations.multiSetIfAbsent(mapN)); // true // 都不存在的时候才会成功

        List<String> keysN = new ArrayList<String>();
        keysN.add("mkey11");
        keysN.add("mkey22");
        keysN.add("mkey33");
        System.out.println(valueOperations.multiGet(keysN)); // [mkey12, mkey22, mkey32]
    }

    @org.junit.Test
    public void getAndSetTest() {
        System.out.println(valueOperations.getAndSet("key1", "key1")); // 看看能不能存中文  // 返回的是原来的旧值
        System.out.println(valueOperations.get("key1")); // key1
        System.out.println(valueOperations.getAndSet("key1GetAndSet", "key1GetAndSet")); // null // 原来没有值
        System.out.println(valueOperations.get("key1GetAndSet")); // key1GetAndSet
    }

    /**
     * 数字
     */
    @org.junit.Test
    public void numberTest() {
        // 整数
        valueOperations.increment("keylong", 10);
        // 浮点数
        valueOperations.increment("keydouble", 10.11);

        System.out.println(valueOperations.get("keylong")); // 10
        System.out.println(valueOperations.get("keydouble")); // 10.11
    }
}
