package com.bjpowernode.crm.commons.utils;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 对日期进行处理的工具类
 * 不用创建对象，直接使用类名调用方法，所以用static修饰
 */
public class DateUtils {
    /**
     * 对指定的对象进行格式化：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatDateTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(new Date());
        return dateStr;
    }

    /**
     * 对指定的对象进行格式化：yyyy-MM-dd
     * @return
     */
    public static String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        return dateStr;
    }

    /**
     * 对指定的对象进行格式化：HH:mm:ss
     * @return
     */
    public static String formatTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dateStr = sdf.format(new Date());
        return dateStr;
    }
}
