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
            Intent intent = new Intent(FakeActivity.this,AddActivity.class);
            intent.putExtra("mode",3);
            Toast(text_po);
            Log.e("CHECK",text_po);
            intent.putExtra("text",text_po);
            startActivity(intent);
            finish();
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
