package com.example.easy.voice.note.recorder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SavedRecordingList extends AppCompatActivity implements DeleteClisckLisner {
    ImageView backimage;
    TextView norecord;
    private File mediaStorageDir;
    ListView listView;
    String[] songNames;

    int myposition;
    SimpleDateFormat sdf2;
    String[] datetime;
    int[] img;
    int[] dlt;
    MyAdapter customAdapter;
    // bottom playlist
    TextView playtitle;
    SeekBar seekBar ;
    ImageView btnprevious,btnstartstop,btnnext;
    MediaPlayer mediaPlayer;
    String name;
    boolean pref=false;
    int lenght=0;
    final ArrayList<File> songs = readsongs(new File(Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/Voice Recorder"));
    Handler handler;
    Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recording);
        backimage = findViewById(R.id.backimage);
        listView = findViewById(R.id.list_item);
        norecord=findViewById(R.id.norecord);

        //bottom layout widgets
        playtitle=findViewById(R.id.ontitle);
        seekBar=findViewById(R.id.seekbar);
        btnprevious=findViewById(R.id.previous);
        btnstartstop=findViewById(R.id.playpause);
        btnnext=findViewById(R.id.next);
        handler= new Handler();
        mediaPlayer= new MediaPlayer();
        Uri uri= Uri.parse(songs.get(myposition).toString());
        mediaPlayer=MediaPlayer.create(SavedRecordingList.this,uri);
       /* if (pref==true) {
            preferencesutill.getPreviousSongFromSharedPrefs(getApplicationContext());
        }*/

        btnprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myposition>0) {
                    myposition=myposition-1;
                    mediaPlayer.stop();
                    String nextsong = songs.get(myposition).toString();
                    Uri uri = Uri.parse(nextsong);
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    //Toast.makeText(SavedRecordingList.this, "Sound Start", Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();
                    String str = listView.getItemAtPosition(myposition).toString();
                    //Toast.makeText(SavedRecordingList.this, str, Toast.LENGTH_SHORT).show();
                    playtitle.setText(str);
                }
                else{
                    myposition=songs.size();
                    myposition=myposition-1;
                    mediaPlayer.stop();
                    String nextsong = songs.get(myposition).toString();
                    Uri uri = Uri.parse(nextsong);
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    //Toast.makeText(SavedRecordingList.this, "Sound Start", Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();
                    String str = listView.getItemAtPosition(myposition).toString();
                    //Toast.makeText(SavedRecordingList.this, str, Toast.LENGTH_SHORT).show();
                    playtitle.setText(str);
                }

            }
        });

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myposition<songs.size()-1) {
                    myposition=myposition+1;
                    mediaPlayer.stop();
                    String nextsong = songs.get(myposition).toString();
                    Uri uri = Uri.parse(nextsong);
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    //Toast.makeText(SavedRecordingList.this, "Sound Start", Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();
                    String str = listView.getItemAtPosition(myposition).toString();
                    //Toast.makeText(SavedRecordingList.this, str, Toast.LENGTH_SHORT).show();
                    playtitle.setText(str);
                }
                else {
                    myposition=-1;
                    myposition=myposition+1;
                    mediaPlayer.stop();
                    String nextsong = songs.get(myposition).toString();
                    Uri uri = Uri.parse(nextsong);
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    //Toast.makeText(SavedRecordingList.this, "Sound Start", Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();
                    String str = listView.getItemAtPosition(myposition).toString();
                    //Toast.makeText(SavedRecordingList.this, str, Toast.LENGTH_SHORT).show();
                    playtitle.setText(str);
                }

            }
        });

        btnstartstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    lenght=mediaPlayer.getCurrentPosition() ;
                    btnstartstop.setImageResource(R.drawable.ic_baseline_play_arrow);
                }
                else{
                    mediaPlayer.seekTo(lenght);
                    mediaPlayer.start();
                    btnstartstop.setImageResource(R.drawable.ic_baseline_pause);

                }

                //seekBar.setMax(mediaPlayer.getDuration());
                //seek();
                //seek1();
                //buttonPlay.startAnimation(buttonClick);
            }
        });


        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = getIntent();
                int back = intent1.getIntExtra("back", 0);
                if (back == 1) {
                    Intent intent = new Intent(SavedRecordingList.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (back == 2) {
                    Intent intent = new Intent(SavedRecordingList.this, RecordingScreen.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


        sdf2 = new SimpleDateFormat("MM-dd-yy HH:mm a");

        adapter();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btnstartstop.setImageResource(R.drawable.ic_baseline_play_arrow);
            }
        });
    }

    public void adapter() {
        mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Voice Recorder");
        if (mediaStorageDir.exists()) {
            Toast.makeText(this, "Directory Exists", Toast.LENGTH_LONG).show();
        }
        songNames = new String[songs.size()];
        datetime = new String[songs.size()];
        img = new int[songs.size()];
        dlt = new int[songs.size()];
        for (int i = 0; i < songs.size(); i++) {

            songNames[i] = songs.get(i).getName().replace(".mp3", "");
            datetime[i] = sdf2.format(songs.get(i).lastModified());
            img[i] = R.drawable.ic_baseline_play_circle_filled;
            dlt[i] = R.drawable.ic_baseline_delete;
        }
        if (songNames.length != 0) {

            customAdapter = new MyAdapter(this, songNames, datetime, img, dlt, this);
         /*   ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(VoiceRecordingList.this,
                    R.layout.vr_list_item, R.id.vr_list_name, songNames);*/
            listView.setAdapter(customAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    myposition=position;

                    name=adapterView.getItemAtPosition(myposition).toString();
                    playtitle.setText(name);

                    Uri uri= Uri.parse(songs.get(myposition).toString());
                    mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                    Toast.makeText(SavedRecordingList.this, "Sound Start", Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();
                    preferencesutill.setPreviousSongInSharedPrefs(getApplicationContext(),uri,name);
                    pref=true;
                    btnstartstop.setImageResource(R.drawable.ic_baseline_pause);
                    runnable= new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                                int mediacurrentpos=mediaPlayer.getCurrentPosition()/1000;
                                seekBar.setProgress(mediacurrentpos);
                                seekBar.setMin(0);
                                seekBar.setMax(mediaPlayer.getDuration());
                                handler.postDelayed(this,500);

                        }
                    };

  /*                  myposition=position;
                    Uri uri= Uri.parse(songs.get(myposition).toString());
                    mediaPlayer=MediaPlayer.create(SavedRecordingList.this,uri);
                    try {
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(uri.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    playtitle.setText((CharSequence) songs.get(myposition));*/
                    /*myposition = position;
                    Intent intent = new Intent(SavedRecordingList.this, PlayRecording.class);
                    intent.putExtra("pos", myposition);
                    intent.putExtra("list", songs);
                    startActivity(intent);*/

                }
            });
        }
        else {
            norecord.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<File> readsongs(File root) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = root.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (file != null) {
                        arrayList.addAll(readsongs(file));
                    }

                } else if (file.getName().endsWith(".mp3")) {
                    arrayList.add(file);
                }
            }
        }
        return arrayList;
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = getIntent();
        int back = intent1.getIntExtra("back", 0);
        if (back == 1) {
            Intent intent = new Intent(SavedRecordingList.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else if (back == 2) {
            Intent intent = new Intent(SavedRecordingList.this, RecordingScreen.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void ondeleteclick(int pos) {
        File file = songs.get(pos);
        if (file.exists()) {
            //Toast.makeText(this, file + "File Exist", Toast.LENGTH_SHORT).show();
            file.delete();
            songs.remove(pos);
            customAdapter.notifyDataSetChanged();

        } else {
                Toast.makeText(this, "File Does not Exist", Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(this, ""+songs.get(pos) ,Toast.LENGTH_LONG).show();
        }
    }
