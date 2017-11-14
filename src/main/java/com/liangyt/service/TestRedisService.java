package com.liangyt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * 描述：
 *
 * @author tony
 * @创建时间 2017-09-26 13:43
 */
@Service
public class TestRedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * redis 一般是以下的5种操作。<br>
     * RedisTemplate 除此外还扩展了其它的数据类型操作:<br/>
     * redisTemplate.opsForCluster();<br/>
     * redisTemplate.opsForGeo();
     */
    public void befor() {
        // 操作字符串
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        // 操作 hash
        redisTemplate.opsForHash();
        // 操作 list
        redisTemplate.opsForList();
        // 操作 set
        redisTemplate.opsForSet();
        // 操作有序set
        redisTemplate.opsForZSet();
    }

    public void save(String key, String vlue) {
        redisTemplate.opsForValue().set(key, vlue);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
