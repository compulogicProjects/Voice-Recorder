package com.easy.voice.note.recorder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        ViewGroup viewGroup= findViewById(android.R.id.content);
        View view= LayoutInflater.from(this).inflate(R.layout.backscreen,viewGroup,false);
        builder.setView(view);
        Button btncancel= view.findViewById(R.id.btncancel);
        Button btnexit= view.findViewById(R.id.btnexit);
        RatingBar ratingBar= view.findViewById(R.id.ratingbar);
        AlertDialog alertDialog= builder.create();

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v==5){
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.easy.voice.note.recorder"); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                else{
                    View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.exitscreen,viewGroup,false);
                    builder.setCancelable(false);
                    builder.setView(view);

                    Button btncancel=view.findViewById(R.id.btncancel);
                    Button btnsubmit=view.findViewById(R.id.btnsubmit);
                    TextView feedbacktext= view.findViewById(R.id.feedbacktext);

                    AlertDialog alertDialog1= builder.create();

                    btncancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog1.dismiss();
                        }
                    });
                    btnsubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (feedbacktext.getText().toString().isEmpty()) {
                                Toast.makeText(HomeActivity.this, "Please Enter Feedback!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HomeActivity.this, "Thanks For Your Feedback!", Toast.LENGTH_SHORT).show();
                                alertDialog1.dismiss();
                                alertDialog.dismiss();
                            }
                        }
                    });
                    alertDialog1.show();
                }
            }
        });

        alertDialog.show();
    }
}