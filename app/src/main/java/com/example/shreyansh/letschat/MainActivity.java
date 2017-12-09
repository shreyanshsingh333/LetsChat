package com.example.shreyansh.letschat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final String ANONYMOUS = "anonymous";


    private MainAdapter mMainAdapter;
    private ListView mMainListView;

    private String mUsername;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;
    private ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = ANONYMOUS;
        //Initialize firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mMessageDatabaseReference = mFirebaseDatabase.getReference().child("friends");

        //Initialize references to Views
        mMainListView = findViewById(R.id.mainListView);

        //Initialize ListView and its adapter
        List<Main> main = new ArrayList<>();
        mMainAdapter = new MainAdapter(MainActivity.this, R.layout.activity_item_main, main);
        mMainListView.setAdapter(mMainAdapter);
    }

    

}
