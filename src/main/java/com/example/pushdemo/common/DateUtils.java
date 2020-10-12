package com.example.pushdemo.common;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 林杰炜 Linjw
 * @Title TODO 类描述
 * @date 2020/9/27 10:44
 */
public class DateUtils {

    public static String getStringDateShort() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }

    public static Date strToDateLong(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.parse(strDate, new ParsePosition(0));
    }

    public static String getStringShortToday() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(new Date());
    }
}
