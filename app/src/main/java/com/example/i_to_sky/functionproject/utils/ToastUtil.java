package com.example.i_to_sky.functionproject.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.i_to_sky.functionproject.application.AppApplication;


/**
 * Created by weiyupei on 2018/12/2.
 */

public class ToastUtil {

    public static void showToastShort(int stringId) {

        Context context = AppApplication.getInstance().getApplicationContext();
        String string = context.getResources().getString(stringId);
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();

    }

    public static void showToastLong(int stringId) {

        Context context = AppApplication.getInstance().getApplicationContext();
        String string = context.getResources().getString(stringId);
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();

    }

    public static void showToast(int stringId, int d_text) {
        Context context = AppApplication.getInstance().getApplicationContext();
        String string = String.format(context.getResources().getString(stringId), d_text);
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

}
