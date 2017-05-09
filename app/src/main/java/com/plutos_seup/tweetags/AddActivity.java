package com.plutos_seup.tweetags;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.plutos_seup.tweetags.Firebase.Firebase_Client_Add;
import com.plutos_seup.tweetags.Picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    final static String database_url = "https://tweetags-512a8.firebaseio.com/";
    String database_url_user;

    private LinearLayout save_btn,gallery_btn,url_btn,layout_btn;
    private EditText editText_tag_name;

    final private int Request_code_permission = 12345;

    private ImageView imageView_bg;
    private CardView clear_btn;

    private String tag_names = "";

    private Button cancel,confirm,clip;
    private ClipboardManager clipboardManager;

    String date_demo;

    String url_image = "";
    String UID = "";

    String key = "";

    String date_real;
    String date_fake;

    Dialog input_dialog;

    boolean image_check = false;
    boolean dialog_check = false;

    public ProgressDialog progressDialog;

    private static final int GALLERY_CODE = 2;

    Firebase_Client_Add firebase_client_add;

    private TextView toolbar_tv;

    private int mode;
    private String tag_name_s,cover_s,date_s,key_s;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://tweetags-512a8.appspot.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Bundle bundle = getIntent().getExtras();
        mode = bundle.getInt("mode");

        firebase_client_add = new Firebase_Client_Add();

        LinearLayout add_back_btn = (LinearLayout)findViewById(R.id.Add_back_btn);
        add_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_dialog();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        toolbar_tv = (TextView)findViewById(R.id.tv_toolbar_add);


        save_btn = (LinearLayout)findViewById(R.id.add_hashtag_upload);
        gallery_btn = (LinearLayout)findViewById(R.id.add_hashtag_gallrey_btn);
        url_btn = (LinearLayout)findViewById(R.id.add_hashtag_url_btn);
        editText_tag_name = (EditText)findViewById(R.id.add_hashtag_edittext_name);

        layout_btn = (LinearLayout)findViewById(R.id.add_hashtag_layout_btn);
        imageView_bg = (ImageView)findViewById(R.id.image_add_hashtag_card);
        clear_btn = (CardView)findViewById(R.id.add_hashtag_clear_btn);

        if (user != null){

            String user_UID = user.getUid();
            UID = user_UID;

            database_url_user = database_url+"User/"+user_UID+"/";

            Firebase firebase = new Firebase(database_url_user);
            final Firebase firebase1 = firebase.child("Tags");
            String ar = firebase1.push().getKey();

            date_fake = date_cal();
            date_real = real_date();




            onImageViewClick();

            url_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Input_dialog();
                }
            });

            save_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String[] tag_fuc = check_hashtag(editText_tag_name.getText().toString());
                    String tag_name = tag_fuc[0];
                    String tag_point = tag_fuc[1];
                    if (tag_point.contentEquals("1") == true){

                        tag_names = tag_name;
                        if (image_check == true){
                            onUploadButtonClick();
                        }
                        else {
                            UP("");
                        }
                    }
                    else {
                        Toast("Please insert hashtag");
                    }

                }
            });

            clear_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clear_btn.setVisibility(View.GONE);
                    imageView_bg.setVisibility(View.INVISIBLE);
                    layout_btn.setVisibility(View.VISIBLE);
                    image_check = false;
                    url_image = "";
                }
            });

            if (mode == 1){
                toolbar_tv.setText("EDIT TAG");
                tag_name_s = bundle.getString("name");
                editText_tag_name.setText(tag_name_s);
                cover_s = bundle.getString("cover");
                key = bundle.getString("key");

                if (cover_s.length()>0){
                    Picasso.downloadImage(AddActivity.this,cover_s,imageView_bg);
                    image_check = true;
                    image_open();
                }
                else {
                    image_check =false;
                    image_clear();
                }
            }
            else if (mode == 3){
                toolbar_tv.setText("ADD TAG");
                key = date_fake + ar;
                String rte = bundle.getString("text");
                Intent intent_s = getIntent();
                String action = intent_s.getAction();
                String type = intent_s.getType();
                if ("text/plain".equals(type)) {
                    String text_po = intent_s.getStringExtra(Intent.EXTRA_TEXT);
                    editText_tag_name.setText(rte);
                }
            }
            else {
                toolbar_tv.setText("ADD TAG");
                key = date_fake + ar;
            }


        }


    }

    private void Input_dialog(){

        input_dialog = new Dialog(AddActivity.this);
        input_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        input_dialog.setContentView(R.layout.add_url_input_dialog);
        input_dialog.setCancelable(true);
        input_dialog.show();

        dialog_check = true;

        cancel = (Button)input_dialog.findViewById(R.id.add_input_cancel);
        confirm = (Button)input_dialog.findViewById(R.id.add_input_confirm);
        clip = (Button)input_dialog.findViewById(R.id.add_input_paste_clip);
        final EditText editText = (EditText)input_dialog.findViewById(R.id.add_input_edittext);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_dialog.cancel();
                dialog_check = false;
            }
        });

        clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        clip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "";
                if (clipboardManager.hasPrimaryClip()){
                    ClipData clipData = clipboardManager.getPrimaryClip();
                    ClipData.Item item = clipData.getItemAt(0);
                    text = item.getText().toString();
                    editText.setText(text);
                }

            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String r = editText.getText().toString();
                Picasso.downloadImage(AddActivity.this,r,imageView_bg);
                image_open();
                input_dialog.cancel();
                dialog_check = false;
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog_check == true){
            input_dialog.cancel();
        }
    }

    protected void onImageViewClick(){

        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),GALLERY_CODE);
            }
        });

    }

    private void image_open(){
        imageView_bg.setVisibility(View.VISIBLE);
        layout_btn.setVisibility(View.INVISIBLE);
        clear_btn.setVisibility(View.VISIBLE);
        image_check = true;
    }

    private void image_clear(){
        clear_btn.setVisibility(View.GONE);
        imageView_bg.setVisibility(View.INVISIBLE);
        layout_btn.setVisibility(View.VISIBLE);
        image_check = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {

            Uri uri = data.getData();

            if ( null != uri){
                imageView_bg.setImageURI(uri);
                image_open();
            }


        }
        else{
            image_clear();
        }
    }

    protected void onUploadButtonClick(){
        if (image_check == true){
            progressDialog = new ProgressDialog(this,R.style.progress);
            progressDialog.setMessage("Uploading ... ");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.setProgressNumberFormat(null);
            //progressDialog.setIndeterminate(true);


            StorageReference myfile = storageReference.child("User").child(UID).child("Tags").child(key+".png");
            imageView_bg.setDrawingCacheEnabled(true);
            imageView_bg.buildDrawingCache();
            Bitmap bitmap = imageView_bg.getDrawingCache();
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,arrayOutputStream);
            final byte[] data = arrayOutputStream.toByteArray();

            final UploadTask uploadTask = myfile.putBytes(data);

            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    uploadTask.cancel();
                }
            });
            progressDialog.show();

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddActivity.this,"TASK FAILED",Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddActivity.this,"TASK SUCCEEDED",Toast.LENGTH_SHORT).show();

                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    url_image = downloadUrl.toString();
                    String gh = downloadUrl.toString();

                    UP(gh);

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") int pro = (int)((100 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount());
                    progressDialog.setProgress(pro);
                }
            });
        }
        else {
            UP(url_image);
        }
    }

    private void UP(String df){
        firebase_client_add.saveOnline(tag_names,df,key,UID,date_real,date_demo);
        Toast.makeText(AddActivity.this,"Upload Successful",Toast.LENGTH_SHORT).show();
        finish();
    }




    private String[] check_hashtag(String name){
        String[] w = new String[2];
        String tag = "";
        String e = "";
        if (name.length()>0){
            String s = Character.toString(name.charAt(0));
            Boolean check = s.contains("#");
            e = "1";
            if (check == true){
                tag = name;
            }
            else {
                tag = "#"+name;
            }
        }
        else {
            e = "0";
        }
        w[0] = tag;
        w[1] = e;
        return w;
    }

    private void Toast(String text){

        Toast.makeText(AddActivity.this,text,Toast.LENGTH_SHORT).show();

    }

    private String real_date() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String format_date = dateFormat.format(calendar.getTime());

        return format_date;
    }

    private String date_cal (){

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat a1 = new SimpleDateFormat("yyyy");
        String a1s = a1.format(calendar.getTime());
        int a1i = Integer.parseInt(a1s);
        SimpleDateFormat a2 = new SimpleDateFormat("MM");
        String a2s = a2.format(calendar.getTime());
        int a2i = Integer.parseInt(a2s);
        SimpleDateFormat a3 = new SimpleDateFormat("dd");
        String a3s = a3.format(calendar.getTime());
        int a3i = Integer.parseInt(a3s);

        SimpleDateFormat b1 = new SimpleDateFormat("HH");
        String b1s = b1.format(calendar.getTime());
        int b1i = Integer.parseInt(b1s);
        SimpleDateFormat b2 = new SimpleDateFormat("mm");
        String b2s = b2.format(calendar.getTime());
        int b2i = Integer.parseInt(b2s);
        SimpleDateFormat b3 = new SimpleDateFormat("ss");
        String b3s = b3.format(calendar.getTime());
        int b3i = Integer.parseInt(b3s);

        int a1e = (3000 - a1i)+10;
        int a2e = (12 - a2i)+10;
        int a3e = (31 - a3i)+10;

        int b1e = (24 - (b1i + 1))+10;
        int b2e = (60 - (b2i + 1))+10;
        int b3e = (60 - (b3i + 1))+10;

        date_demo = String.valueOf(a1e) + "-" + String.valueOf(a2e) + "-" + String.valueOf(a3e) + "_00-00-00";

        String date_full = String.valueOf(a1e) + "-" + String.valueOf(a2e) + "-" + String.valueOf(a3e) +
                "_" + b1e + "-" + b2e + "-" + b3e;

        return date_full;

    }


    private void cancel_dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this,R.style.AppCompatAlertDialogStyle);
        builder.setMessage("Do you want to discard?");
        builder.setTitle("Message");
        builder.setNegativeButton("CANCEL",null);
        builder.setPositiveButton("DISCARD", new DialogInterface.OnClickListener() {
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
        cancel_dialog();
    }




}
