package com.plutos_seup.tweetags.Recyclerview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.plutos_seup.tweetags.NearbyTags;
import com.plutos_seup.tweetags.R;

import java.util.ArrayList;

/**
 * Created by androidworkspace on 5/24/2017 AD.
 */

public class Nearby_Adapter extends RecyclerView.Adapter<Nearby_holder> {

    Context context;
    ArrayList<String> data;

    Dialog text_dialog;
    TextView textView_dialog;
    Button cancel_btn_s,confirm_btn_s;

    public Nearby_Adapter(Context context, ArrayList<String> datas) {

        this.context = context;
        this.data = datas;

    }

    @Override
    public Nearby_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_card,parent,false);

        Nearby_holder holder = new Nearby_holder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(Nearby_holder holder, int position) {

        final TextView s = holder.name;
        final CardView c = holder.card;

        final String se = data.get(position);

        s.setText(se);

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog(se,"Do you want to jump?","NO","YES");
            }
        });

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

    @Override
    public int getItemCount() {
        return data.size();
    }
}
