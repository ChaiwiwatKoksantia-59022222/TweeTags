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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.plutos_seup.tweetags.Data.Nearby_tags;
import com.plutos_seup.tweetags.Data.Tags;
import com.plutos_seup.tweetags.Firebase.Firebase_Client_Add;
import com.plutos_seup.tweetags.Picasso.Picasso;
import com.plutos_seup.tweetags.Recyclerview.Sub_Adapter;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    private FirebaseAuth mAuth,sds;



    final static String database_url = "https://tweetags-512a8.firebaseio.com/";
    private String database_url_user,ar;

    private LinearLayout save_btn,gallery_btn,url_btn,layout_btn;
    private EditText editText_tag_name,edit_sub_tag;

    final private int Request_code_permission = 12345;

    private ImageView imageView_bg;
    private CardView clear_btn;

    private String main_key;

    private static String main_firebase;

    private String tag_names = "";

    private Button cancel,confirm,clip;
    private ClipboardManager clipboardManager,clipboardManagers;

    String date_demo;

    String df;

    String url_image = "";
    String UID = "";

    String key = "";
    int sub_count = 0;

    TextView text_size;
    CardView add_clear;

    String date_real;
    String date_fake;

    Dialog input_dialog;

    Bundle bundle;

    boolean image_check = false;
    boolean dialog_check = false;

    public ProgressDialog progressDialog;

    private static final int GALLERY_CODE = 2;

    Firebase_Client_Add firebase_client_add;

    private TextView toolbar_tv;

    private int mode = -1;
    private String tag_name_s,cover_s,date_s,key_s;

    private ArrayList<String> sub_tag;

    RecyclerView sub_tag_recyclerview;

    int re_sub = 0;

    TextView loading_text_dialog;
    Dialog loading_dialog,text_dialog,add_sub_dialog;
    TextView textView_dialog;
    Button confirm_btn_s,cancel_btn_s,add_sub_btn,sub_add_btn,sub_cancel_btn,sub_paste_btn;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://tweetags-512a8.appspot.com");

    Sub_Adapter sub_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        bundle = getIntent().getExtras();
        String er = bundle.getString("mode");
        mode = Integer.parseInt(er);

        firebase_client_add = new Firebase_Client_Add();

        LinearLayout add_back_btn = (LinearLayout)findViewById(R.id.Add_back_btn);
        add_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_dialog();
            }
        });

        text_size = (TextView)findViewById(R.id.add_main_text_size);

        sub_tag = new ArrayList<String>();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        toolbar_tv = (TextView)findViewById(R.id.tv_toolbar_add);
        add_clear = (CardView)findViewById(R.id.add_image_btn_clear);

        save_btn = (LinearLayout)findViewById(R.id.add_hashtag_upload);
        gallery_btn = (LinearLayout)findViewById(R.id.add_hashtag_gallrey_btn);
        url_btn = (LinearLayout)findViewById(R.id.add_hashtag_url_btn);
        editText_tag_name = (EditText)findViewById(R.id.add_hashtag_edittext_name);

        add_sub_btn = (Button)findViewById(R.id.add_sub_hashtag_btn);

        layout_btn = (LinearLayout)findViewById(R.id.add_hashtag_layout_btn);
        imageView_bg = (ImageView)findViewById(R.id.image_add_hashtag_card);
        clear_btn = (CardView)findViewById(R.id.add_hashtag_clear_btn);

        if (user != null){

            add_sub_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    input_sub_dialog();
                }
            });

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

            add_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image_clear();
                    url_image = "";
                }
            });

            if (mode == 1 || mode == 4){
                toolbar_tv.setText("EDIT TAG");
                tag_name_s = bundle.getString("name");
                editText_tag_name.setText(tag_name_s);
                cover_s = bundle.getString("cover");
                String ty = bundle.getString("count");
                sub_count = Integer.parseInt(ty);

                if (cover_s.length()>0){
                    Picasso.downloadImage(AddActivity.this,cover_s,imageView_bg);
                    image_check = true;
                    image_open();
                }
                else {
                    image_check =false;
                    image_clear();
                }

                re_sub = 3;
                sub_recyclerview(re_sub);

                if (mode == 4){
                    String si = bundle.getString("sub");

                    String[] t = check_hashtag(si);
                    String b = t[0];
                    String a = t[1];

                    if (a.contentEquals("1") == true) {
                        sub_tag.add(b);
                    }

                }

            }
            else if (mode == 3){
                toolbar_tv.setText("ADD TAG");
                String rte = bundle.getString("text");
                Intent intent_s = getIntent();
                String action = intent_s.getAction();
                String type = intent_s.getType();
                editText_tag_name.setText(rte);
                re_sub = 2;
                sub_recyclerview(re_sub);
            }
            else {
                toolbar_tv.setText("ADD TAG");
                re_sub = 1;
                sub_recyclerview(re_sub);

            }


        }


    }

    private void sub_recyclerview(int mode){
        sub_tag_recyclerview = (RecyclerView)findViewById(R.id.add_sub_recyclerview);
        sub_tag_recyclerview.setVisibility(View.GONE);
        sub_tag_recyclerview.setLayoutManager(new LinearLayoutManager(AddActivity.this,LinearLayoutManager.VERTICAL,false));
        sub_tag_recyclerview.setNestedScrollingEnabled(false);
        sub_tag_recyclerview.setHasFixedSize(true);
        sub_tag_recyclerview.setItemViewCacheSize(20);
        sub_tag_recyclerview.setDrawingCacheEnabled(true);
        sub_tag_recyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        if (mode == 1 || mode == 2){
            sub_adapter = new Sub_Adapter(AddActivity.this,sub_tag);
            sub_tag_recyclerview.setAdapter(sub_adapter);
            sub_adapter.notifyDataSetChanged();
        }
        else if (mode == 3) {

            main_key = bundle.getString("key");

            DatabaseReference databaseReference_s = FirebaseDatabase.getInstance().getReference();

            final FirebaseUser user_sub = mAuth.getCurrentUser();
            String user_UID = user_sub.getUid();

            String sub_key = main_key;

            if (sub_count>0){
                for (int l = 0 ;l < sub_count; l++){
                    String position = "Tag_"+String.valueOf(l).toString();

                    final int uy = l;

                    DatabaseReference firebase_sub = databaseReference_s.child("User").child(user_UID)
                            .child("Tags").child(sub_key).child("Nearby_Tags").child(position).child("name");
                    firebase_sub.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String s = dataSnapshot.getValue().toString();
                            sub_tag.add(s);

                            if (uy == (sub_count - 1)){
                                sub_adapter = new Sub_Adapter(AddActivity.this,sub_tag);
                                sub_tag_recyclerview.setAdapter(sub_adapter);
                                sub_adapter.notifyDataSetChanged();
                                Check_list();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }
            else {
                sub_adapter = new Sub_Adapter(AddActivity.this,sub_tag);
                sub_tag_recyclerview.setAdapter(sub_adapter);
                sub_adapter.notifyDataSetChanged();
                Check_list();
            }

        }
    }

    private void input_sub_dialog(){
        add_sub_dialog = new Dialog(AddActivity.this);
        add_sub_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        add_sub_dialog.setContentView(R.layout.add_sub_hashtag_dialog);
        add_sub_dialog.setCancelable(true);
        add_sub_dialog.show();

        clipboardManagers = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        edit_sub_tag = (EditText)add_sub_dialog.findViewById(R.id.add_sub_input_edittext);

        sub_paste_btn = (Button)add_sub_dialog.findViewById(R.id.add_sub_input_paste_clip);
        sub_paste_btn.setVisibility(View.GONE);
        sub_paste_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "";
                if (clipboardManagers.hasPrimaryClip()){
                    ClipData clipData = clipboardManagers.getPrimaryClip();
                    ClipData.Item item = clipData.getItemAt(0);
                    text = item.getText().toString();
                    edit_sub_tag.setText(text);
                }
            }
        });

        sub_add_btn = (Button)add_sub_dialog.findViewById(R.id.add_sub_input_confirm);
        sub_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] data_e = check_hashtag(edit_sub_tag.getText().toString());
                String tag_u = data_e[0];
                String tag_c = data_e[1];
                if (tag_c.contentEquals("1")==true){
                    sub_tag.add(tag_u);
                }
                else {
                    Toast("Please input hashtag");
                }
                add_sub_dialog.cancel();
                Check_list();
            }
        });

        sub_cancel_btn = (Button)add_sub_dialog.findViewById(R.id.add_sub_input_cancel);
        sub_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_sub_dialog.cancel();
                Check_list();
            }
        });

    }

    private void Check_list(){

        if (sub_tag.size()>0){
            sub_tag_recyclerview.setVisibility(View.VISIBLE);
        }
        else {
            sub_tag_recyclerview.setVisibility(View.GONE);
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
                if (r.length()>0){
                    Picasso.downloadImage(AddActivity.this,r,imageView_bg);
                    image_open();
                }
                else {
                    Toast("NOT URL");
                }
                input_dialog.cancel();
                dialog_check = false;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        sds = FirebaseAuth.getInstance();
        final FirebaseUser user_s = sds.getCurrentUser();

        if (user_s != null){
            String user_UID = user_s.getUid();
            UID = user_UID;

            database_url_user = database_url+"User/"+user_UID+"/";

            try {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                DatabaseReference firebase_s = databaseReference.child("User").child(user_UID).child("Tags");

                ar = firebase_s.push().getKey();

                //Firebase firebase = new Firebase(database_url_user);
                //Firebase firebase1 = firebase.child("Tags");
                //ar = firebase1.push().getKey();
                main_firebase = ar;

            }catch (Exception e){

            }

            date_fake = date_cal();
            date_real = real_date();

            mode_check();

            main_key = key;


        }


    }

    private void mode_check(){
        if (mode == 1 || mode == 4){
            key = bundle.getString("key");
        }
        else {
            key = date_fake + ar;

        }

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

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

        image_check = true;
        clear_btn.setVisibility(View.VISIBLE);

        size_image();



    }

    private void loading_dialog(){
        loading_dialog = new Dialog(AddActivity.this);
        loading_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loading_dialog.setContentView(R.layout.loading_dialog);
        loading_dialog.setCancelable(true);
        loading_dialog.show();

        loading_text_dialog = (TextView)loading_dialog.findViewById(R.id.text_loading);

        loading_text_dialog.setText("Uploading ...");

    }

    private void dialog(final String text, String cancel_btn, String confirm_btn){
        text_dialog = new Dialog(AddActivity.this);
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
                text_dialog.cancel();
                finish();
            }
        });

    }

    private void size_image(){
        Bitmap bitmap = ((BitmapDrawable)imageView_bg.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] imageInByte = stream.toByteArray();
        long lengthbmp = imageInByte.length;
        String s = String.valueOf(lengthbmp).toString();
        double leng = Double.parseDouble(s);
        double suy = leng/1024;

        double s_e = 0.0;
        String inu = "";

        if (suy>=1024){
            s_e = suy/1024;
            inu = "MB";
        }
        else {
            s_e = suy;
            inu = "KB";
        }


        df = new DecimalFormat("0.00").format(s_e);
        if (df.contentEquals("35.03")==true){
            df = "0.00";
        }
        String urt = df+" "+inu;
        text_size.setText(urt);
        //Toast("Length of the Image : "+df+" "+inu);


    }

    private void image_clear(){
        imageView_bg.setVisibility(View.INVISIBLE);
        layout_btn.setVisibility(View.VISIBLE);
        clear_btn.setVisibility(View.GONE);

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
            /*progressDialog = new ProgressDialog(this,R.style.progress);
            progressDialog.setMessage("Uploading ... ");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.setProgressNumberFormat(null);
            //progressDialog.setIndeterminate(true);

*/
            StorageReference myfile = storageReference.child("User").child(UID).child("Tags").child(key+".png");
            imageView_bg.setDrawingCacheEnabled(true);
            imageView_bg.buildDrawingCache();

            Bitmap bitmap = imageView_bg.getDrawingCache();
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,arrayOutputStream);
            final byte[] data = arrayOutputStream.toByteArray();

            final UploadTask uploadTask = myfile.putBytes(data);

            /*
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    uploadTask.cancel();
                }
            });


            progressDialog.show();
*/
            loading_dialog();

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
                    //progressDialog.setProgress(pro);
                }
            });
        }
        else {
            UP(url_image);
        }
    }

    private void UP(String df){
        firebase_client_add.saveOnline(tag_names,df,key,UID,date_real,date_demo,sub_tag);
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
        /*AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this,R.style.AppCompatAlertDialogStyle);
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
        builder.show();*/

        dialog("Do you want to discard?","CANCEL","DISCARD");

    }



    @Override
    public void onBackPressed() {
        cancel_dialog();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Firebase",ar);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ar = savedInstanceState.getString("Firebase");
    }
}
