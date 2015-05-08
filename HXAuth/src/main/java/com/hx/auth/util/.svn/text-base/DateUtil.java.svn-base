package com.hx.auth.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Renyulin on 14-4-16 下午5:53.
 */
public class DateUtil {
    public static final int PATTERNTYPE_DATE = 0;
    public static final int PATTERNTYPE_TIME = 1;
    private static SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 日期增加
     *
     * @param date
     * @param column
     * @param value
     * @return
     */
    public static Date addDate(Date date, int column, int value) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(column, value);
        return calendar.getTime();
    }


    public static int getDaysByYear(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        if (year % 4 == 0) {
            return 366;
        } else {
            return 365;
        }
    }

    public static Date str2Date(String str, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String date2Str(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static Date getNow() {
        try {
            Date now = new Date();
            String nowStr =  date2Str(now, "yyyy-MM-dd");
            return str2Date(nowStr, "yyyy-MM-dd");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    public static boolean before(Date d1, Date d2, int patternType) {
        if (patternType == PATTERNTYPE_DATE) {
            Calendar cd1 = new GregorianCalendar();
            cd1.setTime(d1);
            Calendar c1 = new GregorianCalendar();
            c1.set(Calendar.YEAR,cd1.get(Calendar.YEAR));
            c1.set(Calendar.MONTH,cd1.get(Calendar.MONTH));
            c1.set(Calendar.DAY_OF_YEAR,cd1.get(Calendar.DAY_OF_YEAR));
            c1.set(Calendar.HOUR,0);
            c1.set(Calendar.MINUTE,0);
            c1.set(Calendar.SECOND,0);

            Calendar cd2 = new GregorianCalendar();
            cd2.setTime(d2);
            Calendar c2 = new GregorianCalendar();
            c2.set(Calendar.YEAR,cd2.get(Calendar.YEAR));
            c2.set(Calendar.MONTH,cd2.get(Calendar.MONTH));
            c2.set(Calendar.DAY_OF_YEAR,cd2.get(Calendar.DAY_OF_YEAR));
            c2.set(Calendar.HOUR,0);
            c2.set(Calendar.MINUTE,0);
            c2.set(Calendar.SECOND,0);
            return c1.getTime().before(c2.getTime());

        } else {
            return d1.before(d2);
        }
    }

    public static boolean after(Date d1, Date d2, int patternType) {
        if (patternType == PATTERNTYPE_DATE) {
            Calendar cd1 = new GregorianCalendar();
            cd1.setTime(d1);
            Calendar c1 = new GregorianCalendar();
            c1.set(Calendar.YEAR,cd1.get(Calendar.YEAR));
            c1.set(Calendar.MONTH,cd1.get(Calendar.MONTH));
            c1.set(Calendar.DAY_OF_YEAR,cd1.get(Calendar.DAY_OF_YEAR));

            Calendar cd2 = new GregorianCalendar();
            cd2.setTime(d2);
            Calendar c2 = new GregorianCalendar();
            c2.set(Calendar.YEAR,cd2.get(Calendar.YEAR));
            c2.set(Calendar.MONTH,cd2.get(Calendar.MONTH));
            c2.set(Calendar.DAY_OF_YEAR,cd2.get(Calendar.DAY_OF_YEAR));
            return c1.getTime().after(c2.getTime());

        } else {
            return d1.after(d2);
        }
    }

    public static boolean equal(Date d1, Date d2, int patternType) {
        if (patternType == PATTERNTYPE_DATE) {
            Calendar cd1 = new GregorianCalendar();
            cd1.setTime(d1);
            Calendar c1 = new GregorianCalendar();
            c1.set(Calendar.YEAR,cd1.get(Calendar.YEAR));
            c1.set(Calendar.MONTH,cd1.get(Calendar.MONTH));
            c1.set(Calendar.DAY_OF_YEAR,cd1.get(Calendar.DAY_OF_YEAR));

            Calendar cd2 = new GregorianCalendar();
            cd2.setTime(d2);
            Calendar c2 = new GregorianCalendar();
            c2.set(Calendar.YEAR,cd2.get(Calendar.YEAR));
            c2.set(Calendar.MONTH,cd2.get(Calendar.MONTH));
            c2.set(Calendar.DAY_OF_YEAR,cd2.get(Calendar.DAY_OF_YEAR));
            return c1.getTime().equals(c2.getTime());

        } else {
            return d1.equals(d2);
        }
    }

    public static void main(String[] args) {
        Date d1 = new Date();
        Date d2 = new Date();
        d1 = DateUtil.addDate(d1,Calendar.DAY_OF_MONTH,1);
        System.out.println(DateUtil.after(d1,d2,DateUtil.PATTERNTYPE_DATE));
    }



}
