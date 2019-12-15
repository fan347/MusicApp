package com.example.musicapp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicService extends Service {

   private String titleSong;
   private ArrayList<Music> musicArrayList =new ArrayList<>();
   private  int musicIndex;

    public String getTitleSong() {
        return titleSong;
    }

    public void setTitleSong(String titleSong) {
        this.titleSong = titleSong;
    }

    public ArrayList<Music> getMusicArrayList() {
        return musicArrayList;
    }

    public void setMusicArrayList(ArrayList<Music> musicArrayList) {
        this.musicArrayList = musicArrayList;
    }

    public int getMusicIndex() {
        return musicIndex;
    }

    public void setMusicIndex(int musicIdex) {
        this.musicIndex = musicIdex;
    }

    public final IBinder binder = new MyBinder();
    public class MyBinder extends Binder{
        MusicService getService() {
            return MusicService.this;
        }
    }
    public static MediaPlayer mp = new MediaPlayer();
    public MusicService() { }
    public void musicStart(){
        try {
            mp.reset();
            mp.setDataSource(musicArrayList.get(musicIndex).getPath());
            mp.prepare();
            titleSong = musicArrayList.get(musicIndex).getTitle();
        } catch (Exception e) {
            Log.d("hint","can't get to the song");
            e.printStackTrace();
        }
    }
    public void playOrPause() {
        titleSong = musicArrayList.get(musicIndex).getTitle();
        if(mp.isPlaying()){
            mp.pause();
        } else {
            mp.start();
        }
    }
    public void stop() {
        if(mp != null) {
            mp.stop();
            try {
                mp.prepare();
                mp.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void nextMusic() {
        if(mp != null && musicIndex <= musicArrayList.size()) {
            mp.stop();
            try {
                mp.reset();
                mp.setDataSource(musicArrayList.get(musicIndex+1).getPath());
                musicIndex++;
                titleSong = musicArrayList.get(musicIndex).getTitle();
                mp.prepare();
                mp.seekTo(0);
                mp.start();
            } catch (Exception e) {
                Log.d("hint", "can't jump next music");
                e.printStackTrace();
            }
        }
    }
    public void preMusic() {
        if(mp != null && musicIndex > 0) {
            mp.stop();
            try {
                mp.reset();
                mp.setDataSource(musicArrayList.get(musicIndex-1).getPath());
                musicIndex--;
                titleSong = musicArrayList.get(musicIndex).getTitle();
                mp.prepare();
                mp.seekTo(0);
                mp.start();
            } catch (Exception e) {
                Log.d("hint", "can't jump pre music");
                e.printStackTrace();
            }
        }
    }
    public void randown() {
            musicIndex = new Random().nextInt(musicArrayList.size());
            try {
                mp.reset();
                mp.setDataSource(musicArrayList.get(musicIndex).getPath());
                titleSong = musicArrayList.get(musicIndex).getTitle();
                mp.prepare();
                mp.seekTo(0);
                mp.start();
            } catch (Exception e) {
                Log.d("hint", "can't jump pre music");
                e.printStackTrace();
            }

    }
    public void order(){

    }
    @Override
    public void onDestroy() {
        mp.stop();
        mp.release();
        super.onDestroy();
    }

    /**
     * onBind 是 Service 的虚方法，因此我们不得不实现它。
     * 返回 null，表示客服端不能建立到此服务的连接。
     */
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
