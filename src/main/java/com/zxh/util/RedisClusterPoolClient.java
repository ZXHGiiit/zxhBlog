package com.zxh.util;

import com.google.common.base.Strings;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;


/**
 * Created by admin on 2017/12/19.
 */
public class RedisClusterPoolClient {
    private static JedisPool pool;

    public static String VERSION = "001";

    @Value("${redis.ip}")
    private static String ip = "zhouxinghang.com";
    @Value("${redis.port}")
    private static String port = "6379";
    static {

        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(200);
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);

            if (Strings.isNullOrEmpty(ip) || Strings.isNullOrEmpty(port)) {
                IllegalArgumentException e =
                        new IllegalArgumentException("[redis.properties ip or port] can not be null!");
                e.printStackTrace(System.err);
                throw e;
            }
            System.out.println("[Redis] ip:" + ip + " port:" + port);
            pool = new JedisPool(config, ip.trim(), Integer.valueOf(port.trim()));
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public static String set(String key, String value) {
        Jedis jedis = pool.getResource();
        String r = jedis.set(key, value);
        pool.returnResource(jedis);
        return r;
    }

    public static String get(String key) {
        Jedis jedis = pool.getResource();
        String r = jedis.get(key);
        pool.returnResource(jedis);
        return r;
    }

    public static Boolean exists(String key) {
        Jedis jedis = pool.getResource();
        Boolean r = jedis.exists(key);
        pool.returnResource(jedis);
        return r;
    }

    public static String type(String key) {
        Jedis jedis = pool.getResource();
        String r = jedis.type(key);
        pool.returnResource(jedis);
        return r;
    }

    public static Long expire(String key, int seconds) {
        Jedis jedis = pool.getResource();
        Long r = jedis.expire(key, seconds);
        pool.returnResource(jedis);
        return r;
    }

    public static Long expire(int seconds, String... keys) {
        long result = 0;
        for (String key : keys) {
            Jedis jedis = pool.getResource();
            result += jedis.expire(key, seconds);
            pool.returnResource(jedis);
        }
        return result;
    }

    public static Long expire(final Set<String> keys, int seconds) {
        long result = 0;
        for (String key : keys) {
            Jedis jedis = pool.getResource();
            result += jedis.expire(key, seconds);
            pool.returnResource(jedis);
        }
        return result;
    }

    public static Long expireBinary(final Set<byte[]> keys, int seconds) {
        long result = 0;
        for (byte[] key : keys) {
            Jedis jedis = pool.getResource();
            result += jedis.expire(key, seconds);
            pool.returnResource(jedis);
        }
        return result;
    }

    public static Long expireAt(String key, long unixTime) {
        Jedis jedis = pool.getResource();
        Long r = jedis.expireAt(key, unixTime);
        pool.returnResource(jedis);
        return r;
    }

    public static Long ttl(String key) {
        Jedis jedis = pool.getResource();
        Long r = jedis.ttl(key);
        pool.returnResource(jedis);
        return r;
    }

    public static Boolean setbit(String key, long offset, boolean value) {
        Jedis jedis = pool.getResource();
        boolean r = jedis.setbit(key, offset, value);
        pool.returnResource(jedis);
        return r;
    }

    public static Boolean getbit(String key, long offset) {
        Jedis jedis = pool.getResource();
        boolean r = jedis.getbit(key, offset);
        pool.returnResource(jedis);
        return r;
    }

    public static Long setrange(String key, long offset, String value) {
        Jedis jedis = pool.getResource();
        long r = jedis.setrange(key, offset, value);
        pool.returnResource(jedis);
        return r;
    }

    public static String getrange(String key, long startOffset, long endOffset) {

        Jedis jedis = pool.getResource();
        String r = jedis.getrange(key, startOffset, endOffset);
        pool.returnResource(jedis);
        return r;
    }

    public static String getSet(String key, String value) {

        Jedis jedis = pool.getResource();
        String r = jedis.getSet(key, value);
        pool.returnResource(jedis);
        return r;
    }

    public static Long setnx(String key, String value) {

        Jedis jedis = pool.getResource();
        Long r = jedis.setnx(key, value);
        pool.returnResource(jedis);
        return r;
    }

    public static String setex(String key, int seconds, String value) {

        Jedis jedis = pool.getResource();
        String r = jedis.setex(key, seconds, value);
        pool.returnResource(jedis);
        return r;
    }

    public static Long decrBy(String key, long integer) {
        Jedis jedis = pool.getResource();
        Long r = jedis.decrBy(key, integer);
        pool.returnResource(jedis);
        return r;

    }

