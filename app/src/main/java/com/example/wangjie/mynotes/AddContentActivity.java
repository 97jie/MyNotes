package com.example.wangjie.mynotes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;

public class AddContentActivity extends AppCompatActivity{
    private String val;
    private EditText add_text;
    private VideoView add_video;
    private ImageView add_img;
    private Notes note;
    private NotesManager notesManager;
    private File img_file,video_file;
    private Toolbar tb_add;
    private String isLast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);
        Intent intent=getIntent();
        val=intent.getStringExtra("flag");
        note= (Notes) intent.getSerializableExtra("data");
        isLast=intent.getStringExtra("last");
        add_text=findViewById( R.id.add_text);
        add_video=findViewById(R.id.add_video);
        add_img=findViewById(R.id.add_img);
        tb_add= findViewById(R.id.tb_add);
        setSupportActionBar(tb_add);
        tb_add.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        notesManager=new NotesManager(this);
        intiView();
    }

    public  void intiView(){
        if(val.equals("1")){
            add_img.setVisibility(View.GONE);
            add_video.setVisibility(View.GONE);
            if(note!=null){
                add_text.setText(note.getContent());
            }
        }else if(val.equals("2")){
            add_img.setVisibility(View.VISIBLE);
            add_video.setVisibility(View.GONE);
            if(note!=null){
                add_text.setText(note.getContent());
                add_img.setImageBitmap(BitmapFactory.decodeFile(note.getImg_path()));
            }else{
                Intent img_intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//启动相机
                img_file=new File(getExternalCacheDir(),NoteUtils.getTime()+".jpg");
//            img_file=new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/"+
//                    NoteUtils.getTime()+".jpg");
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N);{
                    img_intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            FileProvider.getUriForFile(this,
                                    "com.example.wangjie.mynotes.fileprovider",img_file));
                }
                if(Build.VERSION.SDK_INT<Build.VERSION_CODES.N){
                    img_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(img_file));
                }
                startActivityForResult(img_intent,1);
            }
        }else{
            add_img.setVisibility(View.GONE);
            add_video.setVisibility(View.VISIBLE);
            if(note!=null){
                add_text.setText(note.getContent());
                MediaController controller=new MediaController(this);
                add_video.setVideoURI(FileProvider.getUriForFile(this,
                        "com.example.wangjie.mynotes.fileprovider",new File(note.getVideo_path())));
                add_video.setMediaController(controller);
                add_video.start();
            }
            Intent video_intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);//启动录像机
            video_file=new File(getExternalCacheDir(),NoteUtils.getTime()+".mp4");
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N);{
                video_intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(this,
                                "com.example.wangjie.mynotes.fileprovider",video_file));
            }
            if(Build.VERSION.SDK_INT<Build.VERSION_CODES.N){
                video_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(video_file));
            }
            startActivityForResult(video_intent,2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            Bitmap bitmap= BitmapFactory.decodeFile(img_file.getAbsolutePath());
            add_img.setImageBitmap(bitmap);
        }
        if(requestCode==2){
            Bitmap bitmap=BitmapFactory.decodeFile(video_file.getAbsolutePath());
            Log.i("video_file.....",video_file+"");
            MediaController controller=new MediaController(this);
            add_video.setMediaController(controller);
            add_video.setVideoURI( FileProvider.getUriForFile(this,
                    "com.example.wangjie.mynotes.fileprovider",video_file));
            add_video.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_save){
            if("last".equals(isLast)){
                notesManager.update(add_text.getText().toString(),note.getId());
            }else{
                notesManager.add(add_text.getText().toString(),img_file+"",video_file+"");
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
