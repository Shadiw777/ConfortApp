package com.confortapp.leon.confortapp.ExpandableList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.confortapp.leon.confortapp.R;

/**
 * Created by Leon on 19.03.2018.
 */

public class InfoViewHolder extends RecyclerView.ViewHolder {

    TextView username;

    public InfoViewHolder(View itemView) {
        super(itemView);

        username = itemView.findViewById(R.id.textview_name);
    }
}
