package com.plutos_seup.tweetags.Recyclerview;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.plutos_seup.tweetags.MainActivity;
import com.plutos_seup.tweetags.R;

import java.util.ArrayList;

/**
 * Created by androidworkspace on 5/21/2017 AD.
 */

public class Sub_Adapter extends RecyclerView.Adapter<Sub_holder> {

    Context context;
    ArrayList<String> data;

    Dialog dialog;
    int size;
    TextView textView_s;
    Button cancel,confirm;

    public Sub_Adapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;

    }

    @Override
    public Sub_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_card,parent,false);
        Sub_holder holder = new Sub_holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Sub_holder holder, final int position) {
        final TextView textView = holder.text_sub_hashtag;
        final CardView cardView = holder.card_sub_hashtag;
        final String tag_name = data.get(position);
        textView.setText(tag_name);

        size = data.size();

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.cancel_dialog);
                dialog.setCancelable(true);
                dialog.show();

                textView_s = (TextView)dialog.findViewById(R.id.can_textview_dialog);
                cancel = (Button)dialog.findViewById(R.id.can_cancel_btn);
                confirm = (Button)dialog.findViewById(R.id.can_confirm_btn);

                textView_s.setText("Do you want to delete "+tag_name+" ?");
                cancel.setText("Cancel");
                confirm.setText("Delete");

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remove(position);
                        dialog.cancel();
                    }
                });

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void remove(int position){
        data.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, data.size());
    }

}
