package com.yeshuihan.jigsawpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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
        getData();
        initView();
    }

    public void initView(){
        for(ItemBean itemBean: GameUtil.mItemBeans){
            mBitmapItemLists.add(itemBean.getmBitmap());
        }
        mGridView.setNumColumns(TYPE);
        mGridView.setAdapter(new GridViewApater(this,mBitmapItemLists));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(GameUtil.isMoveable(position)){

                    GameUtil.swapItems(GameUtil.mItemBeans.get(position),GameUtil.mBlankItemBean);
                    newData();
                    ((GridViewApater)mGridView.getAdapter()).notifyDataSetChanged();
                    if (GameUtil.isSuccess()) {
                        // 将最后一张图显示完整
                        newData();
                        mBitmapItemLists.remove(TYPE * TYPE - 1);
                        mBitmapItemLists.add(mLastBitmap);
                        // 通知GridView更改UI
                        ((GridViewApater)mGridView.getAdapter()).notifyDataSetChanged();
                        Toast.makeText(PuzzleMain.this, "拼图成功!",
                                Toast.LENGTH_LONG).show();
                        mGridView.setEnabled(false);

                    }
                }
            }
        });
    }

    public void newData(){
        mBitmapItemLists.clear();
        for(ItemBean itemBean: GameUtil.mItemBeans){
            mBitmapItemLists.add(itemBean.getmBitmap());
        }
    }
    public void getData(){
        Intent intent=getIntent();
        int resSelected=intent.getIntExtra("picSelected",0);
        TYPE = getIntent().getExtras().getInt("mType", 2);

        ImagesUtil imagesUtil=new ImagesUtil();
        imagesUtil.creatInitBitmap(TYPE, BitmapUtils.decodeSampledBitmapFromResource(getResources(),resSelected,900,1600),this);
        GameUtil.getPuzzleGenerator();


    }
}
