package com.yeshuihan.jigsawpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.yeshuihan.jigsawpuzzle.pub.BitmapUtils;
import com.yeshuihan.jigsawpuzzle.pub.GameUtil;
import com.yeshuihan.jigsawpuzzle.pub.ImagesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fzw on 2016/11/29.
 */
public class PuzzleMain extends Activity {
    public static Bitmap mLastBitmap;
    public static int TYPE;
    private GridView mGridView;
    private List<Bitmap> mBitmapItemLists = new ArrayList<Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzlemain);
        mGridView= (GridView) findViewById(R.id.detail);
        initView();
    }

    public void initView(){
        getData();
        mGridView.setNumColumns(TYPE);
        mGridView.setAdapter(new GridViewApater(this,mBitmapItemLists));
    }
    public void getData(){
        Intent intent=getIntent();
        int resSelected=intent.getIntExtra("picSelected",0);
        TYPE = getIntent().getExtras().getInt("mType", 2);

        ImagesUtil imagesUtil=new ImagesUtil();
        imagesUtil.creatInitBitmap(TYPE, BitmapUtils.decodeSampledBitmapFromResource(getResources(),resSelected,1200,1600),this);
        for(ItemBean itemBean: GameUtil.mItemBeans){
            mBitmapItemLists.add(itemBean.getmBitmap());
        }

    }
}
