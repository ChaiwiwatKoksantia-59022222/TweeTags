package com.plutos_seup.tweetags.Recyclerview;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plutos_seup.tweetags.Interface.ItemOnClick;
import com.plutos_seup.tweetags.R;
import com.twitter.sdk.android.core.models.Card;

/**
 * Created by androidworkspace on 4/21/2017 AD.
 */

public class Main_holder extends RecyclerView.ViewHolder{

    ImageView cover;
    TextView tag_name_1,tag_name_2,tag_date;
    RelativeLayout r1,image_card,text_card,date_card;

    CardView click_image_card,click_text_card;

    LinearLayout r2;

    ItemOnClick itemOnClick_class;

    RelativeLayout i1,i2,t1,t2,d1,d2;

    public Main_holder(View itemView) {
        super(itemView);

        tag_date = (TextView)itemView.findViewById(R.id.tag_card_date);
        cover = (ImageView)itemView.findViewById(R.id.tag_card_image_cover);
        tag_name_1 = (TextView)itemView.findViewById(R.id.tag_card_text_name);
        tag_name_2 = (TextView)itemView.findViewById(R.id.tag_card_text_name_2);
        r1 = (RelativeLayout)itemView.findViewById(R.id.tag_card_relative_1);
        r2 = (LinearLayout) itemView.findViewById(R.id.tag_card_relative_2);
        image_card = (RelativeLayout)itemView.findViewById(R.id.image_card);
        text_card = (RelativeLayout)itemView.findViewById(R.id.text_card);
        date_card = (RelativeLayout)itemView.findViewById(R.id.date_card);

        click_image_card = (CardView)itemView.findViewById(R.id.main_card_image);
        click_text_card = (CardView)itemView.findViewById(R.id.main_card_text);

        //itemView.setOnClickListener(this);

    }


/*
    @Override
    public void onClick(View v) {
        this.itemOnClick_class.onItemClick(getLayoutPosition());
    }
*/
}
