package com.aperotechnologies.retrofit.view.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.aperotechnologies.retrofit.R;


/**
 * Created by kshivale on 24/01/18.
 * Footer view for lazy loading
 */
public class FooterViewHolder extends RecyclerView.ViewHolder {

    public final ProgressBar lazy_loader;
    public final RelativeLayout footer;

    public FooterViewHolder(View itemView) {
        super(itemView);
        footer = itemView.findViewById(R.id.footer);
        lazy_loader = itemView.findViewById(R.id.lazy_loader);
    }
}