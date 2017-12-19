package com.zxh.service.impl;

import com.google.common.collect.Lists;
import com.zxh.service.RedisService;
import com.zxh.util.JacksonUtils;
import com.zxh.util.RedisClusterPoolClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by admin on 2017/12/19.
 */
public class RedisServiceImpl implements RedisService {
    private static final Logger LOG = LoggerFactory.getLogger(RedisServiceImpl.class);


    public void set(String area, String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }
        key = area.trim() + key.trim();
        try {
            RedisClusterPoolClient.set(RedisClusterPoolClient.VERSION + key,
                    value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public boolean exists(String area, String key) {
        key = area.trim() + key.trim();
        try {
            return RedisClusterPoolClient.exists(RedisClusterPoolClient.VERSION
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
            RedisClusterPoolClient.setex(
                    RedisClusterPoolClient.VERSION + key, expireTime, value);
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
            RedisClusterPoolClient.set(RedisClusterPoolClient.VERSION + key,
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
            RedisClusterPoolClient.setex(
                    RedisClusterPoolClient.VERSION + key, expireTime,
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
            return RedisClusterPoolClient.get(RedisClusterPoolClient.VERSION
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
            return RedisClusterPoolClient.getSet(RedisClusterPoolClient.VERSION
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
                    RedisClusterPoolClient.get(RedisClusterPoolClient.VERSION
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
                    RedisClusterPoolClient.lrange(
                            RedisClusterPoolClient.VERSION + key, 0, -1);
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
            return RedisClusterPoolClient.ttl(RedisClusterPoolClient.VERSION
                    + key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return 0;
        }
    }

    public Long del(String area, String key) {
        key = area.trim() + key.trim();

        try {
            return RedisClusterPoolClient.del(RedisClusterPoolClient.VERSION
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
            return RedisClusterPoolClient.incr(RedisClusterPoolClient.VERSION + key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return 0L;
        }
    }

    public Long decr(String area, String key) {
        try {
            key = area.trim() + key.trim();
            return RedisClusterPoolClient.decr(RedisClusterPoolClient.VERSION + key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return 0L;
        }
    }

    public Long incrV2(String area, String key) {
        try {
            key = area.trim() + key.trim();
            return RedisClusterPoolClient.incr(RedisClusterPoolClient.VERSION + key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return 0L;
        }
    }

    public Long incrBy(String area, String key, int Integer) {
        try {
            key = area.trim() + key.trim();
            return RedisClusterPoolClient.incrBy(RedisClusterPoolClient.VERSION + key, Integer);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return 0L;
        }
    }

    public Set<String> keys(String pattern) {
        try {
            return RedisClusterPoolClient.keys(pattern);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return new HashSet<String>();
        }

    }

    public String get(String key) {
        try {
            return RedisClusterPoolClient.get(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    public Long sadd(String area, String key, String... members) {
        String newKey = area.trim() + key.trim();
        Long columns = 0l;
        try {
            columns = RedisClusterPoolClient.sadd(RedisClusterPoolClient.VERSION + newKey,
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
            columns = RedisClusterPoolClient.sadd(RedisClusterPoolClient.VERSION + newKey, seconds,
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
            members = RedisClusterPoolClient.smembers(RedisClusterPoolClient.VERSION + newKey);
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
            columns = RedisClusterPoolClient.srem(RedisClusterPoolClient.VERSION + newKey,
                    members);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return columns;
    }

    public boolean sismember(String area, String key, String member) {
        String newKey = area.trim() + key.trim();
        try {
            return RedisClusterPoolClient.sismember(RedisClusterPoolClient.VERSION + newKey,
                    member);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }

    public Set<String> hkeys(String area, String key) {
        String newKey = area.trim() + key.trim();
        try {
            return RedisClusterPoolClient.hkeys(RedisClusterPoolClient.VERSION + newKey);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return Collections.emptySet();
    }

    public Long hdel(String area, String key, String... entityKey) {
        String newKey = area.trim() + key.trim();
        try {
            return RedisClusterPoolClient.hdel(RedisClusterPoolClient.VERSION + newKey, entityKey);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return 0L;
    }

    public Long hsetnx(String area, String key, String entityKey, String entityValue) {
        String newKey = area.trim() + key.trim();
        try {
            return RedisClusterPoolClient.hsetnx(RedisClusterPoolClient.VERSION + newKey, entityKey,
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
            columns = RedisClusterPoolClient.expire(RedisClusterPoolClient.VERSION + newKey,
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
            result = RedisClusterPoolClient.setnx(RedisClusterPoolClient.VERSION + newKey, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }
}
