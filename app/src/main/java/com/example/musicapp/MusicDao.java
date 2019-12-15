package com.example.musicapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class MusicDao {
    private SQLiteDatabase db;
    private String TABLE_NAME = "table_music";

    public MusicDao(Context context) {
        MusicDB musicDB = new MusicDB(context);
        db = musicDB.getWritableDatabase();
    }
    public ArrayList<Music> findAll(){
        ArrayList<Music> arrayList = new ArrayList<>();
        Cursor cursor =db.query(TABLE_NAME,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String word=cursor.getString(cursor.getColumnIndex("title"));
                String meaning=cursor.getString(cursor.getColumnIndex("name"));
                String instance=cursor.getString(cursor.getColumnIndex("path"));
                Music words=new Music(word,meaning,instance);
                arrayList.add(words);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }
    public void addMucis(Music music){
        ContentValues  contentValues =new ContentValues();
        contentValues.put("title",music.getTitle());
        contentValues.put("name",music.getName());
        contentValues.put("path",music.getPath());
        db.insert(TABLE_NAME,null,contentValues);
        contentValues.clear();
    }

    public void deleteMusic(Music music){
        db.delete(TABLE_NAME,"title =? and path = ?",new String[]{music.getTitle(),music.getPath()});
    }
}
