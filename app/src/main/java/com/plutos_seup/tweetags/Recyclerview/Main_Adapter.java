package com.plutos_seup.tweetags.Recyclerview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.plutos_seup.tweetags.AddActivity;
import com.plutos_seup.tweetags.Data.Tags;
import com.plutos_seup.tweetags.Interface.ItemOnClick;
import com.plutos_seup.tweetags.MainActivity;
import com.plutos_seup.tweetags.Picasso.Picasso;
import com.plutos_seup.tweetags.R;
import com.twitter.sdk.android.core.models.Card;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by androidworkspace on 4/21/2017 AD.
 */

public class Main_Adapter extends RecyclerView.Adapter<Main_holder>{

    final static String database_url = "https://tweetags-512a8.firebaseio.com/";
    final static String storage_url = "gs://tweetags-512a8.appspot.com";

    private FirebaseAuth mAuth;
    String UID = "";

    Context context;
    ArrayList<Tags> data;

    String name_u;

    BottomSheetDialog main_bs_dialog,main_mode_bs_dialog,main_delete_dialog;
    BottomSheetBehavior main_bs_behavior,main_mode_bs_behavior,main_delete_behavior;
    View main_bs_view,main_mode_bs_view,main_delete_view;

    private int position_s,position_w;

    public Main_Adapter(Context context, ArrayList<com.plutos_seup.tweetags.Data.Tags> datas) {

        this.context = context;
        data = datas;

    }

