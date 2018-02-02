package com.my.component.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * redis 模板工具
 */
@Slf4j
@Component
public class HessianRedisTemplate extends RedisTemplate<String, Object> {
    public HessianRedisTemplate() {
        RedisSerializer<String> stringSerializer = this.getStringSerializer();
        HessianRedisSerializer hessianRedisSerializer = new HessianRedisSerializer();
        this.setKeySerializer(stringSerializer);
        this.setValueSerializer(hessianRedisSerializer);
        this.setHashKeySerializer(stringSerializer);
        this.setHashValueSerializer(hessianRedisSerializer);
    }

    @Autowired
    public HessianRedisTemplate(RedisConnectionFactory connectionFactory) {
        this();
        this.setConnectionFactory(connectionFactory);
        this.afterPropertiesSet();
    }

    /**
     * string类型获取对应的自增值（不能通过{@link org.springframework.data.redis.core.ValueOperations#get(java.lang.Object)} 获取自增的值）
     *
     * @param key
     * @return
     */
    public Long getIncrValue(final String key) {

        return this.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = HessianRedisTemplate.this.getStringSerializer();
                byte[] rowkey = serializer.serialize(key);
                byte[] rowval = connection.get(rowkey);
                if (rowval == null) {
                    return 0l;
                }
                String val = serializer.deserialize(rowval);
                return Long.parseLong(val);
            }
        });
    }

    /**
     * hash类型获取对应的自增值（不能通过{@link HashOperations#get(java.lang.Object, java.lang.Object)} 获取自增的值）
     *
     * @param key
     * @param hashKey
     * @return
     */
    public Long getIncrValue(final String key, final String hashKey) {

        return this.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = HessianRedisTemplate.this.getStringSerializer();
                byte[] rowkey = serializer.serialize(key);
                byte[] rowhashKey = serializer.serialize(hashKey);
                byte[] rowval = connection.hGet(rowkey, rowhashKey);
                if (rowval == null) {
                    return 0l;
                }
                String val = serializer.deserialize(rowval);
                return Long.parseLong(val);
            }
        });
    }
}
