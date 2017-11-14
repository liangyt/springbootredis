package com.liangyt.test.zset;

import com.liangyt.Starter;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 描述：有序集合 zset
 *
 * @author tony
 * @创建时间 2017-10-10 14:41
 */
@SuppressWarnings("all")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Starter.class)
public class Test {
    @Autowired
    private RedisTemplate redisTemplate;
    private ZSetOperations zSetOperations;

    @Before
    public void before() {
        zSetOperations = redisTemplate.opsForZSet();

        List listDel = new ArrayList();
        listDel.add("zs1");
        // 先删除
        redisTemplate.delete(listDel);

        zSetOperations.add("zs1", "a", 1.1);
        zSetOperations.add("zs1", "b", 1.2);
        zSetOperations.add("zs1", "c", 1.3);
        ZSetOperations.TypedTuple typedTuple1 = new DefaultTypedTuple("d", 1.5);
        ZSetOperations.TypedTuple typedTuple2 = new DefaultTypedTuple("e", 1.4);
        Set<ZSetOperations.TypedTuple> set = new HashSet<ZSetOperations.TypedTuple>();
        set.add(typedTuple1);
        set.add(typedTuple2);
        zSetOperations.add("zs1", set);

        zSetOperations.add("zs2", "a", 1.0);
        zSetOperations.add("zs2", "b", 1.1);
        zSetOperations.add("zs2", "f", 1.2);
    }

    /**
     * 取值的方法比较多，就不一一列出了
     * Set<TypedTuple<V>> rangeByScoreWithScores(K key, double min, double max);
     * Set<TypedTuple<V>> rangeByScoreWithScores(K key, double min, double max, long offset, long count);
     * Set<V> reverseRange(K key, long start, long end);
     * Set<TypedTuple<V>> reverseRangeWithScores(K key, long start, long end);
     * Set<V> reverseRangeByScore(K key, double min, double max);
     * Set<TypedTuple<V>> reverseRangeByScoreWithScores(K key, double min, double max);
     * Set<V> reverseRangeByScore(K key, double min, double max, long offset, long count);
     * Set<TypedTuple<V>> reverseRangeByScoreWithScores(K key, double min, double max, long offset, long count);
     *
     */
    @org.junit.Test
    public void operation() {

        System.out.println(zSetOperations.range("zs1", 0, -1)); // [a, b, c, e, d]

        // 增加元素的score值，并返回增加后的值  这个我测试的时候发现一个问题，1.4 + 0.2 结果为 1.5999999999999999
        System.out.println(zSetOperations.incrementScore("zs1", "e", 0.2)); // 1.5999999999999999
        System.out.println(zSetOperations.incrementScore("zs1", "d", 1.1)); // 2.6

        // 通过分数返回有序集合指定区间内的成员个数
        System.out.println(zSetOperations.count("zs1", 1.1, 2.1));
        // 返回个数   内部调用的就是zCard方法
        System.out.println(zSetOperations.size("zs1"));
        // 返回个数
        System.out.println(zSetOperations.zCard("zs1"));
        // 获取指定成员的score值
        System.out.println(zSetOperations.score("zs1", "a"));

        // Long rank(K key, Object o); 返回有序集中指定成员的排名，其中有序集成员按分数值递增(从小到大)顺序排列
        System.out.println(zSetOperations.rank("zs1", "b"));
        // Long reverseRank(K key, Object o); 返回有序集中指定成员的排名，其中有序集成员按分数值递减(从大到小)顺序排列
        System.out.println(zSetOperations.reverseRank("zs1", "b"));

        zSetOperations.remove("zs1", "a");
        zSetOperations.removeRange("zs1", 0, 1);
        zSetOperations.removeRangeByScore("zs1", 1, 1.1);

        // 通过索引区间返回有序集合成指定区间内的成员，其中有序集成员按分数值递增(从小到大)顺序排列 end 为-1 为到最后
        System.out.println(zSetOperations.range("zs1", 0, -1));
        // 通过分数返回有序集合指定区间内的成员，其中有序集成员按分数值递增(从小到大)顺序排列
        System.out.println(zSetOperations.rangeByScore("zs1", 1.1, 2.1));
    }
}
