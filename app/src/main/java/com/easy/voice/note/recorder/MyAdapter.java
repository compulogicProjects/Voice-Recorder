package com.easy.voice.note.recorder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.example.easy.R;

import java.io.File;
import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<String> {
    Context context;
    String[]  Songname;
    String[]  lastdatemodified;
    int[] playimage;
    int[] deleteimage;
    DeleteClisckLisner deleteClisckLisner;

    public  MyAdapter(Context context,String[] Songname,String[] lastdatemodified,int[] playimage,
                      int[] deleteimage,DeleteClisckLisner deleteClisckLisner){
        super(context, R.layout.list_item,Songname);
        this.context=context;
        this.Songname=Songname;
        this.lastdatemodified=lastdatemodified;
        this.playimage=playimage;
        this.deleteimage=deleteimage;
        this.deleteClisckLisner=deleteClisckLisner;

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item, null, true);

        TextView songname = (TextView) view.findViewById(R.id.vr_list_name);
        TextView datetime = (TextView) view.findViewById(R.id.vr_list_date);
        ImageView deleteimg= (ImageView) view.findViewById(R.id.vr_delete_icon);
        ImageView shareimg= (ImageView) view.findViewById(R.id.vr_share);


        deleteimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder.setTitle("Confirm Delete")
                        .setMessage("Are You Sure You want to Delete")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteClisckLisner.ondeleteclick(position);
                                notifyDataSetChanged();

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

             /*   File mediaStorageDir = new File(Environment.getExternalStorageDirectory().
                        getAbsolutePath(), "/Voice Recorder");
                if (mediaStorageDir.exists()) {
                    String Pos= Songname[position];
                   Toast.makeText(context, ""+Pos, Toast.LENGTH_LONG).show();
                }*/
        /*        final ArrayList<File> songs = readsongs(new File(Environment.
                        getExternalStorageDirectory().getAbsolutePath() + "/Voice Recorder"));
                String[] songNames = new String[songs.size()];
                Toast.makeText(context, songNames+"", Toast.LENGTH_SHORT).show();
                String path= Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/Voice Recorder/";
                File file = new File(path);
             if (file.exists()){
                 Toast.makeText(context, file+"File Exist", Toast.LENGTH_SHORT).show();
                 file.delete();
             }
             else {
                 Toast.makeText(context, "File Does not Exist", Toast.LENGTH_SHORT).show();
             }
             */
            }
        });
        shareimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().
                        getPath()+"/Voice Recorder/"+Songname[position]+".mp3"));
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("audio/*");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                context.startActivity(Intent.createChooser(share, "Shared Recording"));*/

                File file = new File(Environment.getExternalStorageDirectory().getPath()+
                        "/Voice Recorder/"+Songname[position]+".mp3");
                Intent install = new Intent(Intent.ACTION_SEND);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                install.setType("audio/*");
                Uri apkURI = FileProvider.getUriForFile(
                        context,
                        context.getApplicationContext()
                                .getPackageName() + ".provider", file);
                install.putExtra(Intent.EXTRA_STREAM,apkURI);


                context.startActivity(Intent.createChooser(install,"Shared Recording"));

            }
        });

        songname.setText(Songname[position]);
        datetime.setText(lastdatemodified[position]);
        deleteimg.setImageResource(deleteimage[position]);

        return view;
    }

}
