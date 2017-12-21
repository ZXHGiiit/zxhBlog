package com.zxh.service.impl;

import com.google.common.collect.Lists;
import com.zxh.service.RedisService;
import com.zxh.util.JacksonUtils;
import com.zxh.util.RedisClusterPoolClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by admin on 2017/12/19.
 */

@Service
public class RedisServiceImpl implements RedisService {
    private static final Logger LOG = LoggerFactory.getLogger(RedisServiceImpl.class);
    @Value("${spring.redis.version}")
    private String VERSION;
    @Autowired
    private RedisClusterPoolClient redisClusterPoolClient;

    public void set(String area, String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        key = area.trim() + key.trim();
        try {
            redisClusterPoolClient.set(VERSION + key,
                    value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public boolean exists(String area, String key) {
        key = area.trim() + key.trim();
        try {
            return redisClusterPoolClient.exists(VERSION
                    + key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }

    public void set(String area, String key, String value, int expireTime) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        key = area.trim() + key.trim();
        try {
            redisClusterPoolClient.setex(
                    VERSION + key, expireTime, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public void set(String area, String key, Object obj) {
        if (obj == null) {
            return;
        }
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        key = area.trim() + key.trim();
        try {
            redisClusterPoolClient.set(VERSION + key,
                    JacksonUtils.toJson(obj));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public void set(String area, String key, Object obj, int expireTime) {
        if (obj == null) {
            return;
        }
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        key = area.trim() + key.trim();
        try {
            redisClusterPoolClient.setex(
                    VERSION + key, expireTime,
                    JacksonUtils.toJson(obj));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public String get(String area, String key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        key = area.trim() + key.trim();
        try {
            return redisClusterPoolClient.get(VERSION
                    + key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    public String getSet(String area, String key, String value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("key or value is null");
        }
        key = area.trim() + key.trim();
        try {
            return redisClusterPoolClient.getSet(VERSION
                    + key, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    public <T> T get(String area, String key, Class<T> clazz) {
        try {
            if (key == null) {
                throw new IllegalArgumentException("key is null");
            }
            key = area.trim() + key.trim();
            String value =
                    redisClusterPoolClient.get(VERSION
                            + key);
            if (value != null) {
                return JacksonUtils.jsonToPojo(value, clazz);
            }
            return null;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    public <T> List<T> getList(String area, String key, Class<T> clazz) {
        try {
            key = area.trim() + key.trim();
            List<String> list =
                    redisClusterPoolClient.lrange(
                            VERSION + key, 0, -1);
            if (list == null) {
                return null;
            }

            List<T> result = Lists.newArrayListWithExpectedSize(list.size());
            for (String string : list) {
                result.add(JacksonUtils.jsonToPojo(string, clazz));
            }

            return result;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    public long ttl(String area, String key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        key = area.trim() + key.trim();
        try {
            return redisClusterPoolClient.ttl(VERSION
                    + key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return 0;
        }
    }

    public Long del(String area, String key) {
        key = area.trim() + key.trim();

        try {
            return redisClusterPoolClient.del(VERSION
                    + key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return 0L;
        }
    }

    public boolean delWithRetry(String area, String key, int retryTimes) {
        int retry = 1;
        while (exists(area, key) && (retry++ < retryTimes)) {
            del(area, key);
        }
        return !exists(area, key);
    }

    public boolean delWithRetry(String area, String key) {
        return delWithRetry(area, key, 3);
    }

    public boolean setWithRetry(String area, String key, String value, int retryTimes, int
            expireTime) {
        int retry = 1;
        do {
            set(area, key, value, expireTime);
        } while (!exists(area, key) && (retry++ < retryTimes));

        return exists(area, key);
    }

    public boolean setWithRetry(String area, String key, Object value, int retryTimes, int
            expireTime) {
        int retry = 1;
        do {
            set(area, key, value, expireTime);
        } while (!exists(area, key) && (retry++ < retryTimes));

        return exists(area, key);
    }

    public boolean setWithRetry(String area, String key, String value, int expireTime) {
        return setWithRetry(area, key, value, 3, expireTime);
    }

    public boolean setWithRetry(String area, String key, Object value, int expireTime) {
        return setWithRetry(area, key, value, 3, expireTime);
    }

    public Long incr(String area, String key) {
        try {
            key = area.trim() + key.trim();
            return redisClusterPoolClient.incr(VERSION + key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return 0L;
        }
    }

    public Long decr(String area, String key) {
        try {
            key = area.trim() + key.trim();
            return redisClusterPoolClient.decr(VERSION + key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return 0L;
        }
    }

    public Long incrV2(String area, String key) {
        try {
            key = area.trim() + key.trim();
            return redisClusterPoolClient.incr(VERSION + key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return 0L;
        }
    }

    public Long incrBy(String area, String key, int Integer) {
        try {
            key = area.trim() + key.trim();
            return redisClusterPoolClient.incrBy(VERSION + key, Integer);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return 0L;
        }
    }

    public Set<String> keys(String pattern) {
        try {
            return redisClusterPoolClient.keys(pattern);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return new HashSet<String>();
        }

    }

    public String get(String key) {
        try {
            return redisClusterPoolClient.get(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    public Long sadd(String area, String key, String... members) {
        String newKey = area.trim() + key.trim();
        Long columns = 0l;
        try {
            columns = redisClusterPoolClient.sadd(VERSION + newKey,
                    members);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return columns;
    }

    public Long sadd(String area, String key, int seconds, String... members) {
        String newKey = area.trim() + key.trim();
        Long columns = 0l;
        try {
            columns = redisClusterPoolClient.sadd(VERSION + newKey, seconds,
                    members);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return columns;
    }

    public Set<String> smembers(String area, String key) {
        String newKey = area.trim() + key.trim();

        Set<String> members = null;
        try {
            members = redisClusterPoolClient.smembers(VERSION + newKey);
            if (members == null) {
                members = Collections.emptySet();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return members;
    }

    public Long srem(String area, String key, String... members) {
        String newKey = area.trim() + key.trim();
        Long columns = 0l;
        try {
            columns = redisClusterPoolClient.srem(VERSION + newKey,
                    members);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return columns;
    }

    public boolean sismember(String area, String key, String member) {
        String newKey = area.trim() + key.trim();
        try {
            return redisClusterPoolClient.sismember(VERSION + newKey,
                    member);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }

    public Set<String> hkeys(String area, String key) {
        String newKey = area.trim() + key.trim();
        try {
            return redisClusterPoolClient.hkeys(VERSION + newKey);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Collections.emptySet();
    }

    public Long hdel(String area, String key, String... entityKey) {
        String newKey = area.trim() + key.trim();
        try {
            return redisClusterPoolClient.hdel(VERSION + newKey, entityKey);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return 0L;
    }

    public Long hsetnx(String area, String key, String entityKey, String entityValue) {
        String newKey = area.trim() + key.trim();
        try {
            return redisClusterPoolClient.hsetnx(VERSION + newKey, entityKey,
                    entityValue);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return 0L;
    }

    public Long expire(String area, String key, int seconds) {
        String newKey = area.trim() + key.trim();
        Long columns = 0l;
        try {
            columns = redisClusterPoolClient.expire(VERSION + newKey,
                    seconds);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return columns;
    }

    public long setnx(String area, String key, String value) {
        String newKey = area.trim() + key.trim();
        long result = 0l;
        try {
            result = redisClusterPoolClient.setnx(VERSION + newKey, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }
}
