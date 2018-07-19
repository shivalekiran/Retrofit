package com.aperotechnologies.retrofit.view.adapter;

import android.content.Context;
import android.support.v4.widget.TextViewCompat;
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
public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> implements Filterable {


    private static final String TAG = StoreAdapter.class.getSimpleName();
    private final Context mContext;
    public ArrayList<BrandVendorModel> storeList;
    private ArrayList<BrandVendorModel> selectedList;
    private ArrayList<BrandVendorModel> duplicateList;
    private StoreFilter storeFilter;

    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;
    private boolean showfooter;


    public StoreAdapter(Context context, ArrayList<BrandVendorModel> mStoreList, ArrayList<BrandVendorModel> arrayList) {
        this.mContext = context;
        this.storeList = mStoreList;
        this.selectedList = arrayList;
        this.duplicateList = mStoreList;
        getFilter();
    }


    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View filterList = LayoutInflater.from(mContext).inflate(R.layout.ed_filter_list_item, null);
        return new StoreViewHolder(filterList);
    }

    @Override
    public void onBindViewHolder(StoreViewHolder holder, int position) {
        try {
            holder.list.get(0).setText(storeList.get(position).getStoreCode());
            TextViewCompat.setAutoSizeTextTypeWithDefaults(holder.list.get(0), TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            holder.list.get(1).setText(storeList.get(position).getStoreCodeParam());
            holder.list.get(1).setVisibility(View.INVISIBLE);
            holder.itemCheckBox.setChecked(storeList.get(position).isSelected());
            holder.view.setVisibility(position < storeList.size() - 1 ? View.VISIBLE : View.INVISIBLE);
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: ", e);
        }
    }

    @Override
    public int getItemCount() {
        if (storeList.size() > 0 || storeList != null) {
            return storeList.size();
        } else {
            return 0;
        }

    }


    @Override
    public int getItemViewType(int position) {
        if (position == storeList.size()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
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
        if (storeFilter == null) {
            storeFilter = new StoreFilter();
        }
        return storeFilter;
    }

    public class StoreViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemCheckBox)
        CheckBox itemCheckBox;
        @BindViews({R.id.txtdeptname, R.id.txt_code_name})
        List<TextView> list;

        @BindView(R.id.item_decorator)
        View view;

        public StoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @SuppressWarnings("unchecked")
    private class StoreFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<BrandVendorModel> filterList = new ArrayList<>();
                for (int i = 0; i < duplicateList.size(); i++) {
                    if (duplicateList.get(i).getStoreCode().toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filterList.add(duplicateList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = duplicateList.size();
                results.values = duplicateList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            storeList = (ArrayList<BrandVendorModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
