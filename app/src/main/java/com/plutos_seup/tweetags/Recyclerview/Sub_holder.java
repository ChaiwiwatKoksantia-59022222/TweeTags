package com.plutos_seup.tweetags.Recyclerview;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.plutos_seup.tweetags.R;
import com.twitter.sdk.android.core.models.Card;

/**
 * Created by androidworkspace on 5/21/2017 AD.
 */

public class Sub_holder extends RecyclerView.ViewHolder {

    TextView text_sub_hashtag;
    CardView card_sub_hashtag;

    public Sub_holder(View itemView) {
        super(itemView);

        text_sub_hashtag = (TextView) itemView.findViewById(R.id.sub_card_text);
        card_sub_hashtag = (CardView) itemView.findViewById(R.id.sub_card_layout);

    }

}
