package com.example.wangjie.mynotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    private MyAdapter adapter;
    private NotesManager manager;
    private FloatingActionButton fab_add;
    private ListView lv_mian;
    private EditText et_search;
    public Intent intent;
    private Toolbar tb_main;
    List<Notes> data;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager=new NotesManager(this);
        lv_mian=findViewById(R.id.lv_main);
        et_search=findViewById(R.id.et_search);
        fab_add=findViewById(R.id.fab_add);
        fab_add.setOnClickListener(this);
        tb_main=findViewById(R.id.tb_main);
        setSupportActionBar(tb_main);
        lv_mian.setOnItemLongClickListener(this);
        lv_mian.setOnItemClickListener(this);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str=et_search.getText().toString();
                if(str.length()>0){
                    new Handler().post(eChanged);
                }else{
                    new Handler().post(eChanged);
                }
            }
        });
    }

    Runnable eChanged=new Runnable() {
        @Override
        public void run() {
            String str=et_search.getText().toString();
            data.clear();
            data.addAll(manager.getNotesByContent(str));
            adapter.notifyDataSetChanged();
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listview,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_grid){
            Intent intent=new Intent(this,MainActivity_2.class);
            intent.putExtra("list", (Serializable) data);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public  void setView(){
        data=new ArrayList<>();
        data=manager.getAllContent();
        adapter=new MyAdapter(this,data);
        lv_mian.setAdapter(adapter);
        lv_mian.setEmptyView(findViewById(R.id.tv_noNotes));
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(dialog!=null){
            dialog.dismiss();
        }
        if(et_search.getText().toString().length()>0){
            data.clear();
            data.addAll(manager.getNotesByContent(et_search.getText().toString()));
            adapter.notifyDataSetChanged();
            return;
        }
        setView();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(MainActivity.this,NoteContentActivity.class);
        Notes note=data.get(i);
        intent.putExtra("data",note);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int i, long l) {
        AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this,R.style.alertDialog);
        adb.setTitle("确定要删除吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int n) {
                final int id=data.get(i).getId();
                data.remove(i);
                Log.i("删除后，data还剩下",data.toString());
                adapter.notifyDataSetChanged();
                new Thread(){
                    @Override
                    public void run() {
                        manager.delNotesByid(id);
                    }
                }.start();
            }
        }).setNegativeButton("取消",null).show();

        return true;
    }

    @Override
    public void onClick(View view) {
        intent=new Intent(this, AddContentActivity.class);
        switch (view.getId()){
            case R.id.fab_add:
                setDialog();
                break;
            case R.id.btn_text:
                intent.putExtra("flag","1");
                startActivity(intent);
                break;
            case R.id.btn_img:
                intent.putExtra("flag","2");
                startActivity(intent);
                break;
            case R.id.btn_video:
                intent.putExtra("flag","3");
                startActivity(intent);
                break;
            case R.id.cancel:
                dialog.dismiss();
                break;

        }
    }

    private void setDialog() {
        dialog=new Dialog(this,R.style.BottomDialog);
        LinearLayout root= (LinearLayout) LayoutInflater.from(this).inflate(R.layout.bottom_dialog,null);
        root.findViewById(R.id.btn_text).setOnClickListener(this);
        root.findViewById(R.id.btn_img).setOnClickListener(this);
        root.findViewById(R.id.btn_video).setOnClickListener(this);
        root.findViewById(R.id.cancel).setOnClickListener(this);

        dialog.setContentView(root);
        Window dialogWindow=dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        //dialogWindow.setWindowAnimations(R.style.dialogstyle); 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        dialog.show();
    }
}
