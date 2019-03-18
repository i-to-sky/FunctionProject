package com.example.i_to_sky.functionproject.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.example.i_to_sky.functionproject.application.AppApplication;

/**
 * Created by weiyupei on 2018/11/30.
 */

public class ScreenUtil {

    public static int getScreenWidth() {
        Resources resources = AppApplication.getInstance().getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight() {
        Resources resources = AppApplication.getInstance().getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

}
