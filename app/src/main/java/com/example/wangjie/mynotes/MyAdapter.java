package com.example.wangjie.mynotes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<Notes> data;

    public MyAdapter(Context context, List<Notes> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.item, null);
        TextView tv_content = layout.findViewById(R.id.list_content);
        TextView tv_time = layout.findViewById(R.id.list_time);
        ImageView iv_img = layout.findViewById(R.id.list_img);
        ImageView iv_video = layout.findViewById(R.id.list_video);
        Notes note = data.get(i);
        String str=note.getContent();
        if(str.length()>12){
            tv_content.setText(note.getContent().substring(0,12)+"...");
    }else{
        tv_content.setText(str);
    }
        tv_time.setText(note.getTime());
        iv_img.setImageBitmap(NoteUtils.getImgThumb(note.getImg_path(), 200, 200));
        iv_video.setImageBitmap(NoteUtils.getVideoThumb(note.getVideo_path(), 200, 200,
    MediaStore.Images.Thumbnails.MICRO_KIND));
        return layout;
    }
}