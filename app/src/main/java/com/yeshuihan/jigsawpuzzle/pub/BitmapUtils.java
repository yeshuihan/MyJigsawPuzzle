package com.yeshuihan.jigsawpuzzle.pub;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by fzw on 2016/11/29.
 */
public class BitmapUtils {
    public static int computeSampleSize(BitmapFactory.Options opts,int width,int height){
        int inSampleSize=computeInSampleSize(opts,width,height);
        int roundedSize=1;
        while(roundedSize<inSampleSize){
            roundedSize<<=1;
        }
        return roundedSize;
    }

    public static int computeInSampleSize(BitmapFactory.Options opts,int reqW,int reqH){
        int width=opts.outWidth;
        int height=opts.outHeight;
        return Math.min(width/reqW,height/reqH);
    }
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth,
                                            int dstHeight) {
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, true);
        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res,int resId,int reqW,int reqH){
        BitmapFactory.Options opts=new BitmapFactory.Options();
        opts.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(res,resId,opts);
        opts.inSampleSize=computeSampleSize(opts,reqW,reqH);
        opts.inJustDecodeBounds=false;
        return createScaleBitmap(BitmapFactory.decodeResource(res,resId,opts),reqW,reqH);
    }

    public static Bitmap decodeSampledBitmapFromFile(String str,int reqW,int reqH){
        BitmapFactory.Options opts=new BitmapFactory.Options();
        opts.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(str,opts);
        opts.inSampleSize=computeSampleSize(opts,reqW,reqH);
        opts.inJustDecodeBounds=false;
        return createScaleBitmap(BitmapFactory.decodeFile(str,opts),reqW,reqH);
    }
}
