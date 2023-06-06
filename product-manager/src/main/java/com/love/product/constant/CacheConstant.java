package com.love.product.constant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @date 2023-01-03 16:08
 * @describe 缓存根目录
 */

public class CacheConstant implements Serializable {


    //为了减少查询数据库的次数，把用户的一些信息暂时存放到这里
    public static Map<String, Object> USER_MAP = null;
    public static Map<String, Long> CACHE = new HashMap<>(); // 缓存数据
}
