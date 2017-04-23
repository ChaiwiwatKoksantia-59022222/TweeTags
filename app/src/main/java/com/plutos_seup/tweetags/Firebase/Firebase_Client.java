package com.plutos_seup.tweetags.Firebase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
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

    Firebase firebase ;
    ArrayList<Tags> main_datas = new ArrayList<>();
    Main_Adapter adapter;

    public Firebase_Client (Context context, String database_url, RecyclerView recyclerView) {
        this.context = context;
        this.database_url = database_url;
        this.recyclerView = recyclerView;

        Firebase.setAndroidContext(context);
        firebase = new Firebase(database_url);

    }



    public void refresh(){

        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getUpdate(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getUpdate(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                getUpdate(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

    }

    private void getUpdate(DataSnapshot dataSnapshot){


        main_datas.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()){

            Tags main_data = new Tags();

            main_data.setTag_name(ds.getValue(Tags.class).getTag_name());
            main_data.setCover_url(ds.getValue(Tags.class).getCover_url());

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