    public static Long decr(String key) {

        Jedis jedis = pool.getResource();
        Long r = jedis.decr(key);
        pool.returnResource(jedis);
        return r;

    }

    public static Long incrBy(String key, long integer) {

        Jedis jedis = pool.getResource();
        Long r = jedis.incrBy(key, integer);
        pool.returnResource(jedis);
        return r;

    }

    public static Long incr(String key) {

        Jedis jedis = pool.getResource();
        Long r = jedis.incr(key);
        pool.returnResource(jedis);
        return r;

    }

    public static Long append(String key, String value) {

        Jedis jedis = pool.getResource();
        Long r = jedis.append(key, value);
        pool.returnResource(jedis);
        return r;

    }

    public static String substr(String key, int start, int end) {

        Jedis jedis = pool.getResource();
        String r = jedis.substr(key, start, end);
        pool.returnResource(jedis);
        return r;

    }

    public static Long hset(String key, String field, String value) {

        Jedis jedis = pool.getResource();
        Long r = jedis.hset(key, field, value);
        pool.returnResource(jedis);
        return r;

    }

    public static String hget(String key, String field) {

        Jedis jedis = pool.getResource();
        String r = jedis.hget(key, field);
        pool.returnResource(jedis);
        return r;
    }

    public static Long hsetnx(String key, String field, String value) {

        Jedis jedis = pool.getResource();
        Long r = jedis.hsetnx(key, field, value);
        pool.returnResource(jedis);
        return r;

    }

    public static String hmset(String key, Map<String, String> hash) {

        Jedis jedis = pool.getResource();
        String r = jedis.hmset(key, hash);
        pool.returnResource(jedis);
        return r;

    }

    public static List<String> hmget(String key, String... fields) {

        Jedis jedis = pool.getResource();
        List<String> r = jedis.hmget(key, fields);
        pool.returnResource(jedis);
        return r;

    }

    public static Long hincrBy(String key, String field, long value) {

        Jedis jedis = pool.getResource();
        Long r = jedis.hincrBy(key, field, value);
        pool.returnResource(jedis);
        return r;

    }

    public static Boolean hexists(String key, String field) {

        Jedis jedis = pool.getResource();
        Boolean r = jedis.hexists(key, field);
        pool.returnResource(jedis);
        return r;

    }

    public static Long hdel(String key, String... fields) {

        Jedis jedis = pool.getResource();
        Long r = jedis.hdel(key, fields);
        pool.returnResource(jedis);
        return r;

    }

    public static Long hlen(String key) {

        Jedis jedis = pool.getResource();
        Long r = jedis.hlen(key);
        pool.returnResource(jedis);
        return r;

    }

    public static Set<String> hkeys(String key) {

        Jedis jedis = pool.getResource();
        Set<String> r = jedis.hkeys(key);
        pool.returnResource(jedis);
        return r;

    }

    public static List<String> hvals(String key) {

        Jedis jedis = pool.getResource();
        List<String> r = jedis.hvals(key);
        pool.returnResource(jedis);
        return r;

    }

    public static Map<String, String> hgetAll(String key) {

        Jedis jedis = pool.getResource();
        Map<String, String> r = jedis.hgetAll(key);
        pool.returnResource(jedis);
        return r;

    }

    public static Long rpush(String key, String... strings) {

        Jedis jedis = pool.getResource();
        Long r = jedis.rpush(key, strings);
        pool.returnResource(jedis);
        return r;

    }

    public static Long lpush(String key, String... strings) {

        Jedis jedis = pool.getResource();
        Long r = jedis.lpush(key, strings);
        pool.returnResource(jedis);
        return r;

    }

    public static Long llen(String key) {

        Jedis jedis = pool.getResource();
        Long r = jedis.llen(key);
        pool.returnResource(jedis);
        return r;

    }

    public static List<String> lrange(String key, long start, long end) {

        Jedis jedis = pool.getResource();
        List<String> r = jedis.lrange(key, start, end);
        pool.returnResource(jedis);
        return r;

    }

    public static String ltrim(String key, long start, long end) {

        Jedis jedis = pool.getResource();
        String r = jedis.ltrim(key, start, end);
        pool.returnResource(jedis);
        return r;

    }

    public static String lindex(String key, long index) {

        Jedis jedis = pool.getResource();
        String r = jedis.lindex(key, index);
        pool.returnResource(jedis);
        return r;

    }

    public static String lset(String key, long index, String value) {
        Jedis jedis = pool.getResource();
        String r = jedis.lset(key, index, value);
        pool.returnResource(jedis);
        return r;
    }

    public static Long lrem(String key, long count, String value) {
        Jedis jedis = pool.getResource();
        Long r = jedis.lrem(key, count, value);
        pool.returnResource(jedis);
        return r;
    }

