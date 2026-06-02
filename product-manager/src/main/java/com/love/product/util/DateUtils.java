package com.love.product.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间操作工具类
 */
public class DateUtils {

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    private DateUtils() {
    }

    /**
     * 获取过去一年内的日期列表（每天）
     */
    public static List<String> getMonths() {
        List<String> dateList = new ArrayList<>();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        dateList.add(formateDate(calendar.getTime(), YYYY_MM_DD));
        while (date.after(calendar.getTime())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(formateDate(calendar.getTime(), YYYY_MM_DD));
        }
        return dateList;
    }

    /**
     * 把日期按指定格式转换成字符串
     */
    public static String formateDate(Date date, String code) {
        SimpleDateFormat formate = new SimpleDateFormat(code);
        return formate.format(date);
    }
}
