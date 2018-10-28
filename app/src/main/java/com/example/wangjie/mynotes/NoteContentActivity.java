package com.example.wangjie.mynotes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;

public class NoteContentActivity extends AppCompatActivity {

    private TextView tv_note;
    private TextView tv_time;
    private ImageView iv_note;
    private VideoView vv_note;
    private Toolbar tb_content;
    private Notes note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_content);
        tv_note=findViewById(R.id.tv_note);
        tv_time=findViewById(R.id.tv_time);
        iv_note=findViewById(R.id.iv_note);
        vv_note=findViewById(R.id.vv_note);
        note= (Notes) getIntent().getSerializableExtra("data");
        tv_time.setText(note.getTime());
        tv_note.setText(note.getContent());
        String img_path=note.getImg_path();
        String video_path=note.getVideo_path();
        if("null".equals(img_path)){
            iv_note.setVisibility(View.GONE);
        }else{
            Log.i("图片路径",img_path);
            iv_note.setVisibility(View.VISIBLE);
            iv_note.setImageBitmap(BitmapFactory.decodeFile(img_path));
        }
        if("null".equals(video_path)){
            vv_note.setVisibility(View.GONE);
        }else{
            Log.i("视频路径",video_path);
            vv_note.setVisibility(View.VISIBLE);
            MediaController controller=new MediaController(this);
            vv_note.setVideoURI(FileProvider.getUriForFile(this,
                    "com.example.wangjie.mynotes.fileprovider",new File(video_path)));
            vv_note.setMediaController(controller);
            vv_note.start();
        }
        tb_content= findViewById(R.id.tb_content);
        setSupportActionBar(tb_content);
        tb_content.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_edit){
            Intent intent=new Intent(NoteContentActivity.this,AddContentActivity.class);
            if(!("null".equals(note.getImg_path()))){
                intent.putExtra("flag","2");
            }else if(!("null".equals(note.getVideo_path()))){
                intent.putExtra("flag","3");
            }else{
                intent.putExtra("flag","1");
            }
            intent.putExtra("last","last");
            intent.putExtra("data",note);
            finish();
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
