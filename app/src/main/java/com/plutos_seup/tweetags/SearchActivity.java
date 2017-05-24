package com.plutos_seup.tweetags;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.plutos_seup.tweetags.Firebase.Firebase_Search;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    protected Firebase_Search firebase_search;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;

    Bundle bundle;
    int mode;

    final static String database_url = "https://tweetags-512a8.firebaseio.com/";
    String database_url_user;
    EditText search_et;

    boolean vt = false;
    String k = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_et = (EditText)findViewById(R.id.search_edit_text);

        bundle = getIntent().getExtras();
        mode = bundle.getInt("mode");

        if (mode == 1 || mode == 2){
            String test = bundle.getString("text");
            search_et.setText(test);
            search(test,"D");
            k = test;
        }

        LinearLayout back_btn = (LinearLayout)findViewById(R.id.search_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vt == false){
                    finish();
                }
                else {
                    search_et.setText("");
                    search("","C");
                }
            }
        });

        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String text = search_et.getText().toString();
                search(text,"B");
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mode != 1 || mode != 2){
            search(k,"A");
        }
    }

    private void search(String text,String a){

        Log.e("Y","WER");
        Log.e("I",text);
        Log.e("U",a);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        if (text.length()>0){
            vt = true;
        }
        else {
            vt = false;
        }

        if (user != null){

            String user_UID = user.getUid();
            database_url_user = database_url+"User/"+user_UID+"/";

            recyclerView = (RecyclerView)findViewById(R.id.search_recyclerview);

            recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this,LinearLayoutManager.VERTICAL,false));

            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            String mo = String.valueOf(mode);

            firebase_search = new Firebase_Search(SearchActivity.this,database_url_user,recyclerView,mo,text);

            firebase_search.search(text);

        }

    }

    private void Toast(String text){
        Toast.makeText(SearchActivity.this,text,Toast.LENGTH_SHORT).show();
    }

}
