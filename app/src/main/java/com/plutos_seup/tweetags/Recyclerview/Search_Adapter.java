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
import android.widget.TextView;

import com.plutos_seup.tweetags.AddActivity;
import com.plutos_seup.tweetags.Data.Tags;
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
    ArrayList<String> data;
    boolean check = true;

    public Search_Adapter(Context context, ArrayList<String> datas) {

        this.context = context;
        data = datas;

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
        String se = data.get(position);

        String s1 = String.valueOf(se.charAt(0));

        final String sr = s1;

        check = true;

        if (sr.contains("#")!=true){
            check = true;
            s.setText("Add " + data.get(position));
            c.setCardBackgroundColor(Color.parseColor("#9CCC65"));
            s.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else {
            check = true;
            c.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            s.setText(data.get(position));
        }

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sr.contains("#")!=true && check == true){
                    Intent intent = new Intent(context, AddActivity.class);
                    intent.putExtra("mode",3);

                    intent.putExtra("text",data.get(position));
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
                else {
                    //INSERT
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
