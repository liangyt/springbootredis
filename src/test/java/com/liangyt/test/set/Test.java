package com.liangyt.test.set;

import com.liangyt.Starter;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：无序集合 set
 *
 * @author tony
 * @创建时间 2017-10-10 13:17
 */
@SuppressWarnings("all")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Starter.class)
public class Test {
    @Autowired
    private RedisTemplate redisTemplate;

    private SetOperations setOperations;

    @Before
    public void before() {
        setOperations = redisTemplate.opsForSet();

        List listDel = new ArrayList();
        listDel.add("skey");
        listDel.add("newskey");
        listDel.add("s1");
        listDel.add("s2");
        listDel.add("s3");
        listDel.add("s4");
        listDel.add("s5");
        listDel.add("s6");
        listDel.add("s7");
        listDel.add("s8");
        listDel.add("s9");
        // 先删除
        redisTemplate.delete(listDel);
    }

    @org.junit.Test
    public void operation() {

        // 添加元素
        setOperations.add("skey", "第一个", "第二个", 1);
        // 所有的值
        System.out.println(setOperations.members("skey")); // [1, 第二个, 第一个]
        // 元素个数
        System.out.println(setOperations.size("skey")); // 3

        System.out.println(setOperations.members("skey")); // [1, 第二个, 第一个]
        // 把第二个参数的值从第一个参数的集合中移到第三个参数的集合中
        setOperations.move("skey", "第一个", "newskey");
        System.out.println(setOperations.members("skey")); // [1, 第二个]
        System.out.println(setOperations.members("newskey")); // [第一个]

        System.out.println(setOperations.isMember("skey", "第二个")); // true

        // 随意返回一个元素
        System.out.println(setOperations.pop("skey")); // 第二个  // 这个可能跟你看到的不太一样，因为返回的值是随意的

        // 移除 集合中的 1
        setOperations.remove("skey", 1);
    }

    @org.junit.Test
    public void collection() {
        setOperations.add("s1", "a", "b", "c", "d");
        setOperations.add("s2", "a", "e");
        setOperations.add("s3", "a", "c", "d", "f");

        List listCol = new ArrayList();
        listCol.add("s2");
        listCol.add("s3");

        // s1 和 s2 集合的交集
        System.out.println(setOperations.intersect("s1", "s2")); // [a]
        // s1 和 多个集合的交集
        System.out.println(setOperations.intersect("s1", listCol)); // [a]
        // s1 和 s2 的交集保存到 s4
        System.out.println(setOperations.intersectAndStore("s1", "s2", "s4")); // 1  // 交集只有一个
        System.out.println(setOperations.members("s4"));
        // s1 和 多个集合的交集保存到 s5
        System.out.println(setOperations.intersectAndStore("s1", listCol, "s5"));
        System.out.println(setOperations.members("s5"));

        // s1 和 s2 集合的并集
        System.out.println(setOperations.union("s1", "s2"));
        // s1 和 多个集合的并集
        System.out.println(setOperations.union("s1", listCol));
        // s1 和 s2 的并集保存到 s6
        System.out.println(setOperations.unionAndStore("s1", "s2", "s6"));
        System.out.println(setOperations.members("s6"));
        // s1 和 多个集合的并集保存到 s7
        System.out.println(setOperations.unionAndStore("s1", listCol, "s7"));
        System.out.println(setOperations.members("s7"));

        // s1 和 s2 集合的差集
        System.out.println(setOperations.difference("s1", "s2"));
        // s1 和 多个集合的差集
        System.out.println(setOperations.difference("s1", listCol));
        // s1 和 s2 的差集保存到 s8
        System.out.println(setOperations.differenceAndStore("s1", "s2", "s8"));
        System.out.println(setOperations.members("s8"));
        // s1 和 多个集合的差集保存到 s9
        System.out.println(setOperations.differenceAndStore("s1", listCol, "s9"));
        System.out.println(setOperations.members("s9"));

        // 随机返回一个值
        System.out.println(setOperations.randomMember("s1"));
        // 随机返回多个值 可能出现重复值
        System.out.println(setOperations.randomMembers("s1", 3));
        // 随机返回多个值 不出现重复值
        System.out.println(setOperations.distinctRandomMembers("s1", 3));

        Cursor cursor = setOperations.scan("s1", ScanOptions.NONE);
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    }
}
