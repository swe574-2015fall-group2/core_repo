package com.boun.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

    public static Date currentDate() {
        return Calendar.getInstance().getTime();
    }

    public static long currentTime() {
        return currentDate().getTime();
    }

    public static Date todayMidnight() {
        Calendar date = new GregorianCalendar();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTime();
    }

    public static Date tomorrowMidnight() {
        Calendar date = new GregorianCalendar();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        date.add(Calendar.DAY_OF_MONTH, 1);
        return date.getTime();
    }

    public static Date toDate(String dateAsString) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy/MM/dd").parse(dateAsString);
        } catch (ParseException e){}
        return date;
    }

    public static Date toDateTime(String dateAsString) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").parse(dateAsString);
        } catch (ParseException e){}
        return date;
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static long thisYearAsTimestamp(){
        return toDate("2015/01/01").getTime();
    }

    public static void main(String[] args) {
        System.out.println(thisYearAsTimestamp());
    }
}
