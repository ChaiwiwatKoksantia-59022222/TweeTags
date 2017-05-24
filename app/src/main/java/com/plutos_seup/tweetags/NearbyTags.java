package com.plutos_seup.tweetags;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.plutos_seup.tweetags.Firebase.Firebase_Search;
import com.plutos_seup.tweetags.Picasso.Picasso;
import com.plutos_seup.tweetags.Recyclerview.Nearby_Adapter;
import com.plutos_seup.tweetags.Recyclerview.Sub_Adapter;

import java.util.ArrayList;

public class NearbyTags extends AppCompatActivity {

    RecyclerView recyclerView;
    protected Firebase_Search firebase_search;
    private FirebaseAuth mAuth;

    final static String database_url = "https://tweetags-512a8.firebaseio.com/";
    String database_url_user;

    Bundle bundle;

    String text,key,count_s;

    ArrayList<String> data;
    Nearby_Adapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_tags);

        bundle = getIntent().getExtras();
        text = bundle.getString("name");
        key = bundle.getString("key");
        count_s = bundle.getString("count");

        LinearLayout back = (LinearLayout)findViewById(R.id.nearby_back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        String user_UID = user.getUid();
        database_url_user = database_url+"User/"+user_UID+"/";

        data = new ArrayList<String>();
        fun_recyclerview(text);

    }

    private void fun_recyclerview(String u){

        recyclerView = (RecyclerView)findViewById(R.id.nearby_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(NearbyTags.this,LinearLayoutManager.VERTICAL,false));

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        DatabaseReference databaseReference_s = FirebaseDatabase.getInstance().getReference();

        final FirebaseUser user_sub = mAuth.getCurrentUser();
        String user_UID = user_sub.getUid();

        final int count = Integer.parseInt(count_s);

        for (int i = 0 ;i < (count); i++){
            String position = "Tag_"+String.valueOf(i).toString();

            final int uy = i;

            DatabaseReference firebase_sub = databaseReference_s.child("User").child(user_UID)
                    .child("Tags").child(key).child("Nearby_Tags").child(position).child("name");
            firebase_sub.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String s = dataSnapshot.getValue().toString();
                    data.add(s);

                    if (uy == (count - 1)){
                        adapter = new Nearby_Adapter(NearbyTags.this,data);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

    }




}
