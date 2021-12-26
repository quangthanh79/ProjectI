package com.pqt.phamquangthanh.projecti.util;

import com.pqt.phamquangthanh.projecti.R;

public class GroupIconUtil {
    public static int getIcon(String groupName){
        switch(groupName){
            case "Người thân cho":
                return R.drawable.nguoithancho;
            case "Làm thêm":
                return R.drawable.lamthem;
            case "Học bổng":
                return R.drawable.hocbong;
            case "Thuê nhà":
                return R.drawable.home;
           case "Ăn uống":
                return R.drawable.anuong;
            case "Xăng xe":
                return R.drawable.xangxe;
            case "Bạn bè":
                return R.drawable.banbe;
            case "Thời trang":
                return R.drawable.thoitrang;
            case "Làm đẹp":
                return R.drawable.lamdep;

            default:
                return R.drawable.khoanthukhac;
        }
    }
}
