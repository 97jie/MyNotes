package com.example.wangjie.mynotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NotesManager {
    private DBHelper dbHelper;
    public NotesManager(Context context) {
        dbHelper=new DBHelper(context);
    }

    public void add(String text,String img_path,String video_path){
        SQLiteDatabase dbWriter=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DBHelper.CONTENT,text);
        values.put(DBHelper.TIME,NoteUtils.getTime());
        values.put(DBHelper.PATH,img_path);
        values.put(DBHelper.VIDEO,video_path);
        dbWriter.insert(DBHelper.TB_NAME,null,values);
    }

    public List<Notes> getAllContent(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query(DBHelper.TB_NAME,null,null,null,null,null,DBHelper.TIME+" DESC");
        List<Notes> list=new ArrayList<>();
        while (cursor.moveToNext()){
            Notes note=new Notes();
            note.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.ID)));
            note.setContent(cursor.getString(cursor.getColumnIndex(DBHelper.CONTENT)));
            note.setTime(cursor.getString(cursor.getColumnIndex(DBHelper.TIME)));
            note.setImg_path(cursor.getString(cursor.getColumnIndex(DBHelper.PATH)));
            note.setVideo_path(cursor.getString(cursor.getColumnIndex(DBHelper.VIDEO)));
            list.add(note);
        }
        cursor.close();
        db.close();
        return list;
    }

    public void delNotesByid(int id){//根据id删除notes
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.execSQL("delete from "+DBHelper.TB_NAME+" WHERE _ID=?" ,new String[]{id+""});
        db.close();
    }

    public List<Notes> getNotesByContent(String content){//根据输入的内容模糊查询
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query(DBHelper.TB_NAME,null,DBHelper.CONTENT+" like ?",new String[]{"%"+content+"%"},
                null,null,DBHelper.TIME+" DESC");
        List<Notes> list=new ArrayList<>();
        while (cursor.moveToNext()){
            Notes note=new Notes();
            note.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.ID)));
            note.setContent(cursor.getString(cursor.getColumnIndex(DBHelper.CONTENT)));
            note.setTime(cursor.getString(cursor.getColumnIndex(DBHelper.TIME)));
            note.setImg_path(cursor.getString(cursor.getColumnIndex(DBHelper.PATH)));
            note.setVideo_path(cursor.getString(cursor.getColumnIndex(DBHelper.VIDEO)));
            list.add(note);
        }
        cursor.close();
        db.close();
        return list;
    }

    public void update(String content,int id){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DBHelper.CONTENT,content);
        values.put(DBHelper.TIME,NoteUtils.getTime());
        db.update(DBHelper.TB_NAME,values,DBHelper.ID+"=?",new String[]{id+""});
        Log.i("data........",content);
//        db.execSQL("update "+DBHelper.TB_NAME+" set "+
//                DBHelper.CONTENT+"=?,"+DBHelper.TIME+"="+NoteUtils.getTime()+
//                " where "+DBHelper.ID+"=?",new String[]{content,id+""});
        db.close();
    }
}
