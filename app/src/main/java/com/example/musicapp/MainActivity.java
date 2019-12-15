package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{
    private MusicService musicService;
    private ArrayList<Music> musicArrayList = new ArrayList<>();
    private  MusicAdapter musicAdapter;
    private ListView listView;
    private SeekBar seekBar;
    private TextView musicStatus, musicTime,musicTitle;
    private Button btnPlayOrPause;
    private Button btnStop;
    private Button btnPre;
    private Button btnNext;
    private  Button btnRandown;
    private  Button btnOrder;

    private SimpleDateFormat time = new SimpleDateFormat("m:ss");
    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicService = ((MusicService.MyBinder)iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
        }
    };
    private void bindServiceConnection() {
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        startService(intent);
        bindService(intent, sc, this.BIND_AUTO_CREATE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            int permission= ActivityCompat.checkSelfPermission(this,"android.permission.READ_EXTERNAL_STORAGE");
            if(permission!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{"android.permission.READ_EXTERNAL_STORAGE"},1);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        initView();
        setLinster();


        MusicDao musicDao = new MusicDao(getBaseContext());
        musicArrayList = musicDao.findAll();
        musicService = new MusicService();
        musicAdapter = new MusicAdapter(MainActivity.this,R.layout.list_item_title,musicArrayList);
        listView = (ListView) findViewById(R.id.musicTitle_listView);
        listView.setAdapter(musicAdapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Music music =musicArrayList.get(position);
                int musicIndex =position;
                musicService.setMusicIndex(musicIndex);
                musicService.setMusicArrayList(musicArrayList);
                musicService.musicStart();
                bindServiceConnection();
                seekBar.setProgress(musicService.mp.getCurrentPosition());
                seekBar.setMax(musicService.mp.getDuration());

            }
        });


    }
    public void initView(){
        seekBar = (SeekBar)this.findViewById(R.id.musicSeekBar);
        musicStatus = (TextView)this.findViewById(R.id.musicStatus);
        musicTime = (TextView)this.findViewById(R.id.musicTime);
        musicTitle = (TextView) this.findViewById(R.id.musicTitle);
        btnPlayOrPause = (Button)this.findViewById(R.id.btnPlayorPause);
        btnStop = (Button)this.findViewById(R.id.btnStop);
        btnPre = (Button)this.findViewById(R.id.btnPre);
        btnNext = (Button)this.findViewById(R.id.btnNext);
        btnOrder =(Button)this.findViewById(R.id.btnOrder);
        btnRandown =(Button)this.findViewById(R.id.btnRandown);
    }
    public void setLinster(){
        btnPlayOrPause.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPre.setOnClickListener(this);
        btnOrder.setOnClickListener(this);
        btnRandown.setOnClickListener(this);
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPlayorPause:
                musicService.playOrPause();
                break;
            case R.id.btnStop:
                musicService.stop();
                seekBar.setProgress(0);
                break;
            case R.id.btnPre:
                musicService.preMusic();
                break;
            case R.id.btnNext:
                musicService.nextMusic();
                break;
            case R.id.btnOrder:
                break;
            case R.id.btnRandown:
                musicService.randown();
                break;
            default:
                break;
        }
    }

    public android.os.Handler handler = new android.os.Handler();
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            musicTitle.setText(musicService.getTitleSong());
            if(musicService.mp.isPlaying()) {
                musicStatus.setText(getResources().getString(R.string.playing));
                btnPlayOrPause.setText(getResources().getString(R.string.pause).toUpperCase());
            } else {
                musicStatus.setText(getResources().getString(R.string.pause));
                btnPlayOrPause.setText(getResources().getString(R.string.play).toUpperCase());
            }
            musicTime.setText(time.format(musicService.mp.getCurrentPosition()) + "/"
                    + time.format(musicService.mp.getDuration()));
            seekBar.setProgress(musicService.mp.getCurrentPosition());
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        musicService.mp.seekTo(seekBar.getProgress());
                    }
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            handler.postDelayed(runnable, 100);
        }
    };

    @Override
    protected void onResume() {
        if(musicService.mp.isPlaying()) {
            musicStatus.setText(getResources().getString(R.string.playing));
        } else {
            musicStatus.setText(getResources().getString(R.string.pause));
        }

        seekBar.setProgress(musicService.mp.getCurrentPosition());
        seekBar.setMax(musicService.mp.getDuration());
        handler.post(runnable);
        super.onResume();
        Log.d("hint", "handler post runnable");
    }
    @Override
    public void onDestroy() {
        unbindService(sc);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.men_lab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.menu_add:

                Intent intent = new Intent(MainActivity.this,AddMusicActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_refesh:
                Intent intent1 = new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.quit:
                handler.removeCallbacks(runnable);
                unbindService(sc);
                try {
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_content, menu);
        super.onCreateContextMenu(menu, v, menuInfo);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo infor = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final int id=infor.position;
        final int idcontext = item.getItemId();
        switch (idcontext){
            case R.id.menu_delete:
                final  AlertDialog.Builder builderDelte= new AlertDialog.Builder(MainActivity.this);
                final LayoutInflater inflaterDelete = getLayoutInflater();
                final View viewDelete = inflaterDelete.inflate(R.layout.layout_delete,null);
                builderDelte.setView(viewDelete);
                builderDelte.setTitle("删除音乐");
                builderDelte.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderDelte.setPositiveButton("确定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MusicDao musicDao = new MusicDao(getBaseContext());
                        musicDao.deleteMusic(musicArrayList.get(id));
                        Intent intent = new Intent(getBaseContext(),MainActivity.class);
                        startActivity(intent);
                    }
                });
                builderDelte.show();
                break;
            case R.id.menu_add:

                break;
        }
        return super.onContextItemSelected(item);
    }
}
