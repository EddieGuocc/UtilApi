package com.gcy.bootwithutils.service.date;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
@Slf4j
public class DateService extends org.apache.commons.lang3.time.DateUtils{

    /*
     * @Author gcy
     * @Description Return timestamp of now time
     * @Date 16:31 2020/5/13
     * @Param []
     * @return int
     **/
    public static int currentTimeSecond(){
        return (int) (System.currentTimeMillis()/1000);
    }

    /*
     * @Author gcy
     * @Description Return formatted current time
     * @Date 16:40 2020/5/13
     * @Param []
     * @return java.lang.String
     **/
    public static String currentFormattedMilliseconds(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    /*
     * @Author gcy
     * @Description Return timestamp of now time
     * @Date 16:52 2020/5/13
     * @Param [date]
     * @return java.lang.Integer
     **/
    public static Integer getTimeSecond(Date date) {
        if (null == date)
            return null;
        return (int) (date.getTime() / 1000);
    }

    /*
     * @Author gcy
     * @Description Return timestamp of given time
     * @Param [time] e.g. 2020-05-13 16:53:13
     * @return java.lang.Integer
     **/
    public static Integer timeStrToUnix(String time) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = simpleDateFormat.parse(time);
        int ts = (int) (date.getTime() / 1000);
        return ts;
    }

    /*
     * @Author gcy
     * @Description return default date of given time
     * @Date 17:20 2020/5/13
     * @Param [unixTimeStamp]
     * @return java.util.Date
     **/
    public static Date getDate(Integer unixTimeStamp) {
        if (null == unixTimeStamp || 0 == unixTimeStamp)
            return null;

        long l = unixTimeStamp.longValue() * 1000;

        return new Date(l);
    }

    /*
     * @Author gcy
     * @Description return the first day for next month of given date
     * @Date 9:44 2020/5/14
     * @Param [date]
     * @return java.util.Date
     **/
    public static Date getNextMonthFirstDay(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /*
     * @Author gcy
     * @Description return the first day of given date
     * @Date 9:51 2020/5/14
     * @Param [date]
     * @return java.util.Date
     **/
    public static Date getMonthFirstDay(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /*
     * @Author gcy
     * @Description return the last day of given date
     * @Date 10:04 2020/5/14
     * @Param [date]
     * @return java.util.Date
     **/
    public static Date getMonthLastDay(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /*
     * @Author gcy
     * @Description return timestamp for the first day of given date
     * @Date 10:17 2020/5/14
     * @Param [date]
     * @return int
     **/
    public static int getMonthFirstDayTimeSecond(Date date) {
        return getTimeSecond(getMonthFirstDay(date));
    }

    /*
     * @Author gcy
     * @Description return date difference between two given dates
     * @Date 10:32 2020/5/14
     * @Param [from, to]
     * @return java.lang.Integer
     **/
    public static Integer getDays(Date from, Date to) {

        if (null == from || to == null)
            return null;
        if (to.compareTo(from) < 0) {
            return 0;
        }
        return (int) ((to.getTime() - from.getTime()) / (1000 * 3600 * 24));
    }

    /*
     * @Author gcy
     * @Description return date difference between now time and given date
     * @Date 10:38 2020/5/14
     * @Param [to]
     * @return java.lang.Integer
     **/
    public static Integer getDaysFromNow(Date to) {
        return getDays(new Date(), to);
    }

    /*
     * @Author gcy
     * @Description convert given time stamp to formatted date
     * @Date 10:41 2020/5/14
     * @Param [s]
     * @return java.lang.String
     **/
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(Long.valueOf(s));
        res = simpleDateFormat.format(date);
        return res;
    }


}
