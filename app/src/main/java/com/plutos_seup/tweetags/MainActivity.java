package com.plutos_seup.tweetags;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.plutos_seup.tweetags.Firebase.Firebase_Client;
import com.plutos_seup.tweetags.Recyclerview.Main_Adapter;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.models.User;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    final static String database_url = "https://tweetags-512a8.firebaseio.com/";
    String database_url_user;

    Firebase_Client firebase_client;

    RecyclerView recyclerView;

    LinearLayout menu_drawer_btn;
    LinearLayout menu_drawer_layout;
    ImageView open;
    ImageView close;
    Boolean menu_open = false;
    de.hdodenhof.circleimageview.CircleImageView main_image_profile;
    TextView sign_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menu_drawer_btn = (LinearLayout) findViewById(R.id.main_menu_drawer_btn);
        menu_drawer_layout = (LinearLayout)findViewById(R.id.main_menu_drawer_layout);
        open = (ImageView)findViewById(R.id.main_menu_drawer_btn_open);
        close = (ImageView)findViewById(R.id.main_menu_drawer_btn_close);

        ImageView main_add_btn = (ImageView)findViewById(R.id.main_add_btn);


        main_image_profile = (de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.main_image_profile);

        sign_out = (TextView)findViewById(R.id.main_sign_out);
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_out_dialog();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            main_image_profile.setVisibility(View.VISIBLE);
            Uri image = user.getPhotoUrl();
            Picasso.with(MainActivity.this).load(image).into(main_image_profile);

            String user_UID = user.getUid();
            database_url_user = database_url+"User/"+user_UID+"/";

            recyclerView = (RecyclerView)findViewById(R.id.main_recyclerview);

            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false));

            firebase_client = new Firebase_Client(MainActivity.this,database_url_user,recyclerView);

            firebase_client.refresh();


        }

        main_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user != null) {
                    Intent intent = new Intent(MainActivity.this,AddActivity.class);
                    startActivity(intent);
                }
            }
        });


        open.setVisibility(View.VISIBLE);
        close.setVisibility(View.GONE);
        menu_drawer_layout.setVisibility(View.INVISIBLE);
        menu_drawer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu_open == false) {
                    menu_open();
                }
                else {
                    menu_close();
                }
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void menu_open(){
        open.setVisibility(View.GONE);
        close.setVisibility(View.VISIBLE);
        TranslateAnimation animation_in = new TranslateAnimation(0,0,0-menu_drawer_layout.getHeight(),0);
        animation_in.setDuration(500);
        animation_in.setFillAfter(true);
        menu_drawer_layout.setAnimation(animation_in);
        menu_drawer_layout.setVisibility(View.VISIBLE);
        menu_open = true;
    }

    private void menu_close(){
        open.setVisibility(View.VISIBLE);
        close.setVisibility(View.GONE);
        TranslateAnimation animation_out = new TranslateAnimation(0,0, 0, 0-menu_drawer_layout.getHeight());
        animation_out.setDuration(500);
        animation_out.setFillAfter(true);
        menu_drawer_layout.setAnimation(animation_out);
        menu_drawer_layout.setVisibility(View.GONE);
        menu_open = false;
    }

    private void Sign_out() {

        mAuth.signOut();
        Twitter.logOut();
        Toast.makeText(MainActivity.this,"Sign out",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();

    }

    private void sign_out_dialog(){
        menu_close();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.AppCompatAlertDialogStyle);
        builder.setMessage("Do you want to sign out?");
        builder.setTitle("Message");
        builder.setNegativeButton("CANCEL",null);
        builder.setPositiveButton("SIGN OUT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Sign_out();
            }
        });
        builder.create();
        builder.show();
    }

    private void exit_dialog(){
        menu_close();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.AppCompatAlertDialogStyle);
        builder.setMessage("Do you want to exit?");
        builder.setTitle("Message");
        builder.setNegativeButton("CANCEL",null);
        builder.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public void onBackPressed() {
        exit_dialog();
    }
}
