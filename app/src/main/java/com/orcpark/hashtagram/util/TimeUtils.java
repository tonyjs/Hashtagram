package com.orcpark.hashtagram.util;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JunSeon Park on 14. 3. 12.
 */
public class TimeUtils {
    public static String getRelativeTime(long createTime){
        return DateUtils.getRelativeTimeSpanString(
                createTime * 1000,
                System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
    }

    public static String getRelativeTime(Date createDate) {
        if (createDate == null) {
            return null;
        }
        return DateUtils.getRelativeTimeSpanString(
                createDate.getTime(),
                System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
    }

    public static String getTimeHourMinuteSecond(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm.ss");
        Date date = new Date(time * 1000);
        return dateFormat.format(date);
    }

    public static String getTimeUntilMinute(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        Date date = new Date(time * 1000);
        return dateFormat.format(date);
    }

    public static String getTimeUntilMinuteWithHyphen(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(time * 1000);
        return dateFormat.format(date);
    }

    public static String getTimeUntilMinuteWithDay(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd(E) HH:mm");
        Date date = new Date(time * 1000);
        return dateFormat.format(date);
    }

    public static String getTimeUntilMinuteWithMonth(long time){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd HH:mm");
        Date date = new Date(time * 1000);
        return dateFormat.format(date);
    }


    public static String getTimeUntilDay(long time){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time * 1000);
        return dateFormat.format(date);
    }

    public static String getTimeUntilDayWithComma(long time){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Date date = new Date(time * 1000);
        return dateFormat.format(date);
    }

    public static String getTimeUntilDayKorean(long time){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        Date date = new Date(time * 1000);
        return dateFormat.format(date);
    }

    public static String getTimeUntilDayWithRealTime(long time){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        return dateFormat.format(date);
    }

    public static String getTimeUntilMonthKorean(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 M월");
        Date date = new Date(time * 1000);
        return dateFormat.format(date);
    }

    public static String getTimeUntilMonthWithRealTime(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Date date = new Date(time * 1000);
        return dateFormat.format(date);
    }

    public static String getTimeUntilMonthWithRealTimeKorean(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월");
        Date date = new Date(time * 1000);
        return dateFormat.format(date);
    }

    public static String getTimeUntilWeek(long time){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM월 d 주차");
        Date date = new Date(time * 1000);
        return dateFormat.format(date);
    }

}
