package com.pqt.phamquangthanh.projecti.util;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;

public class ConversionUtil {
    public static Date timestampToDate(long timestamp){
        Timestamp stamp = new Timestamp(timestamp);
        return new Date(stamp.getTime());
    }
    public static String longToString(long num) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        if(num >= 0) {
            return "+"+decimalFormat.format(num) + "đ";
        }else{
            return decimalFormat.format(num) + "đ";
        }
    }
}
