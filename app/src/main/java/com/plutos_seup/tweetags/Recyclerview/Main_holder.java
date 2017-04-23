package com.plutos_seup.tweetags.Recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.plutos_seup.tweetags.R;

/**
 * Created by androidworkspace on 4/21/2017 AD.
 */

public class Main_holder extends RecyclerView.ViewHolder {

    ImageView cover;
    TextView tag_name;

    public Main_holder(View itemView) {
        super(itemView);

        cover = (ImageView)itemView.findViewById(R.id.tag_card_image_cover);
        tag_name = (TextView)itemView.findViewById(R.id.tag_card_text_name);


    }
}
