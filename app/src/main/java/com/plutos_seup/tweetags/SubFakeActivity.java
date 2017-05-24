package com.plutos_seup.tweetags;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

public class SubFakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_fake);

        Intent intent_s = getIntent();
        String action = intent_s.getAction();
        String type = intent_s.getType();
        if ("text/plain".equals(type)) {
            String text_po = intent_s.getStringExtra(Intent.EXTRA_TEXT);
            Intent intent = new Intent(SubFakeActivity.this,SearchActivity.class);
            intent.putExtra("mode",2);
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


                int start = hash + 9;

                String word = result.substring(start,stop);

                intent.putExtra("text",word);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(SubFakeActivity.this,"Not Support",Toast.LENGTH_LONG).show();
                finish();
            }

        }

    }
}
