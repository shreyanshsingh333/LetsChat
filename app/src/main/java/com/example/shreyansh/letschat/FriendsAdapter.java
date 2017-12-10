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

public class FriendsAdapter extends ArrayAdapter<Friends>{
    public FriendsAdapter(Context context, int resource, List<Friends> objects){
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
       convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.activity_item_main , null,true);

        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        Friends friends = getItem(position);
        nameTextView.setText(friends.getmName());
        return convertView;
    }

}
