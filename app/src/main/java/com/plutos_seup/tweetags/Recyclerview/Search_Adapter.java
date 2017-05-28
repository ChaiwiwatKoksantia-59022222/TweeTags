package com.plutos_seup.tweetags.Recyclerview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.plutos_seup.tweetags.AddActivity;
import com.plutos_seup.tweetags.Data.Tags;
import com.plutos_seup.tweetags.NearbyTags;
import com.plutos_seup.tweetags.Picasso.Picasso;
import com.plutos_seup.tweetags.R;
import com.plutos_seup.tweetags.SearchActivity;
import com.twitter.sdk.android.core.models.Card;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by androidworkspace on 5/15/2017 AD.
 */

public class Search_Adapter extends RecyclerView.Adapter<Search_holder> {

    Context context;
    ArrayList<String> data,cover,dot,key_p,date;

    String mode_s;

    boolean add_check = false;

    String we,UI,we_2;

    Dialog text_dialog;
    TextView textView_dialog;
    Button cancel_btn_s,confirm_btn_s;

    String name_u;

    final static String database_url = "https://tweetags-512a8.firebaseio.com/";
    final static String storage_url = "gs://tweetags-512a8.appspot.com";

    private FirebaseAuth mAuth;
    String UID = "";

    private int position_s,position_w,sub_po;

    BottomSheetDialog main_bs_dialog,main_mode_bs_dialog,main_delete_dialog;
    BottomSheetBehavior main_bs_behavior,main_mode_bs_behavior,main_delete_behavior;
    View main_bs_view,main_mode_bs_view,main_delete_view;

    public Search_Adapter(Context context, ArrayList<String> datas,ArrayList<String> covers,ArrayList<String> dots,
                          String mode,ArrayList<String> keys ,String ser,String we_1,ArrayList<String> dates) {

        this.context = context;
        this.data = datas;
        this.cover = covers;
        this.dot = dots;
        this.mode_s = mode;
        this.key_p = keys;
        this.date = dates;

        this.we = ser;
        this.we_2 = we_1;


    }

