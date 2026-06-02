package com.love.product.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * XSS 防护工具类
 * 使用 jsoup 过滤用户输入中的危险 HTML 内容
 */
public class XssUtils {

    /**
     * 过滤 HTML 标签，仅保留纯文本（默认策略，最安全）
     */
    public static String filter(String input) {
        if (input == null) {
            return null;
        }
        return Jsoup.clean(input, Safelist.none());
    }

    /**
     * 过滤 HTML，允许基本文本格式标签
     */
    public static String filterWithBasicTags(String input) {
        if (input == null) {
            return null;
        }
        return Jsoup.clean(input, Safelist.basic());
    }
}
