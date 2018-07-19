package com.aperotechnologies.retrofit.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aperotechnologies.retrofit.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Created by pamrutkar on 17/05/18.
 */

@SuppressWarnings("ALL")
public class HierarchyAdapter extends RecyclerView.Adapter<HierarchyAdapter.HierarchyViewHolder> {

    // --Commented out by Inspection (02/07/18, 3:34 PM):private static final String TAG = HierarchyAdapter.class.getName();
    private final Context mContext;

    private ArrayList<String> easyDaySales;
    private ArrayList<Integer> hierarchySelectCount;
    private ArrayList<Boolean> backgroundList;
    private String from;


    public HierarchyAdapter(Context context, ArrayList<String> arr_string, ArrayList<Integer> hierarchySelectCount, ArrayList<Boolean> bglist, String mFrom) {
        mContext = context;
        this.easyDaySales = arr_string;
        this.hierarchySelectCount = hierarchySelectCount;
        this.backgroundList = bglist;
        this.from = mFrom;
    }

    @Override
    public HierarchyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View filterList = LayoutInflater.from(mContext).inflate(R.layout.filter_list_layout, parent, false);
        return new HierarchyViewHolder(filterList);
    }

    @Override
    public void onBindViewHolder(HierarchyViewHolder holder, int position) {
        try {
            if ((position == 0 || position == 6 || position == 11 || position == 14) && from.equals("Sales")) {
                holder.container.setBackgroundColor(ContextCompat.getColor(mContext, R.color.ed_card_bk));
            } else {
                holder.container.setBackgroundColor(backgroundList.get(position) ? ContextCompat.getColor(mContext, R.color.white) : ContextCompat.getColor(mContext, R.color.filter_list_bg));
            }
            holder.text_view_right_line.setVisibility(backgroundList.get(position) ? View.INVISIBLE : View.VISIBLE);
            holder.txt_name.setText(easyDaySales.get(position));
            holder.txt_name.setTypeface(backgroundList.get(position) ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
            holder.txt_name.setTextColor(backgroundList.get(position) ? ContextCompat.getColor(mContext, R.color.ezfb_black) : ContextCompat.getColor(mContext, R.color.darker_grey));
            holder.select_count.setTypeface(backgroundList.get(position) ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
            holder.select_count.setText(hierarchySelectCount.get(position) > 0 ? hierarchySelectCount.get(position) + "" : "");
            holder.select_count.setTextColor(backgroundList.get(position) ? ContextCompat.getColor(mContext, R.color.ezfb_black) : ContextCompat.getColor(mContext, R.color.darker_grey));
//            holder.container.setBackgroundColor(backgroundList.get(position) ? ContextCompat.getColor(mContext, R.color.white): ContextCompat.getColor(mContext, R.color.filter_list_bg));
//            holder.line.setVisibility(backgroundList.get(position) ? View.INVISIBLE: View.VISIBLE);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (easyDaySales != null && easyDaySales.size() > 0) {
            return easyDaySales.size();
        } else {
            return 0;
        }

    }


    public class HierarchyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.text_view_right_line)
        TextView text_view_right_line;
        @BindView(R.id.select_count)
        TextView select_count;
        @BindView(R.id.container)
        RelativeLayout container;

        public HierarchyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}


