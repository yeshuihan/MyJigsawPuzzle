package com.yeshuihan.jigsawpuzzle.pub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.yeshuihan.jigsawpuzzle.ItemBean;
import com.yeshuihan.jigsawpuzzle.PuzzleMain;
import com.yeshuihan.jigsawpuzzle.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fzw on 2016/11/29.
 */
public class ImagesUtil {
    public ItemBean itemBean;


    public void creatInitBitmap(int type, Bitmap picSelectd, Context context){
        Bitmap bitmap=null;
        List<Bitmap> bitmapItems=new ArrayList<Bitmap>();
        int itemWidth=picSelectd.getWidth()/type;
        int itemHeight=picSelectd.getHeight()/type;
        for(int i=1;i<=type;i++){
            for(int j=1;j<=type;j++) {
                bitmap = Bitmap.createBitmap(picSelectd, (j - 1) * itemWidth, (i - 1) * itemHeight, itemWidth, itemHeight);
                bitmapItems.add(bitmap);
                itemBean = new ItemBean((j - 1) * itemWidth, (i - 1) * itemHeight, bitmap);
                GameUtil.mItemBeans.add(itemBean);
            }

        }
        PuzzleMain.mLastBitmap=bitmapItems.get(type*type-1);
        bitmapItems.remove(type*type-1);
        GameUtil.mItemBeans.remove(type*type-1);
        Bitmap blankBitmap= BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        blankBitmap=Bitmap.createBitmap(blankBitmap,0,0,blankBitmap.getWidth(),blankBitmap.getHeight());
        bitmapItems.add(blankBitmap);
        GameUtil.mItemBeans.add(new ItemBean(type*type,0,blankBitmap));
        GameUtil.mBlankItemBean=GameUtil.mItemBeans.get(type*type-1);

    }


    public Bitmap resizeBitmap(float newWidth,float newHeight,Bitmap bitmap){
        Matrix matrix=new Matrix();
        matrix.postScale(newWidth/bitmap.getWidth(),newHeight/bitmap.getHeight());
        Bitmap newBitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return newBitmap;
    }
}
