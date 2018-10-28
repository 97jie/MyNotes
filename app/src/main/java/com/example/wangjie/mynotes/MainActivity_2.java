package com.example.wangjie.mynotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.GridView;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity_2 extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private NotesManager manager;
    private GridAdapter adapter;
    private GridView gv_main;
    private FloatingActionButton fab_add2;
    private Toolbar tb_main2;
    private Dialog dialog;
    private EditText grid_et_search;
    List<Notes> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        manager=new NotesManager(this);
        gv_main=findViewById(R.id.gv_main);
        fab_add2=findViewById(R.id.fab_add2);
        fab_add2.setOnClickListener(this);
        tb_main2=findViewById(R.id.tb_main2);
        setSupportActionBar(tb_main2);
        Intent intent=getIntent();
        data=new ArrayList<>();
        data= (List<Notes>) intent.getSerializableExtra("list");
        gv_main.setOnItemClickListener(this);
        gv_main.setOnItemLongClickListener(this);
        grid_et_search=findViewById(R.id.grid_et_search);
        grid_et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str=grid_et_search.getText().toString();
                data.clear();//大坑，内存指向问题
                data.addAll(manager.getNotesByContent(str));
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(dialog!=null){
            dialog.dismiss();
        }
        if(grid_et_search.getText().toString().length()>0){
            data.clear();
            data.addAll(manager.getNotesByContent(grid_et_search.getText().toString()));
            adapter.notifyDataSetChanged();
            return;
        }
        data=manager.getAllContent();
        adapter=new GridAdapter(this,data);
        gv_main.setAdapter(adapter);
        gv_main.setEmptyView(findViewById(R.id.tv_noNotes));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gridview,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_list){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Intent intent2 = new Intent(this, AddContentActivity.class);
        switch (view.getId()) {
            case R.id.fab_add2:
                setDialog();
                break;
            case R.id.btn_text:
                intent2.putExtra("flag", "1");
                startActivity(intent2);
                break;
            case R.id.btn_img:
                intent2.putExtra("flag", "2");
                startActivity(intent2);
                break;
            case R.id.btn_video:
                intent2.putExtra("flag", "3");
                startActivity(intent2);
                break;
            case R.id.cancel:
                dialog.dismiss();
                break;

        }
    }
    private void setDialog(){
        dialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.bottom_dialog, null);
        root.findViewById(R.id.btn_text).setOnClickListener(this);
        root.findViewById(R.id.btn_img).setOnClickListener(this);
        root.findViewById(R.id.btn_video).setOnClickListener(this);
        root.findViewById(R.id.cancel).setOnClickListener(this);
        dialog.setContentView(root);
        Window dialogWindow = dialog.getWindow();
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(MainActivity_2.this,NoteContentActivity.class);
        Notes note=data.get(i);
        intent.putExtra("data",note);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
        AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity_2.this,R.style.alertDialog);
        adb.setTitle("确定要删除吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int n) {
                final int id=data.get(i).getId();
                data.remove(i);
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
}
