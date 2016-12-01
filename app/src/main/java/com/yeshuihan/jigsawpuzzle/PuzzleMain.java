package com.yeshuihan.jigsawpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.yeshuihan.jigsawpuzzle.pub.BitmapUtils;
import com.yeshuihan.jigsawpuzzle.pub.GameUtil;
import com.yeshuihan.jigsawpuzzle.pub.ImagesUtil;
import com.yeshuihan.jigsawpuzzle.pub.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fzw on 2016/11/29.
 */
public class PuzzleMain extends Activity {
    public static Bitmap mLastBitmap;
    public static int TYPE;
    private GridView mGridView;
    private Button mButtonTime;
    private boolean mIsOnclickTime=false;
    private Button mButtonNum;
    private boolean mIsOnclickNum=false;
    private Chronometer mTimer;
    private long mRecordTime=0;
    private TextView mCount;
    private int mNum=0;
    private List<Bitmap> mBitmapItemLists = new ArrayList<Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzlemain);
        mGridView= (GridView) findViewById(R.id.detail);
        mButtonTime=(Button)findViewById(R.id.jishi);
        mButtonNum=(Button)findViewById(R.id.jishu);
        mCount=(TextView)findViewById(R.id.counts);
        getData();
        initView();
    }

    public void initView(){
        mTimer=(Chronometer)findViewById(R.id.time);
        mButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIsOnclickTime){
                    mTimer.stop();
                    mRecordTime = SystemClock.elapsedRealtime();
                    mButtonTime.setText("继续计时");
                    mButtonTime.setSelected(false);
                    mIsOnclickTime=!mIsOnclickTime;
                }else{

                    if(mRecordTime != 0){
                        mTimer.setBase(mTimer.getBase() + (SystemClock.elapsedRealtime() - mRecordTime));
                    }else{
                        mTimer.setBase(SystemClock.elapsedRealtime());
                    }
                    mTimer.start();
                    mButtonTime.setSelected(true);
                    mButtonTime.setText("暂停计时");
                    mIsOnclickTime=!mIsOnclickTime;
                }
            }
        });
        mButtonNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIsOnclickNum){
                    mButtonNum.setSelected(false);
                    mButtonNum.setText("开始计步");
                    mIsOnclickNum=!mIsOnclickNum;
                }else {
                    mCount.setText("0");
                    mButtonNum.setSelected(true);
                    mButtonNum.setText("停止计步");
                    mIsOnclickNum=!mIsOnclickNum;
                }


            }
        });
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
                    if(mIsOnclickNum){
                        mNum++;
                        mCount.setText(mNum+"");
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
        float density= ScreenUtil.getDeviceDensity(this);
        ImagesUtil imagesUtil=new ImagesUtil();
        imagesUtil.creatInitBitmap(TYPE, BitmapUtils.decodeSampledBitmapFromResource(getResources(),resSelected,(int)((300-(TYPE-1)*2)*density),(int)((454-(TYPE-1)*2)*density)),this);
        GameUtil.getPuzzleGenerator();


    }
}