    public static String blpop(String key, final int timeoutSecs) {
        Jedis jedis = pool.getResource();
        List<String> r = jedis.blpop(timeoutSecs, key);
        pool.returnResource(jedis);
        if (r != null && r.size() == 2) {
            return r.get(1);
        } else {
            return null;
        }
    }

    public static String brpop(String key, final int timeoutSecs) {
        Jedis jedis = pool.getResource();
        List<String> r = jedis.brpop(timeoutSecs, key);
        pool.returnResource(jedis);
        if (r != null && r.size() == 2) {
            return r.get(1);
        } else {
            return null;
        }
    }

    public static byte[] blpop(byte[] key, final int timeoutSecs) {
        Jedis jedis = pool.getResource();
        List<byte[]> r = jedis.blpop(timeoutSecs, key);
        pool.returnResource(jedis);
        if (r != null && r.size() == 2) {
            return r.get(1);
        } else {
            return null;
        }
    }

    public static byte[] brpop(byte[] key, final int timeoutSecs) {
        Jedis jedis = pool.getResource();
        List<byte[]> r = jedis.brpop(timeoutSecs, key);
        pool.returnResource(jedis);
        if (r != null && r.size() == 2) {
            return r.get(1);
        } else {
            return null;
        }
    }

    public static String lpop(String key) {

        Jedis jedis = pool.getResource();
        String r = jedis.lpop(key);
        pool.returnResource(jedis);
        return r;
    }

    public static String rpop(String key) {

        Jedis jedis = pool.getResource();
        String r = jedis.rpop(key);
        pool.returnResource(jedis);
        return r;

    }

    public static Long sadd(String key, String... members) {

        Jedis jedis = pool.getResource();
        Long r = jedis.sadd(key, members);
        pool.returnResource(jedis);
        return r;

    }

    public static Long sadd(String key, int seconds, String... members) {
        Jedis jedis = pool.getResource();
        Long r = jedis.sadd(key, members);
        jedis.expire(key, seconds);
        pool.returnResource(jedis);
        return r;

    }

    public static Set<String> smembers(String key) {

        Jedis jedis = pool.getResource();
        Set<String> r = jedis.smembers(key);
        pool.returnResource(jedis);
        return r;

    }

    public static Long srem(String key, String... members) {

        Jedis jedis = pool.getResource();
        Long r = jedis.srem(key, members);
        pool.returnResource(jedis);
        return r;

    }

    public static String spop(String key) {

        Jedis jedis = pool.getResource();
        String r = jedis.spop(key);
        pool.returnResource(jedis);
        return r;

    }

    public static Long scard(String key) {

        Jedis jedis = pool.getResource();
        Long r = jedis.scard(key);
        pool.returnResource(jedis);
        return r;

    }

    public static Boolean sismember(String key, String member) {

        Jedis jedis = pool.getResource();
        Boolean r = jedis.sismember(key, member);
        pool.returnResource(jedis);
        return r;

    }

    public static String srandmember(String key) {

        Jedis jedis = pool.getResource();
        String r = jedis.srandmember(key);
        pool.returnResource(jedis);
        return r;

    }

    public static Long zadd(String key, double score, String member) {

        Jedis jedis = pool.getResource();
        Long r = jedis.zadd(key, score, member);
        pool.returnResource(jedis);
        return r;

    }

    public static Set<String> zrange(String key, int start, int end) {

        Jedis jedis = pool.getResource();
        Set<String> r = jedis.zrange(key, start, end);
        pool.returnResource(jedis);
        return r;

    }

    public static Long zrem(String key, String... members) {
        Jedis jedis = pool.getResource();
        Long r = jedis.zrem(key, members);
        pool.returnResource(jedis);
        return r;

    }

    public static Double zincrby(String key, double score, String member) {

        Jedis jedis = pool.getResource();
        Double r = jedis.zincrby(key, score, member);
        pool.returnResource(jedis);
        return r;

    }

    public static Long zrank(String key, String member) {

        Jedis jedis = pool.getResource();
        Long r = jedis.zrank(key, member);
        pool.returnResource(jedis);
        return r;

    }

    public static Long zrevrank(String key, String member) {

        Jedis jedis = pool.getResource();
        Long r = jedis.zrevrank(key, member);
        pool.returnResource(jedis);
        return r;

    }

    public static Set<String> zrevrange(String key, int start, int end) {

        Jedis jedis = pool.getResource();
        Set<String> r = jedis.zrevrange(key, start, end);
        pool.returnResource(jedis);
        return r;
    }

