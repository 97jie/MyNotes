package com.example.wangjie.mynotes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int VERSION=1;
    public static final String DB_NAME="notes.db";
    public static final String TB_NAME="tb_notes";
    public static final String CONTENT="content";
    public static final String PATH="path";
    public static final String VIDEO="video";
    public static final String ID="_id";
    public static final String TIME="time";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TB_NAME+"("
                +ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +CONTENT+" TEXT,"
                +PATH+" TEXT,"
                +VIDEO+" TEXT,"
                +TIME+" TEXT NOT NULL )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
