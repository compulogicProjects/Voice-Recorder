package com.example.easy.voice.note.recorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.easy.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SavedRecording extends AppCompatActivity {
    ImageView backimage;
    private File mediaStorageDir;
    ListView listView;
    String[] songNames;

    int myposition;
    SimpleDateFormat sdf2;
    String[] datetime;
    int[] img;
    int[] dlt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recording);
        backimage=findViewById(R.id.backimage);
        listView = findViewById(R.id.list_item);

        backimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SavedRecording.this,RecordingScreen.class);
                startActivity(intent);
            }
        });

        final ArrayList<File> songs = readsongs(new File(Environment.getExternalStorageDirectory() + "/VCRecorder"));

        sdf2 = new SimpleDateFormat("MM-dd-yy HH:mm a");

        mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "/VCRecorder");
        if (mediaStorageDir.exists()) {
            Toast.makeText(this, "Directory Exists", Toast.LENGTH_LONG).show();
        }

        songNames = new String[songs.size()];
        datetime = new String[songs.size()];
        img = new int[songs.size()];
        dlt = new int[songs.size()];
        for (int i = 0; i < songs.size(); i++) {

            songNames[i] = songs.get(i).getName().replace(".3gp", "");
            datetime[i] = sdf2.format(songs.get(i).lastModified());
            img[i] = R.drawable.ic_baseline_play_circle_filled;
            dlt[i] = R.drawable.ic_baseline_delete;
        }
        if (songNames.length != 0) {

            MyAdapter customAdapter = new MyAdapter(this, songNames, datetime, img, dlt);
         /*   ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(VoiceRecordingList.this,
                    R.layout.vr_list_item, R.id.vr_list_name, songNames);*/
            listView.setAdapter(customAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    myposition=position;
                    Intent intent = new Intent(SavedRecording.this, PlayRecording.class);
                    intent.putExtra("pos", myposition);
                    intent.putExtra("list", songs);
                    startActivity(intent);
                }
            });

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

                } else if (file.getName().endsWith(".3gp")) {
                    arrayList.add(file);
                }
            }
        }
        return arrayList;
    }
}