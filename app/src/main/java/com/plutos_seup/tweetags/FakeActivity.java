package com.plutos_seup.tweetags;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

public class FakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent_s = getIntent();
        String action = intent_s.getAction();
        String type = intent_s.getType();
        if ("text/plain".equals(type)) {
            String text_po = intent_s.getStringExtra(Intent.EXTRA_TEXT);
            Intent intent = new Intent(FakeActivity.this,SearchActivity.class);
            intent.putExtra("mode",1);
            //String result = java.net.URLDecoder.decode(text_po, "UTF-8");
            String result = "";
            try {
                result = java.net.URLDecoder.decode(text_po,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }



            int hash = result.indexOf("/hashtag/");

            if (hash>=0){
                int stop = result.indexOf("?");
                Log.e("hash",String.valueOf(hash).toString());
                Log.e("stop",String.valueOf(stop).toString());

                int start = hash + 9;

                String word = result.substring(start,stop);

                intent.putExtra("text",word);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(FakeActivity.this,"Not Support",Toast.LENGTH_LONG).show();
                finish();
            }

        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void Toast(String text){
        Toast.makeText(FakeActivity.this,text,Toast.LENGTH_SHORT).show();
    }

}
