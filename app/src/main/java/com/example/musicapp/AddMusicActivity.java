package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class AddMusicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music);
        File musicDir = new File("/sdcard/music");
        File[] arrayFile = musicDir.listFiles();
        final ArrayList<String> songName = new ArrayList<>();
        for (File k : arrayFile) {
            songName.add(k.getName());
            Log.e("songname", k.getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                AddMusicActivity.this, R.layout.support_simple_spinner_dropdown_item, songName);
        final ListView listView = (ListView) findViewById(R.id.song_listView);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               String song = songName.get(i);
               String[] songs= song.split("-");
               String name = songs[0];
               String title = songs[1];
               String path = "//sdcard/Music/"+song;
               Music music = new Music(title,name,path);
               MusicDao musicDao = new MusicDao(getBaseContext());
               musicDao.addMucis(music);
               Toast.makeText(getBaseContext(),"添加成功！！！！",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }
}
