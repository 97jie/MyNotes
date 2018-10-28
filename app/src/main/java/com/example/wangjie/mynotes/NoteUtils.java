package com.example.wangjie.mynotes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

//封装工具类方法
public class NoteUtils {

    public static String getTime(){//获取固定格式系统时间字符串
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date=new Date();
        return format.format(date);
    }
    public static Bitmap getImgThumb(String uri, int width, int height){//获取当前图片缩略图
        Bitmap bitmap=null;
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        bitmap=BitmapFactory.decodeFile(uri,options);
        options.inJustDecodeBounds=false;
        int beWidth=options.outWidth/width;
        int beHeight=options.outHeight/height;
        int be=0;
        if(beWidth<beHeight)
            be=beWidth;
        else
            be=beHeight;
        if(be<=0){
            be=1;
        }
        bitmap=BitmapFactory.decodeFile(uri,options);
        bitmap= ThumbnailUtils.extractThumbnail(bitmap,width,height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    public static  Bitmap getVideoThumb(String uri,int width,int height,int kind){//获取视频的缩略图
        Bitmap bitmap=null;
        bitmap=ThumbnailUtils.createVideoThumbnail(uri,kind);
        bitmap=ThumbnailUtils.extractThumbnail(bitmap,width,height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}
