package com.example.i_to_sky.functionproject.utils;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by weiyupei on 2018/12/21.
 */

public class TimeUtil {

    private static final ThreadLocal<SimpleDateFormat> SDF_THREAD_LOCAL = new ThreadLocal<>();

    private TimeUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static SimpleDateFormat getDefaultFormat() {
        SimpleDateFormat simpleDateFormat = SDF_THREAD_LOCAL.get();
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SDF_THREAD_LOCAL.set(simpleDateFormat);
        }
        return simpleDateFormat;
    }

    public static String millis2String(final long millis) {
        return millis2String(millis, getDefaultFormat());
    }

    public static String millis2String(final long millis, @NonNull final DateFormat format) {
        return format.format(new Date(millis));
    }

    public static long string2Millis(final String time) {
        return string2Millis(time, getDefaultFormat());
    }

    public static long string2Millis(final String time, @NonNull final DateFormat format) {
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Date string2Date(final String time) {
        return string2Date(time, getDefaultFormat());
    }

    public static Date string2Date(final String time, @NonNull final DateFormat format) {
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String date2String(final Date date) {
        return date2String(date, getDefaultFormat());
    }

    public static String date2String(final Date date, @NonNull final DateFormat format) {
        return format.format(date);
    }

    public static long date2Millis(final Date date) {
        return date.getTime();
    }

    public static Date millis2Date(final long millis) {
        return new Date(millis);
    }

}
