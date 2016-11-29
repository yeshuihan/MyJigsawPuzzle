package com.yeshuihan.jigsawpuzzle;

import android.graphics.Bitmap;

/**
 * Created by fzw on 2016/11/29.
 */
public class ItemBean {
    private int mItemId;
    private int mBitmapId;



    private Bitmap mBitmap;

    public ItemBean(){};
    public ItemBean(int mItemId,int mBitmapId,Bitmap mBitmap){
        this.mBitmap=mBitmap;
        this.mBitmapId=mBitmapId;
        this.mItemId=mItemId;
    }
    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public int getmBitmapId() {
        return mBitmapId;
    }

    public void setmBitmapId(int mBitmapId) {
        this.mBitmapId = mBitmapId;
    }

    public int getmItemId() {
        return mItemId;
    }

    public void setmItemId(int mItemId) {
        this.mItemId = mItemId;
    }

}
