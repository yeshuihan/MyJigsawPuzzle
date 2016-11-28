package com.yeshuihan.jigsawpuzzle.pub;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by fzw on 2016/11/28.
 */
public class ScreenUtil {
    /**
     * 获取屏幕相关参数
     * @param context
     * @return DisplayMetrics
     */
    public static DisplayMetrics getScreenSize(Context context){
        DisplayMetrics metrics=new DisplayMetrics();
        WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display=wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }

    /**
     * 获取屏幕的density
     */
    public static float getDeviceDensity(Context context){
        return getScreenSize(context).density;
    }
}
