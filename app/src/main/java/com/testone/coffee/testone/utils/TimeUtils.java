package com.testone.coffee.testone.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Coffee on 2016/3/1.
 */
public class TimeUtils {
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DATE_FORMAT_DATE);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * get the number of time
     */
    public static String turnToNumberBymils(long timemils) {
        long currenttime = Calendar.getInstance().getTimeInMillis();
        long div = currenttime - timemils;
        int min = (int) (div / 1000 / 60);
        if (min >= 0 && min <= 1) {
            return "";
        } else if (min > 1 && min < 60) {
            return min + "m";
        } else if (min >= 60) {
            int hh = min / 60;
            if (hh < 24) {
                return hh + "h";
            } else {
                return TimeUtils.getTime(timemils);
            }

        } else {
            return "";
        }
    }


    public static String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    public static long getCurrentUtime(){
        return System.currentTimeMillis();
    }
    public static boolean isTodayTheNextDay(long currenttime,long lasttime){
        long div = 24*60*3600*1000;
        return (currenttime - lasttime)<=div ? true : false;
    }

}
