package com.gcy.bootwithutils.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

    public static final SimpleDateFormat SHORTDATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static String DATE_FORMAT_DATEONLY = "yyyy-MM-dd";

    public static final long DAY_MILLI = 24 * 60 * 60 * 1000;



    public static String getReqDateyyyyMMdd(Date date) {
        return SHORTDATEFORMAT.format(date);
    }

    public static Date getDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * 获取几天内日期 return 2014-5-4、2014-5-3
     */
    public static List<String> getLastDays(int countDay) {
        List<String> listDate = new ArrayList<String>();
        for (int i = 1; i <= countDay; i++) {
            listDate.add(getReqDateyyyyMMdd(getDate(i)));
        }
        return listDate;
    }

    public static long daysBetween(Date start, Date end) {
        long diff = 0;
        try {
            diff = (end.getTime() - start.getTime()) / DAY_MILLI;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return diff;
    }

    public static List<String> getDayList(String startDate, String endDate) {
        List<String> res = new ArrayList<>();
        try {
            Calendar start = Calendar.getInstance();
            start.setTime(SHORTDATEFORMAT.parse(startDate));
            Calendar end = Calendar.getInstance();
            end.setTime(SHORTDATEFORMAT.parse(endDate));
            end.add(Calendar.DATE, 1);
            while (start.before(end)) {
                res.add(SHORTDATEFORMAT.format(start.getTime()));
                start.add(Calendar.DATE, 1);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return res;
    }


    public static void main(String[] args) throws ParseException {
        String startTime = "2021-08-25";
        String endTime = "2021-09-08";
        Date startDate = DateUtils.parseDate(startTime, DATE_FORMAT_DATEONLY);
        Date endDate = DateUtils.parseDate(endTime, DATE_FORMAT_DATEONLY);
        // System.out.println(getDayList(startDate, endDate));

    }

}
