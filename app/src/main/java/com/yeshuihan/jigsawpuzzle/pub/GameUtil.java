package com.yeshuihan.jigsawpuzzle.pub;

import com.yeshuihan.jigsawpuzzle.ItemBean;
import com.yeshuihan.jigsawpuzzle.PuzzleMain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fzw on 2016/11/29.
 */
public class GameUtil {
    public static List<ItemBean> mItemBeans=new ArrayList<ItemBean>();
    public static ItemBean mBlankItemBean=new ItemBean();


    public static void getPuzzleGenerator(){
        int index=0;
        for(int i=0;i<mItemBeans.size();i++){
            index=(int)(Math.random()* PuzzleMain.TYPE* PuzzleMain.TYPE);
            swapItems(mItemBeans.get(index),GameUtil.mBlankItemBean);

        }
        List<Integer> data=new ArrayList<Integer>();
        for(int i=0;i<mItemBeans.size();i++){
            data.add(mItemBeans.get(i).getmBitmapId());

        }
        if(canSolve(data)){
            return;
        }else{
            getPuzzleGenerator();
        }
    }

    public static boolean canSolve(List<Integer> data) {
        int blankId=GameUtil.mBlankItemBean.getmItemId();
        if(data.size()%2==1){
            return getInversions(data)%2==0;
        }else{
            if(((blankId-1)/PuzzleMain.TYPE)%2==1){
                return getInversions(data)%2==0;
            }else{
                return getInversions(data)%2==1;
            }
        }
    }

    public static int getInversions(List<Integer> data) {
        int inversions=0;
        int inversionCount=0;
        for(int i=0;i<data.size();i++){
            for(int j=i+1;j<data.size();j++){
                int index=data.get(i);
                if(data.get(j)!=0&&data.get(j)<index){
                    inversionCount++;
                }
            }
            inversions+=inversionCount;
            inversionCount=0;

        }
        return inversions;
    }

    public static void swapItems(ItemBean from, ItemBean blank) {
        ItemBean tempItemBean=new ItemBean();
        tempItemBean.setmBitmapId(from.getmBitmapId());
        from.setmBitmapId(blank.getmBitmapId());
        blank.setmBitmapId(tempItemBean.getmBitmapId());
        tempItemBean.setmBitmap(from.getmBitmap());
        from.setmBitmap(blank.getmBitmap());
        blank.setmBitmap(tempItemBean.getmBitmap());
        GameUtil.mBlankItemBean=from;
    }

    public static boolean isMoveable(int position){
        int type=PuzzleMain.TYPE;
        int blankID=GameUtil.mBlankItemBean.getmItemId()-1;
        if(Math.abs(blankID-position)==type){
            return true;
        }
        if((blankID/type==position/type)&&Math.abs(blankID-position)==1){
            return true;
        }
        return false;
    }
    public static boolean isSuccess(){
        for(ItemBean tempItemBean:GameUtil.mItemBeans ) {
            if(tempItemBean.getmBitmapId()!=0&&(tempItemBean.getmItemId())==tempItemBean.getmBitmapId()){
                continue;
            }else if(tempItemBean.getmBitmapId()==0&&tempItemBean.getmItemId()==PuzzleMain.TYPE*PuzzleMain.TYPE){
                continue;
            }else {
                return false;
            }

        }
        return true;
    }
}
