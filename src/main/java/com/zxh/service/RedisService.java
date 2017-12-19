package com.zxh.service;

import java.util.List;
import java.util.Set;

/**
 * Created by admin on 2017/12/19.
 */
public interface RedisService {
    int MINUTE = 60;
    int HOUR = 60 * 60;
    int DAY = 24 * 60 * 60;
    int MONTH = 30 * DAY;

    /**
     * 放入缓存
     */
    void set(String area, String key, String value);

    /**
     * 缓存中是否存在
     */
    boolean exists(String area, String key);

    /**
     * 带指定超时时间的存入缓存
     * @param expireTime 秒为单位
     */
    void set(String area, String key, String value, int expireTime);

    /**
     * 放入缓存
     */
    void set(String area, String key, Object obj);

    /**
     * 带指定超时时间的存入缓存
     * @param expireTime 秒为单位
     */
    void set(String area, String key, Object obj, int expireTime);

    /**
     * 获取值
     */
    String get(String area, String key);

    /**
     * 获取对象
     */
    <T> T get(String area, String key, Class<T> clazz);

    <T> List<T> getList(String area, String key, Class<T> clazz);

    long ttl(String area, String key);


    /**
     * 删除对象
     */
    Long del(String area, String key);

    /**
     * 删除对象 尝试retryTimes次
     */
    boolean delWithRetry(String area, String key, int retryTimes);

    /**
     * 删除对象 尝试固定次数
     */
    boolean delWithRetry(String area, String key);

    /**
     * 放入缓存 尝试retryTimes
     */
    boolean setWithRetry(String area, String key, String value, int retryTimes, int expireTime);

    /**
     * 把Object类型数据放入缓存 尝试retryTimes
     */
    boolean setWithRetry(String area, String key, Object value, int retryTimes, int expireTime);

    /**
     * 放入缓存 尝试固定次数
     */
    boolean setWithRetry(String area, String key, String value, int expireTime);

    /**
     * 把Object类型放入缓存 尝试固定次数
     */
    boolean setWithRetry(String area, String key, Object value, int expireTime);

    /**
     * @Title: getIncr
     * @Description: Increment the number stored at key by one
     * @param: @param area
     * @param: @param key
     * @param: @return
     */
    Long incr(String area, String key);

    /**
     * key值减少1
     */
    Long decr(String area, String key);

    /**
     * @Title: getIncr
     * @Description: Increment the number stored at key by one
     * @param: @param area
     * @param: @param key
     * @param: @return
     */
    Long incrV2(String area, String key);

    /**
     * @Title: incrBy
     * @Description: the work just like INCR but instead to increment by 1 the increment is integer
     * @param: @param area
     * @param: @param key
     * @param: @param Integer
     * @param: @return
     */
    Long incrBy(String area, String key, int Integer);

    Set<String> keys(String pattern);

    /**
     * 根据key获取信息，在key前面添加任何信息
     */
    String get(String key);

    /**
     * 向一个set的数据结构增加一些成员，如果set的key不存在会新建一个set
     *
     * @warn 如果要添加的key不是set会报异常
     */
    Long sadd(String area, String key, String... members);

    /**
     * 添加一个带过期时间的 set
     */
    Long sadd(String area, String key, int seconds, String... members);

    /**
     * 获取set类型的指定key的所有member
     */
    Set<String> smembers(String area, String key);

    /**
     * 移除set的数据结构中的一些成员
     */
    Long srem(String area, String key, String... members);

    /**
     * 判断set中是否包含member成员
     */
    boolean sismember(String area, String key, String member);

    /**
     * 获取一个hash中的所有的key
     */
    Set<String> hkeys(String area, String key);

    /**
     * 删除一个hash中的一个key
     */
    Long hdel(String area, String key, String... entityKey);

    /**
     * 设置在map中设置一个entityKey,
     * 如果entityKey之前不存在设置成功,返回1
     * 如果entityKey之前存在设置失败,返回0
     *
     * @param key         hashmap的键值
     * @param entityKey   map中实体的key
     * @param entityValue map中实体的value
     */
    Long hsetnx(String area, String key, String entityKey, String entityValue);

    /**
     * 给一个键值加上过期时间
     */
    Long expire(String area, String key, int seconds);

    /**
     * 设定一个值,如果值不存在创建一个返回1,如果值存在不做操作返回0
     */
    long setnx(String area, String key, String value);


}
