package com.yelanlan.scoremanagersystem.Utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String transCurrDateToNum(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return dateFormat.format(new Date(System.currentTimeMillis()));
    }
    public static Timestamp getCurrentTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  Timestamp.valueOf(dateFormat.format(new Date()));
    }

    public static  void main(String[] args){
        System.out.println(getCurrentTime());
    }
}