    @Override
    public Search_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card,parent,false);

        Search_holder holder = new Search_holder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(Search_holder holder, final int position) {

        final TextView s = holder.name;
        final CardView c = holder.card;

        final RelativeLayout ir = holder.image_layout;
        final ImageView im = holder.imageView;
        final TextView it = holder.image_name;

        String se = data.get(position);

        String s1 = String.valueOf(se.charAt(0));


        final String sr = s1;

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();


        if (sr.contains("#") != true){
            add_check = true;
            c.setVisibility(View.VISIBLE);
            ir.setVisibility(View.GONE);
            s.setText("Add " + data.get(position));
            c.setCardBackgroundColor(Color.parseColor("#EF5350"));
            s.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else {
            String text = data.get(position);
            String image = cover.get(position);
            String s_dot = dot.get(position);
            String t = CheckDot(text,s_dot);
            if (image.length()>0){
                ir.setVisibility(View.VISIBLE);
                c.setVisibility(View.GONE);
                Picasso.downloadImage(context,cover.get(position),im);
                it.setText(t);
            }
            else {
                c.setVisibility(View.VISIBLE);
                ir.setVisibility(View.GONE);
                c.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                s.setText(t);
            }
        }

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mode_s.contentEquals("1") == true || mode_s.contentEquals("2") == true){
                    function(position);
                }
                else {
                    main_bs_dialog.show();
                }

                position_w = position;
                sub_po = position_w;
            }
        });

        ir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode_s.contentEquals("1") == true || mode_s.contentEquals("2") == true){
                    function(position);
                }
                else {
                    main_bs_dialog.show();
                }

                position_w = position;
                sub_po = position_w;
            }
        });

        //BottomSheet_Main_Mode
        main_bs_view = ((SearchActivity)context).getLayoutInflater().inflate(R.layout.main_bottomsheet,null);
        main_bs_dialog = new BottomSheetDialog(context);
        main_bs_dialog.setContentView(main_bs_view);
        main_bs_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                main_bs_dialog.cancel();
            }
        });
        main_bs_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                main_bs_dialog.cancel();
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
                main_bs_dialog.cancel();
            }
        });

        CardView main_search = (CardView)main_bs_view.findViewById(R.id.main_card_hashtag_btn_search);
        main_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);

                name_u = data.get(position_w);

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
        main_delete_view = ((SearchActivity)context).getLayoutInflater().inflate(R.layout.main_mode_bottomsheet_delete,null);
        main_delete_dialog = new BottomSheetDialog(context);
        main_delete_dialog.setContentView(main_delete_view);
        main_delete_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                main_delete_dialog.cancel();
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
        main_mode_bs_view = ((SearchActivity)context).getLayoutInflater().inflate(R.layout.main_mode_bottomsheet,null);
        main_mode_bs_dialog = new BottomSheetDialog(context);
        main_mode_bs_dialog.setContentView(main_mode_bs_view);
        main_mode_bs_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                main_mode_bs_dialog.cancel();
            }
        });
        main_mode_bs_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                main_mode_bs_dialog.cancel();
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
                main_mode_bs_dialog.cancel();
            }
        });
        CardView mode_edit = (CardView)main_mode_bs_view.findViewById(R.id.card_bts_edit_edit);
        mode_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cover = get_data("cover",position_w);
                String key = get_data("key",position_w);
                String name = get_data("text",position_w);
                String date = get_data("date",position_w);
                String count = get_data("dot",position_w);

                Intent intent = new Intent(context,AddActivity.class);
                intent.putExtra("cover",cover);
                intent.putExtra("key",key);
                intent.putExtra("name",name);
                intent.putExtra("date",date);
                intent.putExtra("count",count);

                int mode = 1;
                intent.putExtra("mode","1");

                context.getApplicationContext().startActivity(intent);

                main_mode_bs_dialog.cancel();
            }
        });

        CardView nearby = (CardView)main_bs_view.findViewById(R.id.main_nearby_card);
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String count_u = get_data("dot",sub_po);
                String name_u = get_data("text",sub_po);
                String key_u = get_data("key",sub_po);

                final String n = name_u;
                final String k = key_u;
                final String c = count_u;

                Intent intent = new Intent(context,NearbyTags.class);

                intent.putExtra("name",n);
                intent.putExtra("key",k);
                intent.putExtra("count",c);



                context.getApplicationContext().startActivity(intent);
                main_bs_dialog.cancel();
            }
        });



        CardView main_con = (CardView)main_bs_view.findViewById(R.id.main_card_hashtag_btn_detail);
        main_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_bs_dialog.cancel();
                main_mode_bs_dialog.show();
            }
        });

        CardView mode_delete = (CardView)main_mode_bs_view.findViewById(R.id.card_bts_edit_delete);
        mode_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_mode_bs_dialog.cancel();
                main_delete_dialog.show();
            }
        });

        CardView delete_cancel = (CardView)main_delete_view.findViewById(R.id.card_bts_edit_delete_cancel);
        delete_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                main_delete_dialog.cancel();
                main_mode_bs_dialog.show();
            }
        });

        CardView delete_confirm = (CardView)main_delete_view.findViewById(R.id.card_bts_edit_delete_confirm);
        delete_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                main_delete_dialog.cancel();
                String user_UID = user.getUid();
                UID = user_UID;
                String key_d = get_data("key",position_w);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference firebase = databaseReference.child("User").child(UID).child("Tags").child(key_d);
                firebase.removeValue();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference reference = storage.getReferenceFromUrl(storage_url);
                StorageReference storageReference = reference.child("User").child(UID).child("Tags").child(key_d+".png");
                storageReference.delete();

            }
        });


    }

    private String get_data(String m,int p){

        String result = "";

        if (m.contentEquals("text") == true){
            result = data.get(p);
        }
        else if (m.contentEquals("cover") == true){
            result = cover.get(p);
        }
        else if (m.contentEquals("dot") == true){
            result = dot.get(p);
        }
        else if (m.contentEquals("key") == true){
            result = key_p.get(p);
        }
        else if (m.contentEquals("date") == true){
            result = key_p.get(p);
        }

        return result;
    }

    private void function(int p) {

        if (add_check == true){

            String tq = data.get(p);

            intent_jump(0,tq,"","","");
        }
        else if (mode_s.contentEquals("1") == true || mode_s.contentEquals("2") == true){
            String text = data.get(p);
            String image = cover.get(p);
            String s_dot = dot.get(p);
            String key = key_p.get(p);

            int m = -1;
            if (mode_s.contentEquals("1") == true ) {
                m = 1;
            }
            else if (mode_s.contentEquals("2") == true){
                m = 2;
            }

            intent_jump(m,text,image,key,s_dot);
        }


    }

    private void dialog(final String se, String text, String cancel_btn, String confirm_btn){
        text_dialog = new Dialog(context);
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
                Intent intent = new Intent(Intent.ACTION_VIEW);

                String we = Character.toString(se.charAt(0));
                Boolean sw = we.contains("#");
                if (sw == true) {
                    String url_d = se.substring(1);
                    String url = "https://twitter.com/search?f=images&vertical=default&q=%23"+url_d;
                    intent.setData(Uri.parse(url));
                    context.startActivity(Intent.createChooser(intent,"Open with"));
                }
                text_dialog.cancel();

            }
        });

    }



    private void intent_jump(int mode_w,String tq,String cover,String key_q,String count){
        Intent intent = new Intent(context, AddActivity.class);

        String mod = "";


        if (mode_w == 0){
            intent.putExtra("text",tq);
            mod = "3";
        }
        else if (mode_w == 1 || mode_w == 2){
            if (mode_w == 1) {
                mod = "1";
            }
            else {
                mod = "4";
                intent.putExtra("sub",we_2);
            }
            intent.putExtra("key",key_q);
            intent.putExtra("name",tq);
            intent.putExtra("cover",cover);
            intent.putExtra("count",count);
        }
        intent.putExtra("mode",mod);
        context.startActivity(intent);
        ((Activity)context).finish();

    }

    private String CheckDot(String n1,String n2){
        String text = "";
        int s = Integer.parseInt(n2);

        if (n2.length()>0 && s > 0){
            text = n1 + " [ "+n2+" ]";
        }
        else {
            text = n1;
        }

        return text;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
