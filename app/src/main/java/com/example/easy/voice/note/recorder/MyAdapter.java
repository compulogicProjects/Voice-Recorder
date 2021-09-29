package com.example.easy.voice.note.recorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.easy.R;

public class MyAdapter extends ArrayAdapter<String> {
    Context context;
    String[]  Songname;
    String[]  lastdatemodified;
    int[] playimage;
    int[] deleteimage;

    public  MyAdapter(Context context,String[] Songname,String[] lastdatemodified,int[] playimage,
                      int[] deleteimage){
        super(context, R.layout.list_item,Songname);
        this.context=context;
        this.Songname=Songname;
        this.lastdatemodified=lastdatemodified;
        this.playimage=playimage;
        this.deleteimage=deleteimage;

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item, null, true);

        ImageView playimg = (ImageView) view.findViewById(R.id.vr_list_icon);
        TextView songname = (TextView) view.findViewById(R.id.vr_list_name);
        TextView datetime = (TextView) view.findViewById(R.id.vr_list_date);

        playimg.setImageResource(playimage[position]);
        songname.setText(Songname[position]);
        datetime.setText(lastdatemodified[position]);


        return view;
    }
}
