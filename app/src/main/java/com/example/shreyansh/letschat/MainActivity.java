package com.example.shreyansh.letschat;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

        mFriendListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Friends friend = friends.get(position);
                showUpdateDeleteDialog(friend.getmId(), friend.getmName());
                return true;
            }
        });

    }

    private void addFriend(){
        String name = editTextName.getText().toString().trim();

        //checking if value is provided
        if(!TextUtils.isEmpty(name)){
            //Getting a unique key using push().getKey() method
            //it will create a unique id and we will use it as the primary key
            String id = mFriendsDatabaseReference.push().getKey();

            //creating an Friends object
            Friends friend = new Friends(id,name);

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

    private void showUpdateDeleteDialog(final String friendId, String friendName){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflate = getLayoutInflater();
        final View dialogView = inflate.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName =  dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate =  dialogView.findViewById(R.id.buttonUpdateArtist);
        final Button buttonDelete =  dialogView.findViewById(R.id.buttonDeleteArtist);

        dialogBuilder.setTitle(friendName);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    updateFriend(friendId,name);
                    alertDialog.dismiss();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteFriend(friendId);
                alertDialog.dismiss();

            }
        });
    }

    private boolean updateFriend(String id,String name){
        //getting the specifies reference

        DatabaseReference fdr = FirebaseDatabase.getInstance().getReference("friends").child(id);
       Friends friend = new Friends(id,name);
        fdr.setValue(friend);
        Toast.makeText(getApplicationContext(),"Friend Updated",Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteFriend(String id){
        //getting the specified friend reference
        DatabaseReference fdr = FirebaseDatabase.getInstance().getReference("friends").child(id);

        //deleting friend
        fdr.removeValue();

        Toast.makeText(MainActivity.this,"friend deleted",Toast.LENGTH_LONG).show();
        return true;
    }

}
