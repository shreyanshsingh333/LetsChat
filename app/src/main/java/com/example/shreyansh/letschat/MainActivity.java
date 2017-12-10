package com.example.shreyansh.letschat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final String ANONYMOUS = "anonymous";


    private FriendsAdapter mFriendsAdapter;
    private ListView mFriendListView;
    TextView editTextName;
    Button buttonAdd;

    private String mUsername;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mFriendsDatabaseReference;
    private ChildEventListener mChildEventListener;


    List<Friends> friends;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = ANONYMOUS;
        //Initialize firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mFriendsDatabaseReference = mFirebaseDatabase.getReference().child("friends");

        //Initialize references to Views
        editTextName = (EditText) findViewById(R.id.editTextName);
        mFriendListView = findViewById(R.id.mainListView);
        buttonAdd = findViewById(R.id.buttonAdd);

        //Initialize ListView and its adapter
        friends = new ArrayList<>();
        //mFriendsAdapter = new FriendsAdapter(MainActivity.this, R.layout.activity_item_main, friends);
        //mFriendListView.setAdapter(mFriendsAdapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend();
            }
        });


       /* Friends friend = new Friends(mUsername);
        mFriendsDatabaseReference.push().setValue(friend);*/





    }

    private void addFriend(){
        String name = editTextName.getText().toString().trim();

        //checking if value is provided
        if(!TextUtils.isEmpty(name)){
            //Getting a unique key using push().getKey() method
            //it will create a unique id and we will use it as the primary key
            String id = mFriendsDatabaseReference.push().getKey();

            //creating an Friends object
            Friends friend = new Friends(name);

            //saving the Friend
            mFriendsDatabaseReference.child(id).setValue(friend);

            //setting editText to blank again
            editTextName.setText("");

            //Displaying a success toast
            Toast.makeText(this,"Friend added", Toast.LENGTH_LONG).show();
        }else{
            //if the value is not given displaying a toast
            Toast.makeText(this,"Please enter a name",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        mFriendsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //clearing the previous artist nodes
                friends.clear();

                //iterating through all the nodes
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    //getting friend
                    Friends friend = postSnapshot.getValue(Friends.class);
                    friends.add(friend);
                }

                //Creating Adapter
                FriendsAdapter friendsAdapter = new FriendsAdapter(MainActivity.this, R.layout.activity_item_main,friends);
                mFriendListView.setAdapter(friendsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
