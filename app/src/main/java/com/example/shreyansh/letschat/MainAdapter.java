package com.example.shreyansh.letschat;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Shreyansh on 12/9/2017.
 */

public class MainAdapter extends ArrayAdapter<Main>{
    public MainAdapter(Context context, int resource, List<Main> objects){
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.activity_item_main , parent,false);
        }
        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        Main main = getItem(position);
        nameTextView.setText(main.getmName());
        return convertView;
    }

}
