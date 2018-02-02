package com.my.component.cache;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import com.jarvis.cache.AbstractCacheManager;
import com.jarvis.cache.exception.CacheCenterConnectionException;
import com.jarvis.cache.script.AbstractScriptParser;
import com.jarvis.cache.serializer.ISerializer;
import com.jarvis.cache.to.CacheKeyTO;
import com.jarvis.cache.to.CacheWrapper;
import com.my.utils.SpringUtil;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class SpringDataRedisCacheManager extends AbstractCacheManager {

  private static RedisScript redisScrip = new DefaultRedisScript("redis.call(\'HSET\', KEYS[1], ARGV[1], ARGV[2]);\nredis.call(\'EXPIRE\', KEYS[1], tonumber(ARGV[3]));",null);

  private int hashExpire = -1;
  private boolean hashExpireByScript = false;

  @Autowired
  HessianRedisTemplate redisTemplate;
  @Autowired
  public SpringDataRedisCacheManager(AutoLoadCacheProperties config,
      ISerializer<Object> serializer,
      AbstractScriptParser scriptParser) {
    super(config, serializer, scriptParser);
    String nameSpace = config.getNameSpace();
    if(StringUtils.isBlank(nameSpace)){
      nameSpace = SpringUtil.getSpringApplicationName();
    }
    super.setNamespace(nameSpace);
  }
  @Override
  public void setCache(CacheKeyTO cacheKeyTO, CacheWrapper<Object> result, Method method,
      Object[] objects) throws CacheCenterConnectionException {
    if(null != this.redisTemplate && null != cacheKeyTO) {
      String cacheKey = cacheKeyTO.getCacheKey();
      if(null != cacheKey && cacheKey.length() != 0) {
        try {
          try {
            int ex = result.getExpire();
            String hfield = cacheKeyTO.getHfield();
            if(null != hfield && hfield.length() != 0) {
              this.hashSet(cacheKey, hfield, result);
            } else if(ex == 0) {
              redisTemplate.opsForValue().set(cacheKey,result);
            } else {
              redisTemplate.opsForValue().set(cacheKey,result,ex,TimeUnit.SECONDS);
            }
          } catch (Exception var11) {
            log.error(var11.getMessage(), var11);
          }

        } finally {
          ;
        }
      }
    }
  }

  private void hashSet(String cacheKey, String hfield, CacheWrapper<Object> result) throws Exception {
    String key = cacheKey;
    String field = hfield;
    CacheWrapper<Object> val = result;
    int hExpire;
    if(this.hashExpire < 0) {
      hExpire = result.getExpire();
    } else {
      hExpire = this.hashExpire;
    }

    if(hExpire == 0) {
      redisTemplate.opsForHash().put(key, field, val);
    } else if(this.hashExpireByScript) {
      ArrayList keys = new ArrayList();
      keys.add(key);
      ArrayList args = new ArrayList();
      args.add(field);
      args.add(val);
      args.add(hExpire);
      this.redisTemplate.execute(redisScrip, keys, args);
    } else {
      redisTemplate.opsForHash().put(key, field, val);
      redisTemplate.expire(key, hExpire, TimeUnit.SECONDS);
    }

  }

  @Override
  public CacheWrapper<Object> get(CacheKeyTO cacheKeyTO, Method method, Object[] objects)
      throws CacheCenterConnectionException {
    if(null != this.redisTemplate && null != cacheKeyTO) {
      String cacheKey = cacheKeyTO.getCacheKey();
      if(null != cacheKey && cacheKey.length() != 0) {
        CacheWrapper res = null;

        try {
          try {
            Object ex = null;
            String hfield = cacheKeyTO.getHfield();
            if(null != hfield && hfield.length() != 0) {
              res = (CacheWrapper)this.redisTemplate.opsForHash().get(cacheKey,hfield);
            } else {
              res = (CacheWrapper)this.redisTemplate.opsForValue().get(cacheKey);
            }
          } catch (Exception var12) {
            log.error(var12.getMessage(), var12);
          }

          return res;
        } finally {
          ;
        }
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  @Override
  public void delete(CacheKeyTO cacheKeyTO) throws CacheCenterConnectionException {
    if(null != this.redisTemplate && null != cacheKeyTO) {
      String cacheKey = cacheKeyTO.getCacheKey();
      if(null != cacheKey && cacheKey.length() != 0) {
        log.debug("delete cache:" + cacheKey);

        try {
          try {
            String ex = cacheKeyTO.getHfield();
            if(null != ex && ex.length() != 0) {
              this.redisTemplate.opsForHash().delete(cacheKey,ex);
            } else {
              this.redisTemplate.delete(cacheKey);
            }

            this.getAutoLoadHandler().resetAutoLoadLastLoadTime(cacheKeyTO);
          } catch (Exception var7) {

            log.error("", var7);
          }

        } finally {
          ;
        }
      }
    }
  }

}
