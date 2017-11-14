package com.liangyt.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 描述：Redis Config<br>
 *
 * @author tony
 * @创建时间 2017-09-26 13:09
 */
@SuppressWarnings("all")
@Configuration
//@EnableCaching
public class RedisConfig extends CachingConfigurerSupport{

    private final static Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        logger.info("配置 CacheManager");
        return new RedisCacheManager(redisTemplate);
    }

    /**
     * 配置RedisTemplate<br>
     *     <pre>
     *      这两段解释来自:http://blog.csdn.net/u013355724/article/details/47833683<br>
            在使用Redis中，将对象序列化以Json方式写入Redis的方法：
            基本推荐使用JdkSerializationRedisSerializer和StringRedisSerializer，因为其他两个序列化策略使用起来配置很麻烦，如果实在有需要序列化成Json和XML格式，可以使用java代码将String转化成相应的Json和XML。
            1：使用Spring-data-Redis提供的接口JacksonJsonRedisSerializer
            jackson-json工具提供了javabean与json之间的转换能力，可以将pojo实例序列化成json格式存储在redis中，也可以将json格式的数据转换成pojo实例。因为jackson工具在序列化和反序列化时，需要明确指定Class类型，因此此策略封装起来稍微复杂。需要jackson-mapper-asl工具支持
            使用jackson提供的库，将对象序列化为JSON字符串。优点是速度快，序列化后的字符串短小精悍。但缺点也非常致命，那就是此类的构造函数中有一个类型参数，必须提供要序列化对象的类型信息，通过查看源代码，发现其只在反序列化过程中用到了类型信息。

            2：使用Spring-data-Redis提供的接口JdkSerializationRedisSerializer
            数据格式必须为json或者xml，那么在编程级别，在redisTemplate配置中仍然使用StringRedisSerializer，在存储之前或者读取之后，使用“SerializationUtils”工具转换转换成json或者xml。
            使用JDK提供的序列化功能。 优点是反序列化时不需要提供类型信息(class)，但缺点是序列化后的结果非常庞大，是JSON格式的5倍左右，这样就会消耗redis服务器的大量内存.
     *     </pre>
     *  RedisTemplate默认使用JdkSerializationRedisSerializer<br/>
     *  调用方法 afterPropertiesSet 时会判断是否设置了序列化使用方式，如果没有，则使用默认的。
     *
     * @param redisConnectionFactory
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        logger.info("配置 RedisTemplate");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 默认
        RedisConfig.setForJson(redisTemplate);

        return redisTemplate;
    }

    /**
     * 字符串处理模板
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        logger.info("配置 StringRedisTemplate");
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringRedisTemplate;
    }

    /**
     * 设置数据的 Serializer<br>
     *     一般针对非自定义实体的处理。<br>
     *         如果存储的时候使用了这种 Serializer,则提取的时候也使用该 Serializer
     * @param redisTemplate
     */
    public static void setForJson(RedisTemplate redisTemplate) {
        if (null == redisTemplate) {
            logger.error("待配置的 RedisTemplate 为空");
            return;
        }

        Jackson2JsonRedisSerializer<Object> jjrs = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jjrs.setObjectMapper(objectMapper);

        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        redisTemplate.setDefaultSerializer(genericJackson2JsonRedisSerializer);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jjrs);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jjrs);

        redisTemplate.afterPropertiesSet();
    }

    /**
     * 设置数据的 Serializer<br>
     *     主要用于存储实体对象时设置 Serializer，而且有一些限制，该实体需实现了 Serializable,不能是内部类<br/>
     *     如果存储的时候使用了这种 Serializer,则提取的时候也使用该 Serializer<br>
     *         具体使用: com.liangyt.test.object.ObjectToRedis
     * @param redisTemplate
     */
    public static void setForEntity(RedisTemplate redisTemplate) {
        if (null == redisTemplate) {
            logger.error("待配置的 RedisTemplate 为空");
            return;
        }
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);

        redisTemplate.afterPropertiesSet();
    }
}