    public static Set<Tuple> zrangeWithScores(String key, int start, int end) {

        Jedis jedis = pool.getResource();
        Set<Tuple> r = jedis.zrangeWithScores(key, start, end);
        pool.returnResource(jedis);
        return r;

    }

    public static Set<Tuple> zrevrangeWithScores(String key, int start, int end) {

        Jedis jedis = pool.getResource();
        Set<Tuple> r = jedis.zrevrangeWithScores(key, start, end);
        pool.returnResource(jedis);
        return r;
    }

    public static Long zcard(String key) {

        Jedis jedis = pool.getResource();
        Long r = jedis.zcard(key);
        pool.returnResource(jedis);
        return r;

    }

    public static Double zscore(String key, String member) {

        Jedis jedis = pool.getResource();
        Double r = jedis.zscore(key, member);
        pool.returnResource(jedis);
        return r;

    }

    public static List<String> sort(String key) {

        Jedis jedis = pool.getResource();
        List<String> r = jedis.sort(key);
        pool.returnResource(jedis);
        return r;

    }

    public static List<String> sort(String key, SortingParams sortingParameters) {

        Jedis jedis = pool.getResource();
        List<String> r = jedis.sort(key, sortingParameters);
        pool.returnResource(jedis);
        return r;

    }

    public static Long zcount(String key, double min, double max) {

        Jedis jedis = pool.getResource();
        Long r = jedis.zcount(key, min, max);
        pool.returnResource(jedis);
        return r;
    }

    public static Set<String> zrangeByScore(String key, double min, double max) {

        Jedis jedis = pool.getResource();
        Set<String> r = jedis.zrangeByScore(key, min, max);
        pool.returnResource(jedis);
        return r;
    }

    public static Set<String> zrevrangeByScore(String key, double max, double min) {

        Jedis jedis = pool.getResource();
        Set<String> r = jedis.zrevrangeByScore(key, max, min);
        pool.returnResource(jedis);
        return r;

    }

    public static Set<String> zrangeByScore(String key, double min, double max, int offset, int
            count) {

        Jedis jedis = pool.getResource();
        Set<String> r = jedis.zrangeByScore(key, min, max, offset, count);
        pool.returnResource(jedis);
        return r;

    }

    public static Set<String> zrevrangeByScore(String key, double max, double min, int offset,
                                               int count) {

        Jedis jedis = pool.getResource();
        Set<String> r = jedis.zrevrangeByScore(key, max, min, offset, count);
        pool.returnResource(jedis);
        return r;

    }

    public static Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {

        Jedis jedis = pool.getResource();
        Set<Tuple> r = jedis.zrangeByScoreWithScores(key, min, max);
        pool.returnResource(jedis);
        return r;
    }

    public static Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {

        Jedis jedis = pool.getResource();
        Set<Tuple> r = jedis.zrevrangeByScoreWithScores(key, max, min);
        pool.returnResource(jedis);
        return r;
    }

    public static Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset,
                                                     int count) {

        Jedis jedis = pool.getResource();
        Set<Tuple> r = jedis.zrangeByScoreWithScores(key, min, max, offset, count);
        pool.returnResource(jedis);
        return r;

    }

    public static Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min,
                                                        int offset, int count) {

        Jedis jedis = pool.getResource();
        Set<Tuple> r = jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
        pool.returnResource(jedis);
        return r;

    }

    public static Long zremrangeByRank(String key, int start, int end) {

        Jedis jedis = pool.getResource();
        Long r = jedis.zremrangeByRank(key, start, end);
        pool.returnResource(jedis);
        return r;

    }

    public static Long zremrangeByScore(String key, double start, double end) {

        Jedis jedis = pool.getResource();
        Long r = jedis.zremrangeByScore(key, start, end);
        pool.returnResource(jedis);
        return r;

    }

    public static Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String
            value) {

        Jedis jedis = pool.getResource();
        Long r = jedis.linsert(key, where, pivot, value);
        pool.returnResource(jedis);
        return r;

    }

    public static Long del(String... keys) {

        Jedis jedis = pool.getResource();
        Long r = jedis.del(keys);
        pool.returnResource(jedis);
        return r;
    }

    public static Set<String> keys(String pattern) {
        Jedis jedis = pool.getResource();
        Set<String> patternKeys = jedis.keys(pattern);
        pool.returnResource(jedis);
        return patternKeys;
    }

    public static void main(String[] args) {
        RedisClusterPoolClient.set("hello", "zhouxinghang");
        System.out.println(RedisClusterPoolClient.get("hello"));
        System.out.println(RedisClusterPoolClient.del("hello"));
        System.out.println(RedisClusterPoolClient.get("hello"));
    }
}