    @Override
    public Main_holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card_3,parent,false);

        Main_holder holder = new Main_holder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(Main_holder holder, final int position) {

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        final String image = data.get(position).getCover_url();
        String date_check = data.get(position).getTag_key();
        String date_s = data.get(position).getTag_date();

        String year = date_s.substring(0,4);
        String day = date_s.substring(8,10);
        String month = date_s.substring(5,7);
        final String chr_month = date_month_check(month);

        String time = day+" "+chr_month+" "+year;

        String date_a = data.get(position).getTag_name();
        if (date_a.length()<=0) {
            if ((position+1) == data.size()){
                delete(data.size()-1);
            }
            else {
                String date_b = data.get(position+1).getTag_name();
                if (date_b.length()<=0){
                    delete(position);
                }
            }
        }


        int max = data.size();



        if (image.length()>0){
            holder.image_card.setVisibility(View.VISIBLE);
            holder.text_card.setVisibility(View.GONE);
            holder.date_card.setVisibility(View.GONE);
            holder.r1.setVisibility(View.VISIBLE);
            holder.r2.setVisibility(View.GONE);
            holder.tag_name_1.setText(data.get(position).getTag_name());
            holder.cover.setVisibility(View.VISIBLE);
            holder.tag_name_1.setTextColor(Color.parseColor("#dcffffff"));
            Picasso.downloadImage(context,data.get(position).getCover_url(),holder.cover);
            holder.image_card.setVisibility(View.VISIBLE);
            holder.text_card.setVisibility(View.GONE);

        }
        else if (date_check.length()<20){
            holder.r1.setVisibility(View.GONE);
            holder.r2.setVisibility(View.GONE);
            holder.image_card.setVisibility(View.GONE);
            holder.text_card.setVisibility(View.GONE);
            holder.date_card.setVisibility(View.VISIBLE);
            holder.tag_date.setText(time);

        }
        else {
            holder.image_card.setVisibility(View.GONE);
            holder.text_card.setVisibility(View.VISIBLE);
            holder.date_card.setVisibility(View.GONE);
            holder.r2.setVisibility(View.VISIBLE);
            holder.r1.setVisibility(View.GONE);
            holder.tag_name_2.setText(data.get(position).getTag_name());
            holder.tag_name_2.setTextColor(Color.parseColor("#616161"));
            holder.cover.setVisibility(View.GONE);
            holder.image_card.setVisibility(View.GONE);
            holder.text_card.setVisibility(View.VISIBLE);
        }

        holder.click_image_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_s();
                position_w = position;
            }
        });

        holder.click_text_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_s();
                position_w = position;
            }
        });

        holder.click_image_card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                click_l();
                position_w = position;
                return false;
            }
        });

        holder.click_text_card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                click_l();
                position_w = position;
                return false;
            }
        });

        //BottomSheet_Main_Mode
        main_bs_view = ((MainActivity)context).getLayoutInflater().inflate(R.layout.main_bottomsheet,null);
        main_bs_dialog = new BottomSheetDialog(context);
        main_bs_dialog.setContentView(main_bs_view);
        main_bs_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                main_bs_dialog.hide();
            }
        });
        main_bs_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                main_bs_dialog.show();
            }
        });

        main_bs_behavior = BottomSheetBehavior.from((View) main_bs_view.getParent());
        main_bs_behavior.setPeekHeight(2000);
        main_bs_behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING){
                    main_bs_behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                main_bs_dialog.show();
            }
        });

        CardView main_drop = (CardView)main_bs_view.findViewById(R.id.main_card_hashtag_btn_dropdown);
        main_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_bs_dialog.hide();
            }
        });

        CardView main_search = (CardView)main_bs_view.findViewById(R.id.main_card_hashtag_btn_search);
        main_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);

                name_u = data.get(position_w).getTag_name().toString();

                String we = Character.toString(name_u.charAt(0));
                Boolean sw = we.contains("#");
                if (sw == true) {
                    String url_d = name_u.substring(1);
                    String url = "https://twitter.com/search?f=images&vertical=default&q=%23"+url_d;
                    intent.setData(Uri.parse(url));
                    context.startActivity(Intent.createChooser(intent,"Open with"));
                    main_bs_dialog.cancel();
                }
            }
        });

        //BottomSheet_DELETE_Mode
        main_delete_view = ((MainActivity)context).getLayoutInflater().inflate(R.layout.main_mode_bottomsheet_delete,null);
        main_delete_dialog = new BottomSheetDialog(context);
        main_delete_dialog.setContentView(main_delete_view);
        main_delete_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                main_delete_dialog.hide();
            }
        });
        main_delete_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                main_delete_dialog.show();
            }
        });

        main_delete_behavior = BottomSheetBehavior.from((View) main_delete_view.getParent());
        main_delete_behavior.setPeekHeight(2000);
        main_delete_behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING){
                    main_bs_behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                main_bs_dialog.show();
            }
        });



        //BottomSheet_Edit_Mode
        main_mode_bs_view = ((MainActivity)context).getLayoutInflater().inflate(R.layout.main_mode_bottomsheet,null);
        main_mode_bs_dialog = new BottomSheetDialog(context);
        main_mode_bs_dialog.setContentView(main_mode_bs_view);
        main_mode_bs_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                main_mode_bs_dialog.hide();
            }
        });
        main_mode_bs_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                main_mode_bs_dialog.show();
            }
        });

        main_mode_bs_behavior = BottomSheetBehavior.from((View) main_mode_bs_view.getParent());
        main_mode_bs_behavior.setPeekHeight(2000);
        main_mode_bs_behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING){
                    main_mode_bs_behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                main_mode_bs_dialog.show();
            }
        });

        CardView mode_drop = (CardView)main_mode_bs_view.findViewById(R.id.card_bts_edit_cancel);
        mode_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_mode_bs_dialog.hide();
            }
        });
        CardView mode_edit = (CardView)main_mode_bs_view.findViewById(R.id.card_bts_edit_edit);
        mode_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cover = data.get(position_w).getCover_url().toString();
                String key = data.get(position_w).getTag_key().toString();
                String name = data.get(position_w).getTag_name().toString();
                String date = data.get(position_w).getTag_date().toString();

                Intent intent = new Intent(context,AddActivity.class);
                intent.putExtra("cover",cover);
                intent.putExtra("key",key);
                intent.putExtra("name",name);
                intent.putExtra("date",date);

                int mode = 1;
                intent.putExtra("mode",mode);

                context.getApplicationContext().startActivity(intent);

                main_mode_bs_dialog.hide();
            }
        });

        CardView main_con = (CardView)main_bs_view.findViewById(R.id.main_card_hashtag_btn_detail);
        main_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_bs_dialog.hide();
                main_mode_bs_dialog.show();
            }
        });

        CardView mode_delete = (CardView)main_mode_bs_view.findViewById(R.id.card_bts_edit_delete);
        mode_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_mode_bs_dialog.hide();
                main_delete_dialog.show();
            }
        });

        CardView delete_cancel = (CardView)main_delete_view.findViewById(R.id.card_bts_edit_delete_cancel);
        delete_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                main_delete_dialog.hide();
                main_mode_bs_dialog.show();
            }
        });

        CardView delete_confirm = (CardView)main_delete_view.findViewById(R.id.card_bts_edit_delete_confirm);
        delete_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                main_delete_dialog.hide();
                Firebase firebase = new Firebase(database_url);
                String user_UID = user.getUid();
                UID = user_UID;
                String key_d = data.get(position_w).getTag_key().toString();
                firebase.child("User").child(UID).child("Tags").child(key_d).removeValue();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference reference = storage.getReferenceFromUrl(storage_url);
                StorageReference storageReference = reference.child("User").child(UID).child("Tags").child(key_d+".png");
                storageReference.delete();

            }
        });

    }

    private void delete(int n){
        final FirebaseUser user = mAuth.getCurrentUser();
        Firebase firebase = new Firebase(database_url);
        String user_UID = user.getUid();
        UID = user_UID;
        String key_d = data.get(n).getTag_key().toString();
        firebase.child("User").child(UID).child("Tags").child(key_d).removeValue();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReferenceFromUrl(storage_url);
        StorageReference storageReference = reference.child("User").child(UID).child("Tags").child(key_d+".png");
        storageReference.delete();
    }

    private void click_s(){
        main_bs_dialog.show();
    }

    private void click_l(){
        main_mode_bs_dialog.show();
    }

    private String date_month_check(String month){
        String name = "";
        if (month.contentEquals("01")== true){
            name = "January";
        }
        else if (month.contentEquals("02")== true){
            name = "February";
        }
        else if (month.contentEquals("03")== true){
            name = "March";
        }
        else if (month.contentEquals("04")== true){
            name = "April";
        }
        else if (month.contentEquals("05")== true){
            name = "May";
        }
        else if (month.contentEquals("06")== true){
            name = "June";
        }
        else if (month.contentEquals("07")== true){
            name = "July";
        }
        else if (month.contentEquals("08")== true){
            name = "August";
        }
        else if (month.contentEquals("09")== true){
            name = "September";
        }
        else if (month.contentEquals("10")== true){
            name = "October";
        }
        else if (month.contentEquals("11")== true){
            name = "November";
        }
        else {
            name = "December";
        }
        return name;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
