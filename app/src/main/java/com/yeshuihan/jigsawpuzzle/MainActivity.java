package com.yeshuihan.jigsawpuzzle;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yeshuihan.jigsawpuzzle.pub.BitmapUtils;
import com.yeshuihan.jigsawpuzzle.pub.ScreenUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private PopupWindow mPopupWindow;
    private View mPopupView;
    private GridView mGvPicLIst;
    private int[] mResPicId;
    private List<Bitmap> mPicList;
    private TextView mTvSelected;
    private LayoutInflater mLayoutInflater;
    private TextView mTvType2,mTvType3,mTvType4;
    private ProgressDialog mProgressDialog;
    private int mType=3;
    private static String TEMP_IMAGE_PATH=Environment.getExternalStorageDirectory().getPath()+"/temp.png";

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                mProgressDialog.dismiss();
                mGvPicLIst.setAdapter(new GridViewMainApater(MainActivity.this,mPicList));
            }


        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        mGvPicLIst=(GridView)findViewById(R.id.gv_xpuzzle_main_pic_list);
        mTvSelected=(TextView)findViewById(R.id.selected);
        mTvSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupShow(v);
            }
        });

        getData();
        initView();

    }
    private void initView(){
        ScaleAnimation sa=new ScaleAnimation(0,1,0,1);
        sa.setDuration(1000);
        LayoutAnimationController lac=new LayoutAnimationController(sa,0.5f);
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        mGvPicLIst.setLayoutAnimation(lac);
        mGvPicLIst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==mPicList.size()-1){
                    showDialogCustom();
                }else {
                    Intent intent=new Intent(MainActivity.this,PuzzleMain.class);
                    intent.putExtra("picSelected",mResPicId[position]);
                    intent.putExtra("mType",mType);
                    startActivity(intent);
                }
            }
        });

        mLayoutInflater = (LayoutInflater) getSystemService(
                LAYOUT_INFLATER_SERVICE);
        // mType view
        mPopupView = mLayoutInflater.inflate(
                R.layout.type_selected, null);
        mTvType2 = (TextView) mPopupView.findViewById(R.id.tv_main_type_2);
        mTvType3 = (TextView) mPopupView.findViewById(R.id.tv_main_type_3);
        mTvType4 = (TextView) mPopupView.findViewById(R.id.tv_main_type_4);
        // 监听事件
        mTvType2.setOnClickListener(this);
        mTvType3.setOnClickListener(this);
        mTvType4.setOnClickListener(this);
        showProgressDialog();
    }

    private void  showProgressDialog(){
        mProgressDialog=new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("图片加载中...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==100&&data!=null){
                Cursor cursor=this.getContentResolver().query(data.getData(),null,null,null,null);
                cursor.moveToFirst();
                String imagepath=cursor.getString(cursor.getColumnIndex("_data"));
                Intent intent=new Intent(MainActivity.this,PuzzleMain.class);
                intent.putExtra("mPicPath",imagepath);
                intent.putExtra("mType",mType);
                cursor.close();
                startActivity(intent);
            }else if(requestCode==200){
                Intent intent=new Intent(MainActivity.this,PuzzleMain.class);
                intent.putExtra("mPicPath",TEMP_IMAGE_PATH);
                intent.putExtra("mType",mType);
                startActivity(intent);
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            // Type
            case R.id.tv_main_type_2:
                mType = 2;
                mTvSelected.setText("当前难度：2 X 2");
                break;
            case R.id.tv_main_type_3:
                mType = 3;
                mTvSelected.setText("当前难度：3 X 3");
                break;
            case R.id.tv_main_type_4:
                mType = 4;
                mTvSelected.setText("当前难度：4 X 4");
                break;
            default:
                break;
        }
        mPopupWindow.dismiss();
    }
    private void getData(){
        mPicList=new ArrayList<>();
        mResPicId=new int[]{
                R.drawable.image1,R.drawable.image2,R.drawable.image3,R.drawable.image4,R.drawable.image5,R.drawable.image6,
                R.drawable.image7,R.drawable.image8,R.drawable.image9,R.drawable.image10,R.drawable.image11,R.drawable.image12,
                R.drawable.image13,R.drawable.image14,R.drawable.image15,R.mipmap.ic_launcher
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<mResPicId.length;i++) {
                    final int a = i;
                    mPicList.add(BitmapUtils.decodeSampledBitmapFromResource(getResources(), mResPicId[a], 600, 800));
                }
                Message msg=new Message();
                msg.what=1;
                mHandler.sendMessage(msg);
            }
        }).start();



    }

    /**
     * 显示popup window
     */
    private void popupShow(View view){
        int density=(int ) ScreenUtil.getDeviceDensity(this);
        Log.i("ysh",mPopupView.getWidth()+"");
        mPopupWindow=new PopupWindow(mPopupView,150*density,150*density);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setOutsideTouchable(true);
        Drawable transpent=new ColorDrawable(Color.TRANSPARENT);
        mPopupWindow.setBackgroundDrawable(transpent);

        mPopupWindow.showAsDropDown(view,0,2);
    }
    public void showDialogCustom(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("请选择图片选择方式");
        builder.setNegativeButton("图库", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(Intent.ACTION_PICK,null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,100);
            }
        });
        builder.setPositiveButton("相机", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoUri=Uri.fromFile(new File(TEMP_IMAGE_PATH));
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(intent,200);

            }
        });
        builder.show();
    }




}
