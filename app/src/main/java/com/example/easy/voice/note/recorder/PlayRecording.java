package com.example.easy.voice.note.recorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlayRecording extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    RelativeLayout btnstartstop;
    TextView textView;
    SeekBar seekBar;
    ArrayList arrayList;
    Uri uri;
    int lenght = 0;
    AlphaAnimation buttonClick;
    private Button refresh;
    private CheckBox startVideoAdsMuted;
    private TextView videoStatus;
    ImageView shareimg, backimg;
    Toolbar toolbar;
    Runnable runnable;
    Handler handler;
    Thread updateseekbar;

    //RecordingSampler recordingSampler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_recording);
        seekBar = findViewById(R.id.seekBar);
       getintent();
        shareimg = (ImageView) findViewById(R.id.img_share_playrec);
        backimg = (ImageView) findViewById(R.id.img_back);
        btnstartstop = findViewById(R.id.btnstartstop);
        textView = findViewById(R.id.titletool);
        buttonClick = new AlphaAnimation(2F, 0.8F);

        handler = new Handler();

        shareimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out the App at: https://play.google.com/store/apps/details?id=" + appPackageName);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                finish();

            }
        });

    }
     public  void getintent(){
        Bundle bundle = getIntent().getExtras();
        arrayList = bundle.getParcelableArrayList("list");
        int pos = bundle.getInt("pos");
        int seek = bundle.getInt("seek");
        String str= bundle.getString("str");
        uri = Uri.parse(arrayList.get(pos).toString());
         Toast.makeText(this, arrayList.get(pos)+"", Toast.LENGTH_SHORT).show();
         //mediaPlayer = new MediaPlayer();
        //mediaPlayer = MediaPlayer.create(PlayRecording.this, uri);
        textView.setText(str);
        seekBar.setProgress(seek);

    }

    public void seek() {
        updateseekbar = new Thread() {
            @Override
            public void run() {
                int totalduration = mediaPlayer.getDuration();
                int currentposition = 0;
                while (currentposition < totalduration) {
                    try {
                        sleep(500);
                        currentposition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentposition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        seekBar.setMax(mediaPlayer.getDuration());
        updateseekbar.start();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                getintent();
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }
}