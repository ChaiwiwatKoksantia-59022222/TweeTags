package com.plutos_seup.tweetags.Recyclerview;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.plutos_seup.tweetags.R;

/**
 * Created by androidworkspace on 5/24/2017 AD.
 */

public class Nearby_holder extends RecyclerView.ViewHolder {

    TextView name;
    CardView card;

    public Nearby_holder(View itemView) {
        super(itemView);

        card = (CardView)itemView.findViewById(R.id.nearby_card_text);
        name = (TextView)itemView.findViewById(R.id.nearby_tag_card_text_name);

    }

}
