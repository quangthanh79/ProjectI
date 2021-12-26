package com.pqt.phamquangthanh.projecti.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    public static String getDayOfWeek(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch(calendar.get(Calendar.DAY_OF_WEEK)){
            case Calendar.MONDAY:
                return "Thứ Hai ,";
            case Calendar.TUESDAY:
                return "Thứ Ba ,";
            case Calendar.WEDNESDAY:
                return "Thứ Tư ,";
            case Calendar.THURSDAY:
                return "Thứ Năm ,";
            case Calendar.FRIDAY:
                return "Thứ Sáu ,";
            case Calendar.SATURDAY:
                return "Thứ Bảy ,";
            case Calendar.SUNDAY:
                return "Chủ Nhật ,";
            default:
                return "";

        }
    }
    public static String getDayOfMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return "ngày " + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "") + " " ;
    }
    public static int getDateOfYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public static String getMonthAndYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year  = calendar.get(Calendar.YEAR);
        return "tháng " + month +" năm "+ year;
    }
    public static Date getNextDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR,1);
        return calendar.getTime();
    }
    public static Date getNextWeek(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if(calendar.get(Calendar.DAY_OF_WEEK) == 2){
            calendar.add(Calendar.DAY_OF_YEAR,1);
        }
        while(calendar.get(Calendar.DAY_OF_WEEK) != 2){
            calendar.add(Calendar.DAY_OF_YEAR,1);
        }
        return calendar.getTime();
    }
    public static Date getNextMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if(calendar.get(Calendar.DAY_OF_MONTH) == 1){
            calendar.add(Calendar.DAY_OF_YEAR,1);
        }
        while(calendar.get(Calendar.DAY_OF_MONTH) != 1){
            calendar.add(Calendar.DAY_OF_YEAR,1);
        }
        Date date1 = calendar.getTime();
        return date1;
    }
    public static Date getNextQuarter(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,3);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        return calendar.getTime();
    }
    public static Date getNextYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR,1);
        calendar.set(Calendar.DAY_OF_YEAR,1);
        return calendar.getTime();
    }

    public static Date getPrevDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR,-1);
        return calendar.getTime();
    }
    public static Date getPrevWeek(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if(calendar.get(Calendar.DAY_OF_WEEK) == 2){
            calendar.add(Calendar.DAY_OF_YEAR,-1);
            while(calendar.get(Calendar.DAY_OF_WEEK) != 2){
                calendar.add(Calendar.DAY_OF_YEAR,-1);
            }
        }
        else{
            while(calendar.get(Calendar.DAY_OF_WEEK) != 2){
                calendar.add(Calendar.DAY_OF_YEAR,-1);
            }
            calendar.add(Calendar.DAY_OF_YEAR,-1);
            while(calendar.get(Calendar.DAY_OF_WEEK) != 2){
                calendar.add(Calendar.DAY_OF_YEAR,-1);
            }
        }

        return calendar.getTime();
    }
    public static Date getPrevMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        while(calendar.get(Calendar.DAY_OF_MONTH) != 1){
            calendar.add(Calendar.DAY_OF_YEAR,-1);
        }
        calendar.add(Calendar.DAY_OF_YEAR,-1);
        while(calendar.get(Calendar.DAY_OF_MONTH) != 1){
            calendar.add(Calendar.DAY_OF_YEAR,-1);
        }
        Date date2 = calendar.getTime();
        return date2;
    }
    public static Date getPrevQuarter(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,-3);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        return calendar.getTime();
    }
    public static Date getPrevYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR,-1);
        calendar.set(Calendar.DAY_OF_YEAR,1);
        return calendar.getTime();
    }

    public static Date getFirstDay(Date currentDate, String type){
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        if(type == "week"){
            calendar.set(Calendar.DAY_OF_WEEK,2);
            return calendar.getTime();
        }else if(type == "month"){
            calendar.set(Calendar.DAY_OF_MONTH,1);
            return calendar.getTime();
        }else if(type == "quarter"){
            int quarter = (calendar.get(Calendar.MONTH))/3 +1;
            int monthFirst = 3*quarter -2;
            calendar.set(Calendar.MONTH,monthFirst-1);
            calendar.set(Calendar.DAY_OF_MONTH,1);
            return calendar.getTime();
        }else if(type == "year"){
            calendar.set(Calendar.DAY_OF_YEAR,1);
            return calendar.getTime();
        }else{
            return calendar.getTime();
        }
    }
    public static Date getLastDay(Date currentDate, String type){
        Calendar calendar1 =    Calendar.getInstance();
        Calendar calendar  =    Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        if(type == "week"){
            calendar.set(Calendar.DAY_OF_WEEK,7);
            calendar.add(Calendar.DAY_OF_WEEK,1);
            return calendar.getTime();
        }else if(type == "month"){
            calendar.set(Calendar.DAY_OF_MONTH,28);
            while(calendar.get(Calendar.DAY_OF_MONTH) !=1){
                calendar.add(Calendar.DAY_OF_YEAR,1);
            }
            calendar.add(Calendar.DAY_OF_YEAR,-1);
            return calendar.getTime();
        }else if(type == "quarter"){
            int quarter = (calendar.get(Calendar.MONTH))/3 +1;
            int monthFirst = 3*quarter;
            calendar.set(Calendar.MONTH,monthFirst-1);
            calendar.set(Calendar.DAY_OF_MONTH,28);
            while(calendar.get(Calendar.DAY_OF_MONTH) !=1){
                calendar.add(Calendar.DAY_OF_YEAR,1);
            }
            calendar.add(Calendar.DAY_OF_YEAR,-1);
            return calendar.getTime();
        }else if(type == "year"){
            calendar.set(Calendar.MONTH,11);
            calendar.set(Calendar.DAY_OF_MONTH,31);
            return calendar.getTime();
        }else{
            return calendar.getTime();
        }
    }
    public static long getDayTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }
    public static long getStartDayTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendar1 = new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        return calendar1.getTimeInMillis();
    }
    public static long getEndDayTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR,1);
        Calendar calendar1 = new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        return calendar1.getTimeInMillis();
    }
    public static long getStartDayOfMonth(int month,int year){
        Calendar calendar = Calendar.getInstance();
        int month_ex = month-1;
        calendar.set(Calendar.MONTH,month_ex);
        calendar.set(Calendar.YEAR,year);
        while(calendar.get(Calendar.DAY_OF_MONTH)!=1){
            calendar.add(Calendar.DAY_OF_MONTH,-1);
        }
        Calendar calendar1 = new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        return calendar1.getTimeInMillis();
    }
    public static long getEndDayOfMonth(int month,int year){
        Calendar calendar = Calendar.getInstance();
        int month_ex = month-1;
        calendar.set(Calendar.MONTH,month_ex);
        calendar.set(Calendar.YEAR,year);
        if(calendar.get(Calendar.DAY_OF_MONTH)==1){
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }
        while(calendar.get(Calendar.DAY_OF_MONTH) != 1){
            calendar.add(Calendar.DAY_OF_YEAR,1);
        }
        calendar.add(Calendar.DAY_OF_YEAR,-1);
        Calendar calendar1 = new GregorianCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

        return calendar1.getTimeInMillis();
    }


    public static String formatDate(Date date){
        return getDayOfWeek(date) + getDayOfMonth(date) + getMonthAndYear(date);
    }
    public static String formatDateBaseOnDate(Date date){
        Calendar calendar_now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        boolean check2 = (calendar_now.get(Calendar.YEAR) != calendar.get(Calendar.YEAR));
        int check1 = calendar_now.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);

        if (Calendar.getInstance().getTimeInMillis() < date.getTime()) {
            return "";
        } else if(check1==0 && !check2){
            return "HÔM NAY";
        } else if(check1 == 1 && !check2){
            return "HÔM TRƯỚC";
        } else{
            int date1  = calendar.get(Calendar.DATE);
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);
            return date1 +"/"+ month +"/"+year;
        }
    }
    public static String formatDateBaseOnCustom(Date start,Date end){
        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd   = Calendar.getInstance();
        calendarStart.setTime(start);
        calendarEnd.setTime(end);
        int day01 = calendarStart.get(Calendar.DAY_OF_MONTH);
        int day02 = calendarEnd.get(Calendar.DAY_OF_MONTH);
        String dayStart = day01 < 10 ? "0" + day01 : day01 + "";
        String dayEnd   = day02 < 10 ? "0" + day02 : day02 + "";
        String from = dayStart + "/" + (calendarStart.get(Calendar.MONTH)+1)+"/"+ calendarStart.get(Calendar.YEAR);
        String to   = dayEnd   + "/" + (calendarEnd.get(Calendar.MONTH)+1)+"/"+ calendarEnd.get(Calendar.YEAR);
        return from+" - "+to;
    }
    public static String formatDateBaseOnWeek(Date date){
        if (Calendar.getInstance().getTimeInMillis() < date.getTime()) {
            return "";
        } else {

            Calendar calendar_now = Calendar.getInstance();
            Calendar calendar_1    = Calendar.getInstance();
            Calendar calendar_2    = Calendar.getInstance();

            calendar_1.setTime(date);
            calendar_2.setTime(date);
            int check1= calendar_now.get(Calendar.DAY_OF_YEAR)- calendar_1.get(Calendar.DAY_OF_YEAR);
            boolean check2 = (calendar_now.get(Calendar.YEAR) != calendar_1.get(Calendar.YEAR));
            if(check1 < 7 && !check2)
                return "TUẦN NÀY";
            else if(7< check1 && check1 < 14 && !check2){
                return "TUẦN TRƯỚC";
            }
            else {
                int dayOfWeek = calendar_1.get(Calendar.DAY_OF_WEEK);// 1-7
                calendar_1.set(Calendar.DAY_OF_WEEK,2);
                calendar_2.set(Calendar.DAY_OF_WEEK,7);
                calendar_2.add(Calendar.DAY_OF_YEAR,1);
                int date1 = calendar_1.get(Calendar.DATE);
                int date2 = calendar_2.get(Calendar.DATE);
                int month1 = calendar_1.get(Calendar.MONTH) + 1;
                int month2 = calendar_2.get(Calendar.MONTH) + 1;

                int year1 = calendar_1.get(Calendar.YEAR);
                int year2 = calendar_2.get(Calendar.YEAR);

                return date1 + "/" + month1 + "/" + year1+" - "+date2 + "/" + month2 + "/" + year2;
            }
        }
    }
    public static String formatDateBaseOnMonth(Date date) {
        Calendar calendar_now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        boolean check2 = (calendar_now.get(Calendar.YEAR) != calendar.get(Calendar.YEAR));
        int check1 = calendar_now.get(Calendar.MONTH) - calendar.get(Calendar.MONTH);

        if (calendar_now.getTimeInMillis() < date.getTime()) {
            return "";
        } else if( check1 == 0 && !check2 ){
            return "THÁNG NÀY";
        }else if(check1 == 1 && !check2){
            return "THÁNG TRƯỚC";
        }else {
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);
            return "T" + month + "/" + year;

        }
    }
    public static String formatDateBaseOnQuarter(Date date){
        Calendar calendar_now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        boolean check2 = (calendar_now.get(Calendar.YEAR) != calendar.get(Calendar.YEAR));
        int check1 = calendar_now.get(Calendar.MONTH) - calendar.get(Calendar.MONTH);
        if (calendar_now.getTimeInMillis() < date.getTime()) {
            return "";
        } else if(check1 == 0 && !check2){
            return "QUÝ NÀY";
        } else if(check1 == 3 && !check2){
            return "QUÝ TRƯỚC";
        }
        else{
            int quarter = (calendar.get(Calendar.MONTH))/3 +1;
            int year = calendar.get(Calendar.YEAR);
            return "Q"+quarter+"/"+year;
        }


    }
    public static String formatDateBaseOnYear(Date date){
        Calendar calendar_now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int check1 = (calendar_now.get(Calendar.YEAR) - calendar.get(Calendar.YEAR));
        if (calendar_now.getTimeInMillis() < date.getTime()) {
            return "";
        }else if( check1 ==0 ){
            return "NĂM NAY";
        } else if(check1 ==1 ){
            return "NĂM TRƯỚC";
        }else{
            return calendar.get(Calendar.YEAR)+"";
        }
    }
}
