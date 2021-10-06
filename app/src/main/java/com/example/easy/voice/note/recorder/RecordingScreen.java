package com.example.easy.voice.note.recorder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easy.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class RecordingScreen extends AppCompatActivity {
    TextView titlerecording,starttext;
    Chronometer chronometer;
    ProgressBar progressBar;
    RelativeLayout linearLayout;
    ImageView image,saveimage,cancel,backbutton,saverecording;
    public static File mediaStorageDir;
    MediaRecorder mediaRecorder;
    String uuid = "Recording ";
    SimpleDateFormat sdf2=new SimpleDateFormat("MM-dd-yy HH:mm a");
    public String Path = Environment.getExternalStorageDirectory().getAbsolutePath()
            +"/Voice Recorder/"+uuid+".mp3"+sdf2;
    boolean startrecording=false;
    boolean stoprecording=true;
    private boolean pause=false;
    private boolean resume=false;

    long timeWhenStopped = 0;


    private boolean all_permissions = false;


    private final String[] VoiceRecorderPermissions = {Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public static int RecordAudioCode=200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_screen);

        //Initialize
        titlerecording=findViewById(R.id.recordingtitle);
        chronometer=findViewById(R.id.chronometer);
        progressBar=findViewById(R.id.progressbar);
        linearLayout=findViewById(R.id.startlinear);

        image=findViewById(R.id.startimage);
        starttext=findViewById(R.id.start);

        saveimage=findViewById(R.id.saveimage);
        cancel=findViewById(R.id.cancelRecording);

        backbutton=findViewById(R.id.backbutton);

        saverecording=findViewById(R.id.saverecording);

        create_VC_Recorder_Dir();

        getPermission();

        // Save Button
        saveimage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                mediaRecorder.pause();
                timeWhenStopped = SystemClock.elapsedRealtime();
                chronometer.stop();
                LayoutInflater li = LayoutInflater.from(RecordingScreen.this);
                View view1 = li.inflate(R.layout.save_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RecordingScreen.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(view1);

                final EditText userInput = (EditText) view1.findViewById(R.id.editTextDialogUserInput);
                Button cancel1 = (Button) view1.findViewById(R.id.save_cancel);
                Button ok = (Button) view1.findViewById(R.id.save_ok);


                // set dialog message
                alertDialogBuilder
                        .setCancelable(false);

                // create alert dialog
                final AlertDialog alertDialog = alertDialogBuilder.create();
                cancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaRecorder.resume();
                        long intervalOnPause = (SystemClock.elapsedRealtime() - timeWhenStopped);
                        chronometer.setBase( chronometer.getBase() + intervalOnPause );
                        chronometer.start();
                        alertDialog.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newFileName = userInput.getText().toString();
                        if (newFileName != null && newFileName.trim().length() > 0) {
                            File newFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Voice Recorder/"+newFileName+".mp3");
                            File oldFile = new File(Path);
                            oldFile.renameTo(newFile);
                            Toast.makeText(RecordingScreen.this, newFile+"Saved!", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            titlerecording.setText("Tap On Start Button to Record!");
                            image.setImageResource(R.drawable.ic_baseline_keyboard_voice);
                            starttext.setText("Start");
                            startrecording=false;
                            mediaRecorder.stop();
                            mediaRecorder.reset();
                            mediaRecorder.release();
                            mediaRecorder = null;
                            chronometer.setBase(SystemClock.elapsedRealtime());
                            timeWhenStopped=0;
                            chronometer.stop();
                            cancel.setVisibility(View.INVISIBLE);
                            saveimage.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                // show it
                alertDialog.show();
            }
        });

        //backButton
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(RecordingScreen.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // Recording List
        saverecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RecordingScreen.this, SavedRecordingList.class);
                intent.putExtra("back",2);
                startActivity(intent);
                finish();

            }
        });
        // Delete Current Recording
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;
                chronometer.setBase(SystemClock.elapsedRealtime());
                timeWhenStopped=0;
                chronometer.stop();
                File file= new File(Path);
                    file.delete();
                    Toast.makeText(RecordingScreen.this, file + "Deleted", Toast.LENGTH_SHORT).show();
                cancel.setVisibility(View.INVISIBLE);
                saveimage.setVisibility(View.INVISIBLE);
                titlerecording.setText("Tap On Start Button to Record!");
                image.setImageResource(R.drawable.ic_baseline_keyboard_voice);
                starttext.setText("Start");
                startrecording=false;
                //recordingSampler.stopRecording();
            }
        });

        // Start Record Button
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (isMicrophonePresent()){
                    //getPermission();
                }

                 if (startrecording==false) {
                    start_rec();
                    image.setImageResource(R.drawable.ic_baseline_pause);
                    starttext.setText("Stop");
                    saveimage.setVisibility(View.VISIBLE);
                    cancel.setVisibility(View.VISIBLE);
                    chronometer.setBase(SystemClock.elapsedRealtime()+timeWhenStopped);
                     chronometer.start();
                    titlerecording.setText("Recording........");
                    startrecording = true;
                }
                else if(startrecording==true){
                    if (pause==true){
                        resume_rec();
                    }
                    else {pause_rec();}
                }
            }
        });
    }
    // Resume Rec
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void resume_rec(){
        if (mediaRecorder!=null){
            mediaRecorder.resume();
            pause=false;
            image.setImageResource(R.drawable.ic_baseline_pause);
            starttext.setText("Stop");
            long intervalOnPause = (SystemClock.elapsedRealtime() - timeWhenStopped);
            chronometer.setBase( chronometer.getBase() + intervalOnPause );
            chronometer.start();
            titlerecording.setText("Recording.......");

        }
    }
    // pAUSE Rec
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void pause_rec() {
        if (mediaRecorder != null) {
            mediaRecorder.pause();
            pause=true;
            image.setImageResource(R.drawable.ic_baseline_keyboard_voice);
            starttext.setText("Start");
            //timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
            timeWhenStopped = SystemClock.elapsedRealtime();
            chronometer.stop();
            titlerecording.setText("Pause");

    }
    }
    // Start Rec
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void start_rec() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        if(Build.VERSION.SDK_INT < 26) {
            mediaRecorder.setOutputFile(Path);
        }
        else{
            mediaRecorder.setOutputFile(Path);
        }
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
        }
        mediaRecorder.start();
    }
    // Create Directory
    public void create_VC_Recorder_Dir() {
        mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/Voice Recorder");

        if (mediaStorageDir.exists()) {
            //Log.d(TAG, "Dir is already created");
            Toast.makeText(RecordingScreen.this, "Dir is already created" + mediaStorageDir.toString(), Toast.LENGTH_SHORT).show();
        } else {
            mediaStorageDir.mkdir(); // create new directory
            if (mediaStorageDir.isDirectory()) // when dir is created
            {
              //  Log.d(TAG, "Dir is created successfullly");
                Toast.makeText(RecordingScreen.this, "Dir is created Sucessfully" + mediaStorageDir.toString(), Toast.LENGTH_SHORT).show();

            } else {
                //Log.d(TAG, "Failed to create Dir");
                Toast.makeText(RecordingScreen.this, "Dir is failed to created" + mediaStorageDir.toString(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    private boolean isMicrophonePresent(){
        if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }
        else{
            return false;
        }
    }

    private void getMicrophonePermission(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)==
            PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioCode);
        }

    }

    public boolean getPermission(){
        Dexter.withContext(RecordingScreen.this)
                .withPermissions(VoiceRecorderPermissions)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {

                        }
                        else if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            finish();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
        return all_permissions;
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(RecordingScreen.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}