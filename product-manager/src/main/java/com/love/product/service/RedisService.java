package com.love.product.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
public interface RedisService {

    /**
     * 设置缓存，指定过期时间

     *
     * @param s         缓存键

     * @param num       过期时间数值

     * @param i         过期时间单位

     */
    void set(String s, long num, Long i, TimeUnit timeUnit);

    /**
     * 设置缓存，指定过期时间（单位：毫秒）
     *
     * @param key       缓存键

     * @param value     缓存值

     * @param time      过期时间

     */
    void set(String key, Object value, long time);

    /**
     * 设置缓存，不指定过期时间

     *
     * @param key       缓存键

     * @param value     缓存值

     */
    void set(String key, Object value);

    /**
     * 获取缓存值

     *
     * @param key       缓存键

     * @return          缓存值

     */
    Object get(String key);

    /**
     * 删除缓存

     *
     * @param key       缓存键

     * @return          删除成功返回true，否则返回false

     */
    Boolean del(String key);

    /**
     * 批量删除缓存

     *
     * @param keys      缓存键列表

     * @return          成功删除的缓存数量

     */
    Long del(List<String> keys);

    /**
     * 设置缓存的过期时间

     *
     * @param key       缓存键

     * @param time      过期时间（单位：毫秒）
     * @return          设置成功返回true，否则返回false

     */
    Boolean expire(String key, long time);

    /**
     * 设置缓存的过期时间

     *
     * @param key       缓存键

     * @param time      过期时间数值

     * @param timeUnit  过期时间单位

     * @return          设置成功返回true，否则返回false

     */
    Boolean expire(String key, long time, TimeUnit timeUnit);

    /**
     * 获取缓存的过期时间

     *
     * @param key       缓存键

     * @return          缓存的过期时间（单位：毫秒）
     */
    Long getExpire(String key);

    /**
     * 判断缓存是否存在

     *
     * @param key       缓存键

     * @return          存在返回true，否则返回false

     */
    Boolean hasKey(String key);

    /**
     * 对缓存中的值进行增加操作

     *
     * @param key       缓存键

     * @param delta     增加的值

     * @return          增加后的值

     */
    Long incr(String key, long delta);

    /**
     * 对缓存中的值进行增加操作，并设置过期时间

     *
     * @param key       缓存键

     * @param time      过期时间（单位：毫秒）
     * @return          增加后的值

     */
    Long incrExpire(String key, long time);

    /**
     * 对缓存中的值进行减少操作

     *
     * @param key       缓存键

     * @param delta     减少的值

     * @return          减少后的值

     */
    Long decr(String key, long delta);

    /**
     * 将成员和分数添加到Redis中的有序集合中。
     * @param key 有序集合的键。
     * @param value 要添加的成员的值。
     * @param score 成员的分数。
     */
    void zSetAdd(String key, String value, double score);
    /**
     * 从 Redis 中的排序集中获取一系列成员
     * @param key 排序集的键
     * @param start 范围的起始索引
     * @param end 范围的结束索引
     * @return Set<Long>
     */
    Set<Long> zSetRange(String key, long start, long end);

    /**
     * 获取哈希表中指定字段的值

     *
     * @param key       缓存键

     * @param hashKey   哈希表字段

     * @return          哈希表字段对应的值

     */
    Object hGet(String key, String hashKey);

    /**
     * 设置哈希表中指定字段的值，并设置过期时间

     *
     * @param key       缓存键

     * @param hashKey   哈希表字段

     * @param value     哈希表字段对应的值

     * @param time      过期时间（单位：毫秒）
     * @return          设置成功返回true，否则返回false

     */
    Boolean hSet(String key, String hashKey, Object value, long time);

    /**
     * 设置哈希表中指定字段的值

     *
     * @param key       缓存键

     * @param hashKey   哈希表字段

     * @param value     哈希表字段对应的值

     */
    void hSet(String key, String hashKey, Object value);

    /**
     * 获取哈希表中所有字段和值

     *
     * @param key       缓存键

     * @return          哈希表中所有字段和值的映射关系

     */
    Map<String, Object> hGetAll(String key);

    /**
     * 设置哈希表中多个字段和值，并设置过期时间

     *
     * @param key       缓存键

     * @param map       哈希表字段和值的映射关系

     * @param time      过期时间（单位：毫秒）
     * @return          设置成功返回true，否则返回false

     */
    Boolean hSetAll(String key, Map<String, Object> map, long time);

