package com.example.easy.voice.note.recorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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

        seekBar.setProgress(0);
        seekBar.setMax(100);

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(uri.toString());
                    mediaPlayer.prepare();
                    mediaPlayer.seekTo(lenght);
                    mediaPlayer.start();
                    lenght = 0;
                    //seekBar.setMax(mediaPlayer.getDuration());
                    //seek();
                    //seek1();
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

            }
        });

    }

    private void seek1() {
        seekBar.setProgress(0);
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //recordingSampler.stopRecording();
            }
        });
        new Thread((Runnable) this).start();
    }
}