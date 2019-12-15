package com.example.musicapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MusicAdapter extends ArrayAdapter<Music> {
    private  int resourced;

    public MusicAdapter(@NonNull Context context, int resource, @NonNull List<Music> objects ){
        super(context, resource, objects);
        this.resourced = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Music music = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourced,parent,false);
        TextView musicTitle = (TextView) view.findViewById(R.id.list_view_title_item);
        musicTitle.setText(music.getTitle()+"-"+music.getName());
        return view;
    }
}