    /**
     * 设置哈希表中多个字段和值

     *
     * @param key       缓存键

     * @param map       哈希表字段和值的映射关系

     */
    void hSetAll(String key, Map<String, ?> map);

    /**
     * 删除哈希表中的字段

     *
     * @param key       缓存键

     * @param hashKey   哈希表字段

     */
    void hDel(String key, Object... hashKey);

    /**
     * 判断哈希表中是否存在指定字段

     *
     * @param key       缓存键

     * @param hashKey   哈希表字段

     * @return          存在返回true，否则返回false

     */
    Boolean hHasKey(String key, String hashKey);

    /**
     * 对哈希表中的字段进行增加操作

     *
     * @param key       缓存键

     * @param hashKey   哈希表字段

     * @param delta     增加的值

     * @return          增加后的值

     */
    Long hIncr(String key, String hashKey, Long delta);



    /**
     * 对哈希表中的字段进行减少操作

     *
     * @param key       缓存键

     * @param hashKey   哈希表字段

     * @param delta     减少的值

     * @return          减少后的值

     */
    Long hDecr(String key, String hashKey, Long delta);

    /**
     * 对有序集合中的成员进行增加操作

     *
     * @param key       缓存键

     * @param value     有序集合成员

     * @param score     有序集合分数

     * @return          增加后的分数

     */
    Double zIncr(String key, Object value, Double score);

    /**
     * 对有序集合中的成员进行减少操作

     *
     * @param key       缓存键

     * @param value     有序集合成员

     * @param score     有序集合分数

     * @return          减少后的分数

     */
    Double zDecr(String key, Object value, Double score);

    /**
     * 获取有序集合中指定范围内的成员和分数（按照分数从高到低排序）
     *
     * @param key       缓存键

     * @param start     起始位置

     * @param end       结束位置

     * @return          成员和分数的映射关系

     */
    Map<Object, Double> zReverseRangeWithScore(String key, long start, long end);

    /**
     * 获取有序集合中指定成员的分数

     *
     * @param key       缓存键

     * @param value     有序集合成员

     * @return          成员的分数

     */
    Double zScore(String key, Object value);

    /**
     * 获取有序集合中所有成员和分数

     *
     * @param key       缓存键

     * @return          成员和分数的映射关系

     */
    Map<Object, Double> zAllScore(String key);

    /**
     * 获取集合中的所有成员

     *
     * @param key       缓存键

     * @return          集合中的所有成员

     */
    Set<Object> sMembers(String key);

    /**
     * 向集合中添加成员

     *
     * @param key       缓存键

     * @param values    需要添加的成员

     * @return          添加成功的成员数量

     */
    Long sAdd(String key, Object... values);

    /**
     * 向集合中添加成员，并设置过期时间

     *
     * @param key       缓存键

     * @param time      过期时间（单位：毫秒）
     * @param values    需要添加的成员

     * @return          添加成功的成员数量

     */
    Long sAddExpire(String key, long time, Object... values);



    /**
     * 判断集合中是否存在指定的元素

     *
     * @param key   集合的键名

     * @param value 要判断的元素值

     * @return 如果集合中存在指定的元素，则返回true；否则返回false

     */
    Boolean sIsMember(String key, Object value);

    /**
     * 获取集合的大小（元素数量）
     *
     * @param key 集合的键名

     * @return 集合的大小

     */
    Long sSize(String key);

    /**
     * 从集合中移除一个或多个元素

     *
     * @param key    集合的键名

     * @param values 要移除的元素值

     * @return 被成功移除的元素数量

     */
    Long sRemove(String key, Object... values);

    /**
     * 获取列表中指定范围内的元素

     *
     * @param key   列表的键名

     * @param start 起始索引（包含）
     * @param end   结束索引（包含）
     * @return 指定范围内的元素列表

     */
    List<Object> lRange(String key, long start, long end);

    /**
     * 获取列表的大小（元素数量）
     *
     * @param key 列表的键名

     * @return 列表的大小

     */
    Long lSize(String key);

    /**
     * 获取列表中指定索引位置的元素

     *
     * @param key   列表的键名

     * @param index 索引位置

     * @return 指定索引位置的元素

     */
    Object lIndex(String key, long index);

    /**
     * 在列表的头部插入一个元素

     *
     * @param key   列表的键名

     * @param value 要插入的元素值

     * @return 插入后列表的大小

     */
    Long lPush(String key, Object value);

    /**
     * 在列表的头部插入一个带有过期时间的元素

     *
     * @param key   列表的键名

     * @param value 要插入的元素值

     * @param time  过期时间（单位：秒）
     * @return 插入后列表的大小

     */
    Long lPush(String key, Object value, long time);

    /**
     * 在列表的头部插入多个元素

     *
     * @param key    列表的键名

     * @param values 要插入的元素值数组

     * @return 插入后列表的大小

     */
    Long lPushAll(String key, Object... values);

    /**
     * 在列表的头部插入多个带有过期时间的元素

     *
     * @param key    列表的键名

     * @param time   过期时间（单位：秒）
     * @param values 要插入的元素值数组

     * @return 插入后列表的大小

     */
    Long lPushAll(String key, Long time, Object... values);

    /**
     * 从列表中移除指定数量的元素

     *
     * @param key   列表的键名

     * @param count 移除的数量，正数表示从头部开始移除，负数表示从尾部开始移除

     * @param value 要移除的元素值

     * @return 被成功移除的元素数量

     */
    Long lRemove(String key, long count, Object value);

    /**
     * 在指定偏移量上设置位值

     *
     * @param key    键名

     * @param offset 偏移量

     * @param b      位值（true表示1，false表示0）
     * @return 设置成功返回true；否则返回false

     */
    Boolean bitAdd(String key, int offset, boolean b);

    /**
     * 获取指定偏移量上的位值

     *
     * @param key    键名

     * @param offset 偏移量

     * @return 指定偏移量上的位值（true表示1，false表示0）
     */
    Boolean bitGet(String key, int offset);

    /**
     * 统计指定键名对应的位图中的1的个数

     *
     * @param key 键名

     * @return 位图中1的个数

     */
    Long bitCount(String key);

    /**
     * 对指定键名对应的位图进行位操作

     *
     * @param key     键名

     * @param limit   限制返回结果的数量

     * @param offset  结果集的偏移量

     * @return 位操作的结果列表

     */
    List<Long> bitField(String key, int limit, int offset);

    /**
     * 获取指定键名对应的位图的所有位值

     *
     * @param key 键名

     * @return 位图的所有位值

     */
    byte[] bitGetAll(String key);

    /**
     * 将一个或多个元素添加到HyperLogLog中

     *
     * @param key    键名

     * @param values 要添加的元素值

     * @return 添加成功的元素数量

     */
    Long hyperAdd(String key, Object... values);

    /**
     * 获取HyperLogLog的基数估算值

     *
     * @param key 键名

     * @return HyperLogLog的基数估算值

     */
    Long hyperGet(String... key);

    /**
     * 删除指定键名对应的HyperLogLog

     *
     * @param key 键名

     */
    void hyperDel(String key);

    /**
     * 将一个地理位置添加到指定键名对应的地理位置集合中

     *
     * @param key   键名

     * @param x     经度

     * @param y     纬度

     * @param name  地理位置名称

     * @return 添加成功的地理位置数量

     */
    Long geoAdd(String key, Double x, Double y, String name);

    /**
     * 获取指定地理位置集合中多个地理位置的坐标

     *
     * @param key    键名

     * @param place  地理位置名称数组

     * @return 地理位置的坐标列表

     */
    List<Point> geoGetPointList(String key, Object... place);

    /**
     * 计算两个地理位置之间的距离

     *
     * @param key       键名

     * @param placeOne  第一个地理位置名称

     * @param placeTow  第二个地理位置名称

     * @return 两个地理位置之间的距离

     */
    Distance geoCalculationDistance(String key, String placeOne, String placeTow);

    /**
     * 在指定地理位置集合中，根据给定的地理位置和距离，返回与其相邻的地理位置

     *
     * @param key      键名

     * @param place    地理位置名称

     * @param distance 距离范围

     * @param limit    返回结果的数量限制

     * @param sort     结果排序方式（升序或降序）
     * @return 与给定地理位置相邻的地理位置列表

     */
    GeoResults<RedisGeoCommands.GeoLocation<Object>> geoNearByPlace(String key, String place, Distance distance, long limit, Sort.Direction sort);

    /**
     * 获取指定地理位置集合中多个地理位置的哈希值

     *
     * @param key   键名

     * @param place 地理位置名称数组

     * @return 地理位置的哈希值列表

     */
    List<String> geoGetHash(String key, String... place);

}

