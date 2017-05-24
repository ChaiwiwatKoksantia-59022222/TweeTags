package com.plutos_seup.tweetags.Recyclerview;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plutos_seup.tweetags.R;
import com.twitter.sdk.android.core.models.Card;

/**
 * Created by androidworkspace on 5/15/2017 AD.
 */



public class Search_holder extends RecyclerView.ViewHolder{

    TextView name,image_name;
    CardView card;

    RelativeLayout image_layout;
    ImageView imageView;


    public Search_holder(View itemView) {
        super(itemView);

        card = (CardView)itemView.findViewById(R.id.search_card_text);
        name = (TextView)itemView.findViewById(R.id.search_tag_card_text_name_2);

        image_name = (TextView)itemView.findViewById(R.id.search_tag_card_text_name);
        image_layout = (RelativeLayout)itemView.findViewById(R.id.search_image_card_layout);
        imageView = (ImageView)itemView.findViewById(R.id.search_tag_card_image_cover);

        //itemView.setOnClickListener(this);

    }

}
