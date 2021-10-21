package com.easy.voice.note.recorder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SavedRecordingList extends AppCompatActivity implements DeleteClisckLisner{
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
    boolean pref;
    int lenght=0;
    final ArrayList<File> songs = readsongs(new File(Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/Voice Recorder"));
    Handler handler;
    Thread updateseekbar;
    Thread updateseekbar1;
    TextView txtstart,textstop;
    LinearLayout bottomlinear;
    SharedPreferences sharedPreferences;
    private static final String TAG = SavedRecordingList.class.getSimpleName();
    TextView songname,starttime,endtime;
    ImageView prebtn,nextbtn;
    RelativeLayout startstopbtn;
    SeekBar seekBar1;
    ImageView startstopbn;
    RelativeLayout bottomlayout;
    LinearLayout deleteall;
    Uri uri;
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
        txtstart=findViewById(R.id.textstart);
        textstop=findViewById(R.id.textstop);
        bottomlinear=findViewById(R.id.bottomlinear);
        bottomlayout= findViewById(R.id.bottomlayout);
        deleteall=findViewById(R.id.delteall);
        handler= new Handler();
        mediaPlayer= new MediaPlayer();
        pref=preferencesutill.getpref(this);
        Toast.makeText(SavedRecordingList.this, pref+"", Toast.LENGTH_SHORT).show();
            /*sharedPreferences= getSharedPreferences(TAG,MODE_PRIVATE);
            Boolean a= sharedPreferences.getBoolean("pref",true);
            String s= sharedPreferences.getString("str",null);
            int i= sharedPreferences.getInt("seekbarval",0);
            //Toast.makeText(SavedRecordingList.this, s+"", Toast.LENGTH_SHORT).show();
            Toast.makeText(SavedRecordingList.this, a+"", Toast.LENGTH_SHORT).show();*/
               /* String presong = preferencesutill.getPreviousSongFromSharedPrefs(this);
                int seekbarpro = preferencesutill.getseekprogressFromSharedPrefs(this);
                String n= preferencesutill.getsongname(this);
                Uri uri1 = Uri.parse(presong);
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri1);*/
        //Toast.makeText(this, n + "", Toast.LENGTH_SHORT).show();
                /*seekBar.setProgress(seekbarpro);
                seekBar1.setProgress(seekbarpro);*//*
                playtitle.setText(n);
//                songname.setText(n);

            //Toast.makeText(SavedRecordingList.this, i+"", Toast.LENGTH_SHORT).show();
            //Toast.makeText(SavedRecordingList.this, s+"", Toast.LENGTH_SHORT).show();*/
    /*    pref=preferencesutill.getpref(this);
        String presong = preferencesutill.getPreviousSongFromSharedPrefs(this);
        Toast.makeText(SavedRecordingList.this, pref+"", Toast.LENGTH_SHORT).show();*/
      /*  sharedPreferences= getSharedPreferences(TAG,MODE_PRIVATE);
        pref= sharedPreferences.getBoolean("pref",true);
        Toast.makeText(SavedRecordingList.this, pref+"", Toast.LENGTH_SHORT).show();*/
        deleteall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(songs.size()>0) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(SavedRecordingList.this);
                    builder.setTitle("Confirmation")
                            .setMessage("Are You Sure You Want To Delete All Files??")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    if (pref){
                                        preferencesutill.clearpref(SavedRecordingList.this);
                                    }
                                    for (int j=0;j<songs.size();j++){
                                        File file = songs.get(j);
                                        Toast.makeText(SavedRecordingList.this, file+"", Toast.LENGTH_SHORT).show();
                                        //songs.remove(i);
                                        file.delete();
                                    }
                                    Toast.makeText(SavedRecordingList.this, "All deleted", Toast.LENGTH_LONG).show();
                                    songs.clear();
                                    customAdapter.notifyDataSetChanged();
                                    listView.setVisibility(View.INVISIBLE);
                                    adapter();
                    /*customAdapter.clear();
                    customAdapter.notifyDataSetChanged();
                    adapter();*/

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    AlertDialog dialog= builder.create();
                    dialog.show();

                } else {
                    Toast.makeText(SavedRecordingList.this, "There is no Recording to delete", Toast.LENGTH_LONG).show();
                }


            }
        });
        if (!songs.isEmpty()){
            uri = Uri.parse(songs.get(myposition).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);

        }
        BottomSheetDialog dialog= new BottomSheetDialog(SavedRecordingList.this);
        View bottomsheetview= getLayoutInflater().inflate(R.layout.bottomsheet,null);
        dialog.setContentView(bottomsheetview);
        songname= bottomsheetview.findViewById(R.id.songname);
        starttime= bottomsheetview.findViewById(R.id.textstart);
        endtime= bottomsheetview.findViewById(R.id.textstop);
        seekBar1=bottomsheetview.findViewById(R.id.seekBar);
        startstopbn= bottomsheetview.findViewById(R.id.startstopbtn);
        prebtn=bottomsheetview.findViewById(R.id.previous);
        startstopbtn=bottomsheetview.findViewById(R.id.btnstartstop);
        nextbtn=bottomsheetview.findViewById(R.id.next);
        bottomlinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name2=listView.getItemAtPosition(myposition).toString();
                if (mediaPlayer.isPlaying()){
                    startstopbn.setImageResource(R.drawable.ic_baseline_pause);
                }
                else{
                    startstopbn.setImageResource(R.drawable.ic_baseline_play_arrow);
                }
                songname.setText(name2);
                String str= playtitle.getText().toString();
                songname.setText(str);
                String starty= createtime(mediaPlayer.getDuration());
                endtime.setText(starty);
                //seek1();
                seek();
                final Handler handler= new Handler();
                int delay=100;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String currentttime=createtime(mediaPlayer.getCurrentPosition());
                        starttime.setText(currentttime);
                        handler.postDelayed(this,delay);
                    }
                },delay);
                nextbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nextsong1();
                    }
                });
                prebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        prevsong1();
                    }
                });
                startstopbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startstop1();
                    }
                });
                dialog.show();
                /*Intent intent = new Intent(SavedRecordingList.this, PlayRecording.class);
                intent.putExtra("pos", myposition);
                intent.putExtra("list", songs);
                intent.putExtra("seek",mediaPlayer.getCurrentPosition());
                String str = listView.getItemAtPosition(myposition).toString();
                intent.putExtra("str",str);
                startActivity(intent);*/
            }
        });

        btnprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevsong();
            }
        });

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextsong();
            }
        });

        btnstartstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startstop();

                //seekBar.setMax(mediaPlayer.getDuration());
                //seek();
                //seek1();
                //buttonPlay.startAnimation(buttonClick);
            }
        });


        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        int seekbarval = mediaPlayer.getCurrentPosition();
                        String str = songs.get(myposition).toString();
                        //Uri uri= Uri.parse(songs.get(myposition).toString());
                        //Toast.makeText(SavedRecordingList.this, uri+"", Toast.LENGTH_SHORT).show();
                        preferencesutill.setPreviousSongInSharedPrefs(SavedRecordingList.this,
                                str, seekbarval, true);
                  /*  sharedPreferences= getSharedPreferences(TAG,MODE_PRIVATE);
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.putString("str",str);
                    editor.putInt("seekbarval",seekbarval);
                    editor.putBoolean("pref",pref);
                    editor.apply();*/
                        mediaPlayer.stop();
                        finish();
                    }
                    else{
                        finish();
                    }
                }
                else{
                    finish();
                    Toast.makeText(SavedRecordingList.this, "Finish before pref", Toast.LENGTH_SHORT).show();
                }
            }
        });


        sdf2 = new SimpleDateFormat("MM-dd-yy HH:mm a");

        adapter();

        /*if (savedInstanceState!=null){

            int m=savedInstanceState.getInt("mplayer");
            String s1=savedInstanceState.getString("songname1");





            if (mediaPlayer.isPlaying()){
                startstopbn.setImageResource(R.drawable.ic_baseline_pause);
            }
            else{
                startstopbn.setImageResource(R.drawable.ic_baseline_play_arrow);
            }
            songname.setText(s1);
            playtitle.setText(s1);
            String starty= createtime(mediaPlayer.getDuration());
            endtime.setText(starty);
            //seek1();
            seek();
            final Handler handler= new Handler();
            int delay=100;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String currentttime=createtime(m);
                    starttime.setText(currentttime);
                    handler.postDelayed(this,delay);
                }
            },delay);

*/






    }
    public void startstop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnstartstop.setImageResource(R.drawable.ic_baseline_play_arrow);
        } else {
            lenght = seekBar.getProgress();
            mediaPlayer.seekTo(lenght);
            mediaPlayer.start();
            btnstartstop.setImageResource(R.drawable.ic_baseline_pause);

        }
    }
    public void startstop1() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            startstopbn.setImageResource(R.drawable.ic_baseline_play_arrow);
            btnstartstop.setImageResource(R.drawable.ic_baseline_play_arrow);
        } else {             lenght = seekBar1.getProgress();
            mediaPlayer.seekTo(lenght);
            mediaPlayer.start();
            startstopbn.setImageResource(R.drawable.ic_baseline_pause);
            btnstartstop.setImageResource(R.drawable.ic_baseline_pause);

        }
    }

    public void nextsong() {
        seekBar.setProgress(0);
        seekBar1.setProgress(0);
        if (myposition < songs.size() - 1) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            myposition=myposition+1;
            String nextsong = songs.get(myposition).toString();
            uri = Uri.parse(nextsong);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            //Toast.makeText(SavedRecordingList.this, "Sound Start", Toast.LENGTH_SHORT).show();
            mediaPlayer.start();
            String str = listView.getItemAtPosition(myposition).toString();
            //Toast.makeText(SavedRecordingList.this, str, Toast.LENGTH_SHORT).show();
            playtitle.setText(str);
            songname.setText(str);
            String endtime1 = createtime(mediaPlayer.getDuration());
            textstop.setText(endtime1);
            endtime.setText(endtime1);
            Log.e("media player", "bottom media player song name:" + playtitle.getText().toString());
            Log.e("media player", "bottom media player song end time" + textstop.getText().toString());
            Log.e("media player", "bottom sheet dialog song name:" + playtitle.getText().toString());
            Log.e("media player", "bottom sheet dialog song end time" + textstop.getText().toString());
            btnstartstop.setImageResource(R.drawable.ic_baseline_pause);
            startstopbn.setImageResource(R.drawable.ic_baseline_pause);
            seek();
        }

        else {
            myposition = 0;
            //myposition=myposition+1;
            mediaPlayer.stop();
            String nextsong = songs.get(myposition).toString();
            uri = Uri.parse(nextsong);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            //Toast.makeText(SavedRecordingList.this, "Sound Start", Toast.LENGTH_SHORT).show();
            mediaPlayer.start();
            String str = listView.getItemAtPosition(myposition).toString();
            //Toast.makeText(SavedRecordingList.this, str, Toast.LENGTH_SHORT).show();
            playtitle.setText(str);
            songname.setText(str);
            String endtime1 = createtime(mediaPlayer.getDuration());
            textstop.setText(endtime1);
            endtime.setText(endtime1);
            Log.e("media player", "bottom media player song name:" + playtitle.getText().toString());
            Log.e("media player", "bottom media player song end time" + textstop.getText().toString());
            Log.e("media player", "bottom sheet dialog player song name:" + playtitle.getText().toString());
            Log.e("media player", "bottom sheet dialog song end time" + textstop.getText().toString());
            btnstartstop.setImageResource(R.drawable.ic_baseline_pause);
            seek();
        }
    }

    public void prevsong(){
        if (myposition>0) {
            myposition=myposition-1;
            mediaPlayer.stop();
            String nextsong = songs.get(myposition).toString();
            uri = Uri.parse(nextsong);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            //Toast.makeText(SavedRecordingList.this, "Sound Start", Toast.LENGTH_SHORT).show();
            mediaPlayer.start();
            String str = listView.getItemAtPosition(myposition).toString();
            //Toast.makeText(SavedRecordingList.this, str, Toast.LENGTH_SHORT).show();
            playtitle.setText(str);
            String endtime= createtime(mediaPlayer.getDuration());
            textstop.setText(endtime);
            btnstartstop.setImageResource(R.drawable.ic_baseline_pause);
            seek();
        }
        else{
            myposition=songs.size();
            myposition=myposition-1;
            mediaPlayer.stop();
            String nextsong = songs.get(myposition).toString();
            uri = Uri.parse(nextsong);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            //Toast.makeText(SavedRecordingList.this, "Sound Start", Toast.LENGTH_SHORT).show();
            mediaPlayer.start();
            String str = listView.getItemAtPosition(myposition).toString();
            //Toast.makeText(SavedRecordingList.this, str, Toast.LENGTH_SHORT).show();
            playtitle.setText(str);
            String endtime= createtime(mediaPlayer.getDuration());
            textstop.setText(endtime);
            btnstartstop.setImageResource(R.drawable.ic_baseline_pause);
            seek();
        }
    }
    public void prevsong1(){
        if (myposition>0) {
            myposition=myposition-1;
            mediaPlayer.stop();
            String nextsong = songs.get(myposition).toString();
            uri = Uri.parse(nextsong);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            //Toast.makeText(SavedRecordingList.this, "Sound Start", Toast.LENGTH_SHORT).show();
            mediaPlayer.start();
            String str = listView.getItemAtPosition(myposition).toString();
            //Toast.makeText(SavedRecordingList.this, str, Toast.LENGTH_SHORT).show();
            songname.setText(str);
            playtitle.setText(str);
            String endtime1= createtime(mediaPlayer.getDuration());
            endtime.setText(endtime1);
            textstop.setText(endtime1);
            Log.e("songname",songname.getText().toString());
            Log.e("playtile",playtitle.getText().toString());
            Log.e("endtime",endtime.getText().toString());
            Log.e("textstop",textstop.getText().toString());
            btnstartstop.setImageResource(R.drawable.ic_baseline_pause);
            startstopbn.setImageResource(R.drawable.ic_baseline_pause);
            //seek1();
            seek();
        }
        else{
            myposition=songs.size();
            myposition=myposition-1;
            mediaPlayer.stop();
            String nextsong = songs.get(myposition).toString();
            uri = Uri.parse(nextsong);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            //Toast.makeText(SavedRecordingList.this, "Sound Start", Toast.LENGTH_SHORT).show();
            mediaPlayer.start();
            String str = listView.getItemAtPosition(myposition).toString();
            //Toast.makeText(SavedRecordingList.this, str, Toast.LENGTH_SHORT).show();
            songname.setText(str);
            playtitle.setText(str);
            String endtime1= createtime(mediaPlayer.getDuration());
            endtime.setText(endtime1);
            textstop.setText(endtime1);
            Log.e("songname",songname.getText().toString());
            Log.e("playtile",playtitle.getText().toString());
            Log.e("endtime",endtime.getText().toString());
            Log.e("textstop",textstop.getText().toString());
            btnstartstop.setImageResource(R.drawable.ic_baseline_pause);
            startstopbn.setImageResource(R.drawable.ic_baseline_pause);
            // seek1();
            seek();
        }
    }
    public  void seek(){
        updateseekbar= new Thread(){
            @Override
            public  void run(){
                int totalduration=mediaPlayer.getDuration();
                int currentposition=0;
                while(currentposition<totalduration){
                    try {
                        sleep(500);
                        if (mediaPlayer!=null){
                            currentposition=mediaPlayer.getCurrentPosition();
                            seekBar.setProgress(currentposition);
                            seekBar1.setProgress(currentposition); }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        };
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar1.setMax(mediaPlayer.getDuration());
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
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btnstartstop.setImageResource(R.drawable.ic_baseline_play_arrow);
                startstopbn.setImageResource(R.drawable.ic_baseline_play_arrow);
                mediaPlayer.reset();
                seekBar.setProgress(0);
                seekBar1.setProgress(0);
                mediaPlayer.setLooping(true);
                nextsong();
            }
        });
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }
    public  void seek1(){
        updateseekbar1= new Thread(){
            @Override
            public  void run(){
                int totalduration=mediaPlayer.getDuration();
                int i= preferencesutill.getseekprogressFromSharedPrefs(SavedRecordingList.this);
                int currentposition=i;
                while(currentposition<totalduration){
                    try {
                        sleep(500);
                        currentposition=mediaPlayer.getCurrentPosition();
                        seekBar1.setProgress(currentposition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        seekBar1.setMax(mediaPlayer.getDuration());
        updateseekbar1.start();
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.stop();
                seekBar.setProgress(0);
                seekBar1.setProgress(0);
                mediaPlayer.setLooping(true);
                //nextsong1();
                nextsong();
            }
        });
    }
    public void nextsong1() {
        if (myposition < songs.size() - 1) {
            myposition = myposition + 1;
            Toast.makeText(SavedRecordingList.this, myposition+"", Toast.LENGTH_SHORT).show();
            mediaPlayer.stop();
            String nextsong = songs.get(myposition).toString();
            uri = Uri.parse(nextsong);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            //Toast.makeText(SavedRecordingList.this, "Sound Start", Toast.LENGTH_SHORT).show();
            mediaPlayer.start();
            String str = listView.getItemAtPosition(myposition).toString();
            //Toast.makeText(SavedRecordingList.this, str, Toast.LENGTH_SHORT).show();
            songname.setText(str);
            playtitle.setText(str);
            String endtime1 = createtime(mediaPlayer.getDuration());
            endtime.setText(endtime1);
            textstop.setText(endtime1);
            Log.e("songname",songname.getText().toString());
            Log.e("playtile",playtitle.getText().toString());
            Log.e("endtime",endtime.getText().toString());
            Log.e("textstop",textstop.getText().toString());
            btnstartstop.setImageResource(R.drawable.ic_baseline_pause);
            startstopbn.setImageResource(R.drawable.ic_baseline_pause);
            //seek1();
            seek();
        } else {
            myposition = 0;
            //myposition = myposition + 1;
            mediaPlayer.stop();
            String nextsong = songs.get(myposition).toString();
            uri = Uri.parse(nextsong);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            //Toast.makeText(SavedRecordingList.this, "Sound Start", Toast.LENGTH_SHORT).show();
            mediaPlayer.start();
            String str = listView.getItemAtPosition(myposition).toString();
            //Toast.makeText(SavedRecordingList.this, str, Toast.LENGTH_SHORT).show();
            playtitle.setText(str);
            songname.setText(str);
            String endtime1 = createtime(mediaPlayer.getDuration());
            endtime.setText(endtime1);
            textstop.setText(endtime1);
            Log.e("songname",songname.getText().toString());
            Log.e("playtile",playtitle.getText().toString());
            Log.e("endtime",endtime.getText().toString());
            Log.e("textstop",textstop.getText().toString());
            btnstartstop.setImageResource(R.drawable.ic_baseline_pause);
            startstopbn.setImageResource(R.drawable.ic_baseline_pause);
            //seek1();
            seek();
        }
    }
    public void adapter() {
        mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Voice Recorder");
        /*if (mediaStorageDir.exists()) {
            //Toast.makeText(this, "Directory Exists", Toast.LENGTH_LONG).show();
        }*/
        /*boolean check= songs.isEmpty();-----
        if(check==true) {
*/
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
            pref=preferencesutill.getpref(this);
            Toast.makeText(SavedRecordingList.this, pref+"", Toast.LENGTH_SHORT).show();
            if (pref){
                String son= preferencesutill.getPreviousSongFromSharedPrefs(this);
                uri= Uri.parse(son);
                mediaPlayer= MediaPlayer.create(SavedRecordingList.this, uri);
                File file= new File(uri.getPath());
                playtitle.setText(file.getName().replace(".mp3",""));
                songname.setText(file.getName().replace(".mp3",""));
                if (mediaPlayer!=null) {
                    seekBar.setMax(mediaPlayer.getDuration());
                    String endtime = createtime(mediaPlayer.getDuration());
                    textstop.setText(endtime);

                    final Handler handler = new Handler();
                    int delay = 1000;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mediaPlayer != null) {
                                String currentttime = createtime(mediaPlayer.getCurrentPosition());
                                txtstart.setText(currentttime);
                            }
                            handler.postDelayed(this, delay);
                        }
                    }, delay);
                    seek();
                }//Toast.makeText(SavedRecordingList.this, file.getName()+"", Toast.LENGTH_SHORT).show();
            }
            else { uri = Uri.parse(songs.get(myposition).toString());
                mediaPlayer = MediaPlayer.create(SavedRecordingList.this, uri);
                name = listView.getItemAtPosition(myposition).toString();
                playtitle.setText(name);
                songname.setText(name);

                //Toast.makeText(SavedRecordingList.this, "Sound Start", Toast.LENGTH_SHORT).show();
                //mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                //seekBar.setProgress(0);
                    /*preferencesutill.setPreviousSongInSharedPrefs(getApplicationContext(),uri,name);
                    pref=true;*/
                //btnstartstop.setImageResource(R.drawable.ic_baseline_pause);
                String endtime = createtime(mediaPlayer.getDuration());
                textstop.setText(endtime);

                final Handler handler = new Handler();
                int delay = 1000;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null) {
                            String currentttime = createtime(mediaPlayer.getCurrentPosition());
                            txtstart.setText(currentttime);
                        }
                        handler.postDelayed(this, delay);
                    }
                }, delay);
                seek();
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    if (mediaPlayer!=null) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                        }
                    }
                    myposition = position;

                    name = adapterView.getItemAtPosition(myposition).toString();
                    playtitle.setText(name);
                    songname.setText(name);
                    uri = Uri.parse(songs.get(myposition).toString());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    Toast.makeText(SavedRecordingList.this, "Sound Start", Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();
                    seekBar.setMax(mediaPlayer.getDuration());
                    //seekBar.setProgress(0);
                    /*preferencesutill.setPreviousSongInSharedPrefs(getApplicationContext(),uri,name);
                    pref=true;*/
                    btnstartstop.setImageResource(R.drawable.ic_baseline_pause);
                    String endtime = createtime(mediaPlayer.getDuration());
                    textstop.setText(endtime);

                    final Handler handler = new Handler();
                    int delay = 1000;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mediaPlayer != null) {
                                String currentttime = createtime(mediaPlayer.getCurrentPosition());
                                txtstart.setText(currentttime);
                            }
                            handler.postDelayed(this, delay);
                        }
                    }, delay);
                    seek();
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
            if (pref) {
                preferencesutill.clearpref(this);
            }
            norecord.setVisibility(View.VISIBLE);
            bottomlayout.setVisibility(View.INVISIBLE);
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
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                int seekbarval = mediaPlayer.getCurrentPosition();
                String str = songs.get(myposition).toString();
                preferencesutill.setPreviousSongInSharedPrefs(SavedRecordingList.this,
                        str, seekbarval, true);
                mediaPlayer.stop();
                finish();

            }
            else{
                finish();
            }
        }
        else
        {
            finish();
        }
    }

    @Override
    public void ondeleteclick(int pos) {
        File file = songs.get(pos);
        boolean getpref=preferencesutill.getpref(SavedRecordingList.this);
        if (getpref) {
            String str = preferencesutill.getPreviousSongFromSharedPrefs(SavedRecordingList.this);
            File filepre = new File(str);
            if (file.equals(filepre)) {
                preferencesutill.clearpref(SavedRecordingList.this);
                file.delete();
                songs.remove(pos);
                adapter();
            }
        }
        else if (file.exists()) {
            file.delete();
            songs.remove(pos);
            adapter();
            //Toast.makeText(this, customAdapter.getItem(pos)+"", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "File Does not Exist", Toast.LENGTH_SHORT).show();
        }
    }

    public String createtime(int duraion){
        String time="";
        int min=duraion/1000/60;
        int sec=duraion/1000%60;

        time+=min+":";

        if (sec<10){
            time+=0;
        }
        time+=sec;

        return time;
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*int newmusic= preferencesutill.getseekprogressFromSharedPrefs(SavedRecordingList.this);
        seekBar.setProgress(newmusic);
        String mv= createtime(newmusic);
        starttime.setText(mv);
        txtstart.setText(mv);
        songname.setText(mv);*/
    }

    /*@Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mplayer",mediaPlayer.getCurrentPosition());
        outState.putString("songname1",playtitle.getText().toString());
        outState.putString("starttime",txtstart.getText().toString());
        outState.putString("endtime",textstop.getText().toString());

        //outState.putString("uri",songs.get(myposition).toString());
    }*/


}

