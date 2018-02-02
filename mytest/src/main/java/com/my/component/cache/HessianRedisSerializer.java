package com.my.component.cache;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.jarvis.cache.serializer.HessianSerializer;

import lombok.extern.slf4j.Slf4j;

/**
 * Hessian序列化实现
 */
@Slf4j
public class HessianRedisSerializer implements RedisSerializer<Object> {

  final static HessianSerializer hessianSerializer = new HessianSerializer();

  @Override
  public byte[] serialize(Object o) throws SerializationException {
    try {
      return hessianSerializer.serialize(o);
    } catch (Exception e) {
      throw new SerializationException("",e);
    }
  }

  @Override
  public Object deserialize(byte[] bytes) throws SerializationException {
    try {
      Object rs = hessianSerializer.deserialize(bytes,null);
      return rs;
    } catch (Exception e) {
      throw new SerializationException("",e);
    }
  }
}
