package com.example.i_to_sky.functionproject.utils;


import com.example.i_to_sky.functionproject.application.AppApplication;

/**
 * Created by weiyupei on 2018/11/30.
 */

public class DensityUtil {

    /**
     * dp 转 px
     */
    public static int dp2px(final float dpValue) {
        float scale = AppApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px 转 dp
     */
    public static int px2dp(final float pxValue) {
        float scale = AppApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp 转 px
     */
    public static int sp2px(final float spValue) {
        float fontScale = AppApplication.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px 转 sp
     */
    public static int px2sp(final float pxValue) {
        final float fontScale = AppApplication.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

}
