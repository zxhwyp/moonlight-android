package com.limelight.Infrastructure.common;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.limelight.HXSLog;

public class HXSCommon {
    public static boolean isStringEmpty(String str){
        return (str == null || str.length() <= 0);
    }
    public static void recycleImageView(View view){
        if(view==null) return;
        if(view instanceof ImageView){
            Drawable drawable=((ImageView) view).getDrawable();
            if(drawable instanceof BitmapDrawable){
                Bitmap bmp = ((BitmapDrawable)drawable).getBitmap();
                if (bmp != null && !bmp.isRecycled()){
                    ((ImageView) view).setImageBitmap(null);
//                    bmp.recycle();
                    HXSLog.info("have recycled ImageView Bitmap");
                    bmp=null;
                }
            }
        }
    }
}
