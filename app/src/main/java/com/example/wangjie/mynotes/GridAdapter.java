package com.example.wangjie.mynotes;

import android.content.Context;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class GridAdapter extends BaseAdapter{

    private Context context;
    private List<Notes> data;
    public GridAdapter(Context context, List<Notes> data) {
        this.context=context;
        this.data=data;
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
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout layout= (LinearLayout) inflater.inflate(R.layout.griditem,null);
        ImageView grid_iv_img=layout.findViewById(R.id.grid_iv_img);
        ImageView grid_iv_video=layout.findViewById(R.id.grid_iv_video);
        TextView grid_tv_text=layout.findViewById(R.id.grid_tv_text);
        TextView grid_tv_time=layout.findViewById(R.id.grid_tv_time);
        Notes note=data.get(i);

        grid_iv_img.setImageBitmap(NoteUtils.getImgThumb(note.getImg_path(), 200, 200));
        grid_iv_video.setImageBitmap(NoteUtils.getVideoThumb(note.getVideo_path(), 200, 200,
                MediaStore.Images.Thumbnails.MICRO_KIND));
        String str= note.getContent();
        if(str.length()>7){
            grid_tv_text.setText(str.substring(0,7)+"...");
        }else{
            grid_tv_text.setText(str);
        }
        grid_tv_time.setText(note.getTime());

        return layout;
    }
}
