package com.liangyt.test.list;

import com.liangyt.Starter;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：列表
 *
 * @author tony
 * @创建时间 2017-10-09 17:20
 */
@SuppressWarnings("all")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Starter.class)
public class Test {
    @Autowired
    private RedisTemplate redisTemplate;

    private ListOperations listOperations;

    @org.junit.Before
    public void before() {
        listOperations = redisTemplate.opsForList();
    }

    /**
     * push 操作
     */
    @org.junit.Test
    public void pushTest() {
        // 清空列表 其实就是删除该键了
        redisTemplate.delete("lkey1");

        // 从左边插入

        // 将所有指定的值插入存储在键的列表的头部。如果键不存在，则在执行推送操作之前将其创建为空列表。（从左边插入）还会返回当前列表的长度
        listOperations.leftPush("lkey1", "java");
        listOperations.leftPush("lkey1", "C++");
        listOperations.leftPush("lkey1", "中文测试");

        System.out.println(listOperations.range("lkey1", 0, -1)); // [中文测试, C++, java]

        // 插入数组
        String[] array = new String[]{"javascript", "css"};
        listOperations.leftPushAll("lkey1", array);

        System.out.println(listOperations.range("lkey1", 0, -1)); // [css, javascript, 中文测试, C++, java]

        // 插入列表
        List<String> list = new ArrayList<String>();
        list.add("Vue");
        list.add("React");
        listOperations.leftPushAll("lkey1", list);

        System.out.println(listOperations.range("lkey1", 0, -1)); // [React, Vue, css, javascript, 中文测试, C++, java]

        // 只有存在key对应的列表才能将这个value值插入到key所对应的列表中
        listOperations.leftPushIfPresent("lkey1", "Angular");
        listOperations.leftPushIfPresent("lkey2", "Angular");

        System.out.println(listOperations.range("lkey1", 0, -1)); // [Angular, React, Vue, css, javascript, 中文测试, C++, java]
        System.out.println(listOperations.range("lkey2", 0, -1)); // []

        // Long leftPush(K key, V pivot, V value) 把value值放到key对应列表中pivot值的左面，如果pivot值存在的话
        listOperations.leftPush("lkey1", "Vue", "preVue");
        System.out.println(listOperations.range("lkey1", 0, -1)); // [Angular, React, preVue, Vue, css, javascript, 中文测试, C++, java]
        listOperations.leftPush("lkey1", "noVue", "preVue");
        System.out.println(listOperations.range("lkey1", 0, -1)); // [Angular, React, preVue, Vue, css, javascript, 中文测试, C++, java]

        // 清空列表 其实就是删除该键了
        redisTemplate.delete("lkey1");

        // 从右边插入

        // 将所有指定的值插入存储在键的列表的头部。如果键不存在，则在执行推送操作之前将其创建为空列表。（从右边插入）还会返回当前列表的长度
        listOperations.rightPush("lkey1", "java");
        listOperations.rightPush("lkey1", "C++");
        listOperations.rightPush("lkey1", "中文测试");

        System.out.println(listOperations.range("lkey1", 0, -1)); // [java, C++, 中文测试]

        // 插入数组
        array = new String[]{"javascript", "css"};
        listOperations.rightPushAll("lkey1", array);

        System.out.println(listOperations.range("lkey1", 0, -1)); // [java, C++, 中文测试, javascript, css]

        // 插入列表
        list = new ArrayList<String>();
        list.add("Vue");
        list.add("React");
        listOperations.rightPushAll("lkey1", list);

        System.out.println(listOperations.range("lkey1", 0, -1)); // [java, C++, 中文测试, javascript, css, Vue, React]

        // 只有存在key对应的列表才能将这个value值插入到key所对应的列表中
        listOperations.rightPushIfPresent("lkey1", "Angular");
        listOperations.rightPushIfPresent("lkey2", "Angular");

        System.out.println(listOperations.range("lkey1", 0, -1)); // [java, C++, 中文测试, javascript, css, Vue, React, Angular]
        System.out.println(listOperations.range("lkey2", 0, -1)); // []

        // Long rightPush(K key, V pivot, V value) 把value值放到key对应列表中pivot值的右面，如果pivot值存在的话
        listOperations.rightPush("lkey1", "Vue", "preVue");
        System.out.println(listOperations.range("lkey1", 0, -1)); // [java, C++, 中文测试, javascript, css, Vue, preVue, React, Angular]
        listOperations.rightPush("lkey1", "noVue", "preVue");
        System.out.println(listOperations.range("lkey1", 0, -1)); // [java, C++, 中文测试, javascript, css, Vue, preVue, React, Angular]
    }

    @org.junit.Test
    public void setTest() {
        String[] array = new String[]{"javascript", "css"};
        listOperations.rightPushAll("lkey3", array);
        System.out.println(listOperations.range("lkey3", 0, -1)); // [javascript, css]

        // void set(K key, long index, V value)  在列表中index的位置设置value值
        listOperations.set("lkey3", 1, "newValue");
        System.out.println(listOperations.range("lkey3", 0, -1)); // [javascript, newValue]

        // 设置一个不存在的下标
        listOperations.set("lkey3", 5, "newValue"); // 下标越界异常
        System.out.println(listOperations.range("lkey3", 0, -1));
    }

    @org.junit.Test
    public void removeTest() {
        // Long remove(K key, long count, Object value);
        /**
         * 从存储在键中的列表中删除等于值的元素的第一个计数事件。
         * count > 0：删除等于value的从头到尾开始的 count 个值
         * count < 0：删除等于value的从尾到头开始的 count 个值
         * count = 0：删除等于value的所有元素
          */

        String[] array = new String[]{"javascript", "css", "javascript", "css", "css", "a", "a", "a", "b", "b"};
        listOperations.rightPushAll("lkey4", array);
        System.out.println(listOperations.range("lkey4", 0, -1)); // [javascript, css, javascript, css, css, a, a, a, b, b]

        // 删除从左边开始出现的第一个 javascript
        listOperations.remove("lkey4", 1, "javascript");
        System.out.println(listOperations.range("lkey4", 0, -1)); // [css, javascript, css, css, a, a, a, b, b]

        // 删除从左边开始出现的头两个 css
        listOperations.remove("lkey4", 2, "css");
        System.out.println(listOperations.range("lkey4", 0, -1)); // [javascript, css, a, a, a, b, b]

        // 删除所有的 a
        listOperations.remove("lkey4", 0, "a");
        System.out.println(listOperations.range("lkey4", 0, -1)); // [javascript, css, b, b]

        listOperations.remove("lkey4", -1, "javascript");
        System.out.println(listOperations.range("lkey4", 0, -1)); // [css, b, b]

        listOperations.remove("lkey4", -2, "b");
        System.out.println(listOperations.range("lkey4", 0, -1)); // [css]
    }

    @org.junit.Test
    public void indexTest() {
        String[] array = new String[]{"javascript", "css", "javascript", "css"};
        listOperations.rightPushAll("lkey5", array);
        System.out.println(listOperations.range("lkey5", 0, -1)); // [javascript, css, javascript, css]

        // V index(K key, long index)  根据下标获取列表中的值，下标是从0开始的
        System.out.println(listOperations.index("lkey5", 0)); // javascript
        System.out.println(listOperations.index("lkey5", 2)); // javascript
        System.out.println(listOperations.index("lkey5", 10)); // null   // 下标越界取回 null
    }

    @org.junit.Test
    public void popTest() {
        String[] array = new String[]{"javascript", "css", "javascript", "css", "不", "错", "吗"};
        listOperations.rightPushAll("lkey6", array);
        System.out.println(listOperations.range("lkey6", 0, -1)); // [javascript, css, javascript, css, 不, 错, 吗]

        System.out.println(listOperations.leftPop("lkey6")); // javascript
        System.out.println(listOperations.rightPop("lkey6")); // 吗

        /**
         *  V leftPop(K key, long timeout, TimeUnit unit); 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
         *  V rightPop(K key, long timeout, TimeUnit unit); 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
         *  V rightPopAndLeftPush(K sourceKey, K destinationKey); 用于移除列表的最后一个元素，并将该元素添加到另一个列表并返回。
         *  V rightPopAndLeftPush(K sourceKey, K destinationKey, long timeout, TimeUnit unit); 用于移除列表的最后一个元素，并将该元素添加到另一个列表并返回，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
         */
    }
}
