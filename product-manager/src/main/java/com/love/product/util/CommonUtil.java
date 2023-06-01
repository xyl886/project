package com.love.product.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.love.product.constant.CommonConstant.EMAIL_REGEX;
import static com.love.product.constant.CommonConstant.PASSWORD_REGEX;

public class CommonUtil {

    public static boolean checkEmail(String email) {
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(EMAIL_REGEX);
        //正则表达式的匹配器
        Matcher m = p.matcher(email);
        //进行正则匹配
        return m.matches();
    }

    // 匹配由数字、字母和特殊字符组成的密码，长度在 6 到 20 之间
    public static boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
        }
    public static String getBracketsContent(String str) {
        return str.substring(str.indexOf("(") + 1, str.indexOf(")"));
    }

    public static String getRandomCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return result;
    }

    public static <T> Set<T> castSet(Object obj, Class<T> clazz) {
        Set<T> result = new HashSet<>();
        if (obj instanceof Set<?>) {
            for (Object o : (Set<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return result;
    }

}
