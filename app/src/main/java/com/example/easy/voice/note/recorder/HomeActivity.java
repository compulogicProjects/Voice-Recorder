package com.example.easy.voice.note.recorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.easy.R;

public class HomeActivity extends AppCompatActivity {
    LinearLayout startlinear;
    RelativeLayout savedlinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //initilize
        startlinear = findViewById(R.id.startlinear);
        savedlinear = findViewById(R.id.savedrecordinglinear);

        // Start Button
        startlinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, RecordingScreen.class);
                startActivity(intent);

            }
        });
        // Saved Recording Button
        savedlinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SavedRecordingList.class);
                startActivity(intent);

            }
        });
    }
}