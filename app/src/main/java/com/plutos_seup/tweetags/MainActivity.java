package com.plutos_seup.tweetags;

import android.animation.Animator;
import android.app.Dialog;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.plutos_seup.tweetags.Firebase.Firebase_Client;
import com.plutos_seup.tweetags.Recyclerview.Main_Adapter;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import com.plutos_seup.tweetags.SunBabyLoadingView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase;

    final static String database_url = "https://tweetags-512a8.firebaseio.com/";
    String database_url_user;

    protected Firebase_Client firebase_client;

    RecyclerView recyclerView;

    CardView progressBar;

    Animation animator;

    LinearLayout menu_drawer_btn;
    LinearLayout menu_drawer_layout;
    ImageView open;
    ImageView close;
    Boolean menu_open = false;
    de.hdodenhof.circleimageview.CircleImageView main_image_profile;

    TextView sign_out,search;
    RelativeLayout line_1;

    RelativeLayout main_load;

    TextView textView_dialog;
    Button confirm_btn_s,cancel_btn_s;
    Dialog text_dialog;

    TranslateAnimation animation_in,animation_out;

    //SunBabyLoadingView sunBabyLoadingView;

    private boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        open = (ImageView)findViewById(R.id.main_menu_drawer_btn_open);
        close = (ImageView)findViewById(R.id.main_menu_drawer_btn_close);

        ImageView main_add_btn = (ImageView)findViewById(R.id.main_add_btn);
        main_load = (RelativeLayout)findViewById(R.id.main_load_layout);
        main_load.setVisibility(View.VISIBLE);


        main_image_profile = (de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.main_image_profile);

        sign_out = (TextView)findViewById(R.id.main_sign_out);
        sign_out.setVisibility(View.GONE);
        sign_out.setEnabled(false);
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu_open == true) {
                    sign_out_dialog();
                }
            }
        });


        search = (TextView)findViewById(R.id.main_search_btn);
        search.setEnabled(false);
        search.setVisibility(View.GONE);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu_open == true){
                    Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                    intent.putExtra("mode","0");
                    startActivity(intent);
                    if (menu_open == true){
                        menu_close();
                    }
                }
            }
        });

        line_1 = (RelativeLayout)findViewById(R.id.line_s_1);
        line_1.setEnabled(false);
        line_1.setVisibility(View.GONE);

        menu_drawer_btn = (LinearLayout) findViewById(R.id.main_menu_drawer_btn);
        menu_drawer_layout = (LinearLayout)findViewById(R.id.main_menu_drawer_layout);

        //sunBabyLoadingView = (SunBabyLoadingView)findViewById(R.id.sun_load);
        progressBar = (CardView) findViewById(R.id.pro_main);

        if (check == false){
            //sunBabyLoadingView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

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

            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            //SnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
            //snapHelper.attachToRecyclerView(recyclerView);

            firebase_client = new Firebase_Client(MainActivity.this,database_url_user,recyclerView);

            firebase_client.refresh();


        }

        main_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user != null) {
                    int mode = 0;
                    Intent intent = new Intent(MainActivity.this,AddActivity.class);
                    intent.putExtra("mode","0");
                    startActivity(intent);

                    if(menu_open == true){
                        menu_close();
                    }
                }
            }
        });


        open.setVisibility(View.VISIBLE);
        close.setVisibility(View.GONE);
        menu_drawer_layout.setVisibility(View.GONE);
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void menu_open(){
        animation_in = new TranslateAnimation(0,0,0-274,0);

        sign_out.setEnabled(true);
        sign_out.setVisibility(View.VISIBLE);
        search.setEnabled(true);
        search.setVisibility(View.VISIBLE);
        line_1.setEnabled(true);
        line_1.setVisibility(View.VISIBLE);


        open.setVisibility(View.GONE);
        close.setVisibility(View.VISIBLE);
        animation_in.setDuration(500);
        animation_in.setFillAfter(true);
        menu_drawer_layout.setAnimation(animation_in);
        menu_drawer_layout.setVisibility(View.VISIBLE);
        menu_open = true;

    }

    private void menu_close(){
        open.setVisibility(View.VISIBLE);
        close.setVisibility(View.GONE);
        animation_out = new TranslateAnimation(0,0, 0, 0-menu_drawer_layout.getHeight());
        animation_out.setDuration(500);
        animation_out.setFillAfter(true);
        menu_drawer_layout.setAnimation(animation_out);
        menu_drawer_layout.setVisibility(View.GONE);
        menu_open = false;

        animation_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sign_out.setEnabled(false);
                sign_out.setVisibility(View.GONE);
                search.setEnabled(false);
                search.setVisibility(View.GONE);
                line_1.setEnabled(false);
                line_1.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }



    private void Sign_out() {

        mAuth.signOut();
        TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (twitterSession != null) {
            Twitter.getSessionManager().clearActiveSession();
            Twitter.logOut();
        }
        //Twitter.logOut();
        Toast.makeText(MainActivity.this,"Sign out",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        loading();
    }

    private void dialog(final int dialog_mode, String text, String cancel_btn, String confirm_btn){
        text_dialog = new Dialog(MainActivity.this);
        text_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        text_dialog.setContentView(R.layout.cancel_dialog);
        text_dialog.setCancelable(true);
        text_dialog.show();

        textView_dialog = (TextView)text_dialog.findViewById(R.id.can_textview_dialog);
        cancel_btn_s = (Button)text_dialog.findViewById(R.id.can_cancel_btn);
        confirm_btn_s = (Button)text_dialog.findViewById(R.id.can_confirm_btn);

        textView_dialog.setText(text);
        cancel_btn_s.setText(cancel_btn);
        confirm_btn_s.setText(confirm_btn);

        cancel_btn_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_dialog.cancel();
            }
        });
        confirm_btn_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog_mode == 0){
                    text_dialog.cancel();
                    finish();
                }
                else if (dialog_mode == 1){
                    text_dialog.cancel();
                    Sign_out();
                }
            }
        });

    }

    private void loading() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootDbRef = firebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            String user_UID = user.getUid();
            final DatabaseReference globalRoomDbRef = rootDbRef.child("User").child(user_UID);
            globalRoomDbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    animator = new AlphaAnimation(1,0);
                    animator.setDuration(500);
                    animator.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            main_load.setVisibility(View.GONE);

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    main_load.startAnimation(animator);

                    //sunBabyLoadingView.setVisibility(View.GONE);
                    check = true;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast("Failed: " + databaseError.getMessage());
                }
            });



        }
    }

    private void sign_out_dialog(){
        if (menu_open == true){
            menu_close();
        }
        /*
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
        */
        dialog(1,"Do you want to sign out?","CANCEL","SIGN OUT");


    }

    private void exit_dialog(){
/*
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

  */
        dialog(0,"Do you want to exit?","CANCEL","EXIT");

    }

    @Override
    public void onBackPressed() {
        if (menu_open == true){
            menu_close();
        }
        else {
            exit_dialog();
        }
    }

    private void Toast(String text){

        Toast.makeText(MainActivity.this,text,Toast.LENGTH_SHORT).show();

    }



}
