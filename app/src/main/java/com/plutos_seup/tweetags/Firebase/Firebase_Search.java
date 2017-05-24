package com.plutos_seup.tweetags.Firebase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.core.Tag;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.plutos_seup.tweetags.Data.Tags;
import com.plutos_seup.tweetags.Recyclerview.Main_Adapter;
import com.plutos_seup.tweetags.Recyclerview.Search_Adapter;

import java.util.ArrayList;

public class Firebase_Search {

    Context context;
    String database_url;
    RecyclerView recyclerView;

    String mode;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference,firebase_s;

    ArrayList<String> datas = new ArrayList<>();
    ArrayList<String> image_ar = new ArrayList<>();
    ArrayList<String> near = new ArrayList<>();
    ArrayList<String> key = new ArrayList<>();

    String rt,ku;

    Search_Adapter adapter;

    public Firebase_Search (Context context, String database_url, RecyclerView recyclerView,String mo,String tr) {
        this.context = context;
        this.database_url = database_url;
        this.recyclerView = recyclerView;

        this.mode = mo;
        this.ku = tr;

        databaseReference = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        String user_UID = user.getUid();

        firebase_s = databaseReference.child("User").child(user_UID);

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
        image_ar.clear();
        near.clear();
        key.clear();

        for (com.google.firebase.database.DataSnapshot ds : dataSnapshot.getChildren()){

            String name = ds.getValue(Tags.class).getTag_name();
            String image = ds.getValue(Tags.class).getCover_url();
            String dot = ds.getValue(Tags.class).getSub_tag_count();
            String keys = ds.getValue(Tags.class).getTag_key();

            int s = name.indexOf(text);

            if (s>=0 && text.length()>0){
                datas.add(name);
                image_ar.add(image);
                near.add(dot);
                key.add(keys);

                rt = text;
            }
        }

        if (datas.size()>0){
            ArrayList<String> w = new ArrayList<>();
            w.add(text);
            rt = text;
            adapter = new Search_Adapter(context,datas,image_ar,near,mode,key,rt,ku);
            recyclerView.setAdapter(adapter);
        }
        else {
            image_ar.clear();
            datas.clear();
            near.clear();
            key.clear();
            if (text.length()==0 || mode.contentEquals("2") == true) {
                for (com.google.firebase.database.DataSnapshot ds : dataSnapshot.getChildren()){

                    String name = ds.getValue(Tags.class).getTag_name();
                    String image = ds.getValue(Tags.class).getCover_url();
                    String dot = ds.getValue(Tags.class).getSub_tag_count();
                    String keys = ds.getValue(Tags.class).getTag_key();

                    if (name.length()>0){
                        datas.add(name);
                        image_ar.add(image);
                        near.add(dot);
                        key.add(keys);

                        rt = text;
                    }
                }
            }
            else {
                String fg = String.valueOf(text.charAt(0));
                boolean sg = fg.contains("#");
                if (sg == false) {
                    datas.add(text);
                    image_ar.add("");
                    near.add("");
                    key.add("");
                    rt = text;
                }
                else {
                    datas.clear();
                    image_ar.clear();
                    near.clear();
                    key.clear();
                }
            }

            adapter = new Search_Adapter(context,datas,image_ar,near,mode,key,rt,ku);
            recyclerView.setAdapter(adapter);

        }



    }

}
