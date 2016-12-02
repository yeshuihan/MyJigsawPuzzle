package com.yeshuihan.jigsawpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
    private int mImageId;
    private String mImagePath;

    private PopupWindow mPopup;
    private View mPopupView;
    private ImageView mImageView;

    private Button mShowImage;
    private Button mReset;
    private Button mBack;

    private Bitmap mBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzlemain);

        findView();
        getData();
        initView();
    }

    private void findView(){
        mGridView= (GridView) findViewById(R.id.detail);
        mButtonTime=(Button)findViewById(R.id.jishi);
        mButtonNum=(Button)findViewById(R.id.jishu);
        mCount=(TextView)findViewById(R.id.counts);
        mPopupView=((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.showpopup,null);
        mImageView=(ImageView) mPopupView.findViewById(R.id.image);

        mShowImage= (Button) findViewById(R.id.image);
        mBack=(Button)findViewById(R.id.back);
        mReset=(Button)findViewById(R.id.restart);

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
        newData();
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


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mShowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView.setImageBitmap(mBitmap);
                showPopupWindow(v);
            }
        });
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });


    }
    public void showPopupWindow(View v){
        if(mPopup==null||!mPopup.isShowing()){
            int d=(int)ScreenUtil.getDeviceDensity(this);
            if(mPopup==null){
                mPopup=new PopupWindow(mPopupView,306*d,462*d);

                mPopup.setOutsideTouchable(true);
                mPopup.setBackgroundDrawable(new BitmapDrawable());
                mPopup.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        mPopupView.startAnimation(getCloseAnimation());

                        return true;
                    }
                });
            }
            mPopupView.startAnimation(getOpenAnimation());
            mPopup.showAtLocation(v, Gravity.CENTER,1*d,10*d);
        }
    }



    public void reset(){
        ImagesUtil imagesUtil=new ImagesUtil();
        imagesUtil.creatInitBitmap(TYPE,mBitmap ,this);
        GameUtil.getPuzzleGenerator();
        newData();
        mGridView.setAdapter(new GridViewApater(this,mBitmapItemLists));
    }
    public void newData(){
        mBitmapItemLists.clear();
        for(ItemBean itemBean: GameUtil.mItemBeans){
            mBitmapItemLists.add(itemBean.getmBitmap());
        }
    }
    private void getIntentData(){
        Intent intent=getIntent();
        mImageId=intent.getIntExtra("picSelected",0);
        TYPE = getIntent().getExtras().getInt("mType", 2);
        if(mImageId==0){
            mImagePath=intent.getStringExtra("mPicPath");
        }
    }

    public void getData(){
        getIntentData();
        float density= ScreenUtil.getDeviceDensity(this);
        ImagesUtil imagesUtil=new ImagesUtil();
        if(mImageId==0){
            mBitmap=BitmapUtils.decodeSampledBitmapFromFile(mImagePath,(int)((300-(TYPE-1)*2)*density),(int)((454-(TYPE-1)*2)*density));

        }else{
            mBitmap=BitmapUtils.decodeSampledBitmapFromResource(getResources(),mImageId,(int)((300-(TYPE-1)*2)*density),(int)((454-(TYPE-1)*2)*density));
        }

        imagesUtil.creatInitBitmap(TYPE,mBitmap ,this);
        GameUtil.getPuzzleGenerator();
    }

    private ScaleAnimation getOpenAnimation(){
        ScaleAnimation sca=new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,1);
        sca.setDuration(1000);
        return sca;
    }
    private ScaleAnimation getCloseAnimation(){
        ScaleAnimation sca=new ScaleAnimation(1,0,1,0);
        sca.setDuration(1000);
        sca.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mPopup.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return sca;
    }
    class MyPopupWindow extends PopupWindow{
        private View mView;
        public MyPopupWindow(){
            super();
        }
        public MyPopupWindow(View v,int width,int height){
            super(v,width,height);
            mView=v;
        }

        @Override
        public void dismiss() {
            Log.i("fzw","sd");
            mView.startAnimation(getCloseAnimation());
            super.dismiss();
        }

        @Override
        public void showAtLocation(View parent, int gravity, int x, int y) {
            mView.startAnimation(getOpenAnimation());
            super.showAtLocation(parent, gravity, x, y);
        }

    }
}
