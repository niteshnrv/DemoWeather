package com.example.niteshverma.demoweather.Utility;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Utilities {

    public static void printStackTrace(Exception e) {
        e.printStackTrace();
    }

    public static void printStackTrace(String tag, Exception e) {
        e.printStackTrace();
    }

    public static void printStackTrace(Throwable ex) {
        ex.printStackTrace();
    }

    public static void makeToast(Context context, String msg) {
        try {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            printStackTrace(e);
        }
    }

    public static void makeToast(Context context, Exception e) {
        try {
            Toast.makeText(context, getExceptionString(e), Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            printStackTrace(ex);
        }
    }

    private static String getExceptionString(Exception e) {
        String msg = "-";
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            msg = sw.toString();
        } catch (Exception ex) {
            Utilities.printStackTrace(ex);
        }
        return msg;
    }

    public static void printLog(String tag, String msg) {
        try {
            Log.e(tag, msg);
        } catch (Exception e) {
            printStackTrace(e);
        }
    }

    public static String getFormatedTime(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return simpleDateFormat.format(calendar.getTime());
    }
}
