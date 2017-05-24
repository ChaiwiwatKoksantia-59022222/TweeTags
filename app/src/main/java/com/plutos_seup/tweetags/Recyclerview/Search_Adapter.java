package com.plutos_seup.tweetags.Recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plutos_seup.tweetags.AddActivity;
import com.plutos_seup.tweetags.Data.Tags;
import com.plutos_seup.tweetags.Picasso.Picasso;
import com.plutos_seup.tweetags.R;
import com.twitter.sdk.android.core.models.Card;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by androidworkspace on 5/15/2017 AD.
 */

public class Search_Adapter extends RecyclerView.Adapter<Search_holder> {

    final static String database_url = "https://tweetags-512a8.firebaseio.com/";

    Context context;
    ArrayList<String> data,cover,dot,key_p;

    String mode_s;

    boolean add_check = false;

    String we,UI,we_2;

    public Search_Adapter(Context context, ArrayList<String> datas,ArrayList<String> covers,ArrayList<String> dots,
                          String mode,ArrayList<String> keys ,String ser,String we_1) {

        this.context = context;
        this.data = datas;
        this.cover = covers;
        this.dot = dots;
        this.mode_s = mode;
        this.key_p = keys;

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

        Log.e("RT",we);


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
                if (add_check == true){

                    String tq = data.get(position);

                    intent_jump(0,tq,"","","");
                }
                else if (mode_s.contentEquals("1") == true || mode_s.contentEquals("2") == true){
                    String text = data.get(position);
                    String image = cover.get(position);
                    String s_dot = dot.get(position);
                    String key = key_p.get(position);

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
                Log.e("RTY",we_2);
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
