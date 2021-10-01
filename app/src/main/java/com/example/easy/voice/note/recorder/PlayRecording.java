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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlayRecording extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    Button buttonPlay, buttonStop, buttonPause;
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
    //RecordingSampler recordingSampler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_recording);
        seekBar=findViewById(R.id.seekBar);
        Bundle bundle = getIntent().getExtras();
        arrayList = bundle.getParcelableArrayList("list");
        int pos = bundle.getInt("pos");
        uri = Uri.parse(arrayList.get(pos).toString());
        mediaPlayer = new MediaPlayer();
        Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
        mediaPlayer = MediaPlayer.create(PlayRecording.this, uri);
        //seek1();
        shareimg = (ImageView) findViewById(R.id.img_share_playrec);
        backimg = (ImageView) findViewById(R.id.img_back);
        buttonPlay=findViewById(R.id.buttonPlay);
        textView=findViewById(R.id.titletool);
        textView.setText(arrayList.get(pos).toString());
        buttonClick = new AlphaAnimation(2F, 0.8F);

        handler=new Handler();


        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(uri.toString());
                    Toast.makeText(PlayRecording.this, "Recording Play", Toast.LENGTH_SHORT).show();
                    mediaPlayer.prepare();
                    mediaPlayer.seekTo(lenght);
                    mediaPlayer.start();
                    lenght = 0;

                    //seekBar.setMax(mediaPlayer.getDuration());
                    //seek();
                    seek1();
                    buttonPlay.startAnimation(buttonClick);
                } catch (IOException e) {

                    e.printStackTrace();
                }

            }
        });

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

    private void seek1() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                seekBar.setMax(mediaPlayer.getDuration());
                for (int i=0;i<=mediaPlayer.getCurrentPosition();i++){
                seekBar.setProgress(i);
                }
                Toast.makeText(PlayRecording.this,String.valueOf(mediaPlayer.getCurrentPosition()),Toast.LENGTH_SHORT).show();
            }
        },1000);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    Toast.makeText(getApplicationContext(), String.valueOf(progress), Toast.LENGTH_LONG).show();
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
            }

        }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
    });
    }
}