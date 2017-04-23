package com.plutos_seup.tweetags.Recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plutos_seup.tweetags.Data.Tags;
import com.plutos_seup.tweetags.Picasso.Picasso;
import com.plutos_seup.tweetags.R;

import java.util.ArrayList;

/**
 * Created by androidworkspace on 4/21/2017 AD.
 */

public class Main_Adapter extends RecyclerView.Adapter<Main_holder>{

    Context context;
    ArrayList<Tags> data;

    public Main_Adapter(Context context, ArrayList<com.plutos_seup.tweetags.Data.Tags> datas) {

        this.context = context;
        data = datas;

    }

    @Override
    public Main_holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card_2,parent,false);
        Main_holder holder = new Main_holder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(Main_holder holder, int position) {

        holder.tag_name.setText(data.get(position).getTag_name());
        Picasso.downloadImage(context,data.get(position).getCover_url(),holder.cover);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
