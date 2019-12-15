package com.example.musicapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MusicDB extends SQLiteOpenHelper {
    public static  final String CREAT_MUSIC="create table table_music("
            +"id integer primary key autoincrement,"
            +"title text,"
            +"name text,"
            +"path text)";
    private  static  final  String DB_NAME="music.db";
    private  static final int DB_VERSION=1;
    public MusicDB(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREAT_MUSIC);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
