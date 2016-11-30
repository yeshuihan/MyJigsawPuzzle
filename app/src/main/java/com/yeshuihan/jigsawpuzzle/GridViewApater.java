package com.yeshuihan.jigsawpuzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


import java.util.List;

public class GridViewApater extends BaseAdapter {
    private Context mContext;
    private List<Bitmap> picList;
    public GridViewApater(Context context,List<Bitmap> list){
        mContext=context;
        picList=list;
    }
    @Override
    public int getCount() {
        return picList.size();
    }

    @Override
    public Object getItem(int position) {
        return picList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View converView, ViewGroup arg2){
        ImageView iv_pic_item=null;

        if(converView==null){
            iv_pic_item=new ImageView(mContext);
            iv_pic_item.setLayoutParams(new GridView.LayoutParams(picList.get(position).getWidth(),picList.get(position).getHeight()));
            iv_pic_item.setScaleType(ImageView.ScaleType.FIT_XY);
        }else{
            iv_pic_item=(ImageView)converView;
        }
        iv_pic_item.setBackgroundColor(Color.BLACK);
        iv_pic_item.setImageBitmap(picList.get(position));
        return iv_pic_item;

    }
}