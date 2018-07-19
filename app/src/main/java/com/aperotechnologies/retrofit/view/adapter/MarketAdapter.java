package com.aperotechnologies.retrofit.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.aperotechnologies.retrofit.R;
import com.aperotechnologies.retrofit.service.model.BrandVendorModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by pamrutkar on 22/05/18.
 */

@SuppressWarnings("ALL")
public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.MarketViewHolder> implements Filterable {


    private static final String TAG = MarketAdapter.class.getSimpleName();
    private final Context mContext;
    public ArrayList<BrandVendorModel> marketList;
    private final ArrayList<BrandVendorModel> selectedList;
    private ArrayList<BrandVendorModel> duplicateList;
    private MarketFilter marketFilter;

    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;
    private boolean showfooter;

    public MarketAdapter(Context context, ArrayList<BrandVendorModel> mMarketList, ArrayList<BrandVendorModel> arrayList)
    {
        this.mContext = context;
        this.marketList = mMarketList;
        this.selectedList = arrayList;
        this.duplicateList = mMarketList;
        getFilter();
    }


    @Override
    public MarketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View filterList = LayoutInflater.from(mContext).inflate(R.layout.ed_filter_list_item, null);
        return new MarketViewHolder(filterList);
    }

    @Override
    public void onBindViewHolder(MarketViewHolder holder, int position)
    {
        try
        {
            holder.list.get(0).setText(marketList.get(position).getMarketED());
            holder.list.get(1).setVisibility(View.INVISIBLE);
            holder.itemCheckBox.setChecked(marketList.get(position).isSelected());
            holder.view.setVisibility(position < marketList.size() - 1 ? View.VISIBLE : View.INVISIBLE);
        }
        catch(Exception e)
        {
            Log.e(TAG, "onBindViewHolder: " );
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == marketList.size()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (marketList != null) {
            return marketList.size();
        } else {
            return 0;
        }
    }

    public void setfooter(boolean showfooter) {
        this.showfooter = showfooter;
    }

    public boolean getFooter() {
        return !this.showfooter;
    }


    @Override
    public Filter getFilter() {
        if (marketFilter == null) {
            marketFilter = new MarketFilter();
        }
        return marketFilter;
    }

    public class MarketViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.itemCheckBox)
        CheckBox itemCheckBox;
        @BindViews({R.id.txtdeptname, R.id.txt_code_name})
        List<TextView> list;

        @BindView(R.id.item_decorator)
        View view;
        public MarketViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @SuppressWarnings("unchecked")
    private class MarketFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0)
            {
                ArrayList<BrandVendorModel> filterList = new ArrayList<>();
                for (int i = 0; i < duplicateList.size(); i++)
                {
                    if (duplicateList.get(i).getMarketED().toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filterList.add(duplicateList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            }
            else
            {
                results.count = duplicateList.size();
                results.values = duplicateList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            marketList = (ArrayList<BrandVendorModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
