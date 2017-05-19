package com.plutos_seup.tweetags.Firebase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.plutos_seup.tweetags.Recyclerview.Search_Adapter;

import java.util.ArrayList;

public class Firebase_Search {

    Context context;
    String database_url;
    RecyclerView recyclerView;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference,firebase_s;

    Firebase firebase ;
    ArrayList<Tags> main_datas = new ArrayList<>();
    ArrayList<String> datas = new ArrayList<>();
    Search_Adapter adapter;

    public Firebase_Search (Context context, String database_url, RecyclerView recyclerView) {
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

    public void search(final String text){

        firebase_s.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                getData(dataSnapshot,text);
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                getData(dataSnapshot,text);
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

    private void getData(com.google.firebase.database.DataSnapshot dataSnapshot,String text){

        datas.clear();
        for (com.google.firebase.database.DataSnapshot ds : dataSnapshot.getChildren()){

            String name = ds.getValue(Tags.class).getTag_name();

            int s = name.indexOf(text);

            if (s>=0 && text.length()>0){
                datas.add(name);
            }
        }

        Log.d("TAG", String.valueOf(datas.size()).toString());
        if (datas.size()>0){
            adapter = new Search_Adapter(context,datas);
            recyclerView.setAdapter(adapter);
        }
        else {
            datas.clear();
            if (text.length()==0) {
                for (com.google.firebase.database.DataSnapshot ds : dataSnapshot.getChildren()){

                    String name = ds.getValue(Tags.class).getTag_name();

                    if (name.length()>0){
                        datas.add(name);
                    }
                }
            }
            else {
                String fg = String.valueOf(text.charAt(0));
                boolean sg = fg.contains("#");
                if (sg == false) {
                    datas.add(text);
                }
                else {
                    datas.clear();
                }
            }
            adapter = new Search_Adapter(context,datas);
            recyclerView.setAdapter(adapter);
        }


    }

}
