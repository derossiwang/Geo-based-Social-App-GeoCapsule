package com.example.group_w01_07_3.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * (Modified) From: https://github.com/jianjunxiao/BottomDialog
 * Created by XiaoJianjun on 2016/9/1
 * Pixel conversion util class
 */
public class DensityUtil {

    /**
     * dp -> px
     *
     * @param context context
     * @param dpVal   dp value
     * @return px value
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                context.getResources().getDisplayMetrics());
    }

    /**
     * sp -> px
     *
     * @param context context
     * @param spVal   sp value
     * @return px value
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal,
                context.getResources().getDisplayMetrics());
    }

    /**
     * px -> dp
     *
     * @param context context
     * @param pxVal   px value
     * @return dp value
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px -> sp
     *
     * @param context context
     * @param pxVal   px value
     * @return sp value
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

}