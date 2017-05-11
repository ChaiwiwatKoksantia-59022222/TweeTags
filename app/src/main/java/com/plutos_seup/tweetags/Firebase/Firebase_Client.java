package com.plutos_seup.tweetags.Firebase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.plutos_seup.tweetags.Data.Tags;
import com.plutos_seup.tweetags.Recyclerview.Main_Adapter;

import java.util.ArrayList;

/**
 * Created by androidworkspace on 4/21/2017 AD.
 */

public class Firebase_Client {

    Context context;
    String database_url;
    RecyclerView recyclerView;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference,firebase_s;

    Firebase firebase ;
    ArrayList<Tags> main_datas = new ArrayList<>();
    Main_Adapter adapter;

    public Firebase_Client (Context context, String database_url, RecyclerView recyclerView) {
        this.context = context;
        this.database_url = database_url;
        this.recyclerView = recyclerView;

        databaseReference = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        String user_UID = user.getUid();

        firebase_s = databaseReference.child("User").child(user_UID);

        //Firebase.setAndroidContext(context);
       // firebase = new Firebase(database_url);

    }

    public void refresh(){

        firebase_s.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                getUpdate(dataSnapshot);
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                getUpdate(dataSnapshot);
            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void getUpdate(com.google.firebase.database.DataSnapshot dataSnapshot){


        main_datas.clear();
        for (com.google.firebase.database.DataSnapshot ds : dataSnapshot.getChildren()){

            Tags main_data = new Tags();

            main_data.setTag_name(ds.getValue(Tags.class).getTag_name());
            main_data.setCover_url(ds.getValue(Tags.class).getCover_url());
            main_data.setTag_key(ds.getValue(Tags.class).getTag_key());
            main_data.setTag_date(ds.getValue(Tags.class).getTag_date());

            main_datas.add(main_data);

        }

        if (main_datas.size()>0){
            adapter = new Main_Adapter(context,main_datas);
            recyclerView.setAdapter(adapter);
        }
        else {
            Toast.makeText(context,"No Card",Toast.LENGTH_SHORT).show();
        }


    }

}
