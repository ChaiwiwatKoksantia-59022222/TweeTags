package com.plutos_seup.tweetags.Firebase;

import android.provider.Contacts;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.plutos_seup.tweetags.Data.Tags;

/**
 * Created by androidworkspace on 4/27/2017 AD.
 */

public class Firebase_Client_Add {

    final static String database_url = "https://tweetags-512a8.firebaseio.com/";

    public Firebase_Client_Add() {
    }

    public void saveOnline(String name,String url,String key,String Uid,String date,String demo){

        for (int i = 0;i<2;i++){
            if (i == 0){
                Tags tags = new Tags();
                tags.setTag_name(name);
                tags.setCover_url(url);
                tags.setTag_key(key);
                tags.setTag_date(date);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference firebase = databaseReference.child("User").child(Uid).child("Tags");
                firebase.child(key).setValue(tags);
            }
            else {
                Tags tags_2 = new Tags();
                tags_2.setTag_name("");
                tags_2.setCover_url("");
                tags_2.setTag_key(demo);
                tags_2.setTag_date(date);

                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference();
                DatabaseReference firebase_s4 = databaseReference3.child("User").child(Uid).child("Tags");
                firebase_s4.child(demo).setValue(tags_2);

            }
        }





    }


}
