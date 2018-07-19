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
public class ZoneAdapter extends RecyclerView.Adapter<ZoneAdapter.ZoneViewHolder> implements Filterable {


    private static final String TAG = SubDeptAdapter.class.getSimpleName();
    private final Context mContext;
    public ArrayList<BrandVendorModel> zoneList;
    private ArrayList<BrandVendorModel> selectedList;
    private ArrayList<BrandVendorModel> duplicateList;
    private ZoneFilter zoneFilter;


    public ZoneAdapter(Context context, ArrayList<BrandVendorModel> mZoneList, ArrayList<BrandVendorModel> arrayList) {
        this.mContext = context;
        this.zoneList = mZoneList;
        this.selectedList = arrayList;
        this.duplicateList = mZoneList;
        getFilter();
    }


    @Override
    public ZoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View filterList = LayoutInflater.from(mContext).inflate(R.layout.ed_filter_list_item, null);
        return new ZoneViewHolder(filterList);
    }

    @Override
    public void onBindViewHolder(ZoneViewHolder holder, int position) {
        try {
            holder.list.get(0).setText(zoneList.get(position).getZoneED());
            holder.list.get(1).setVisibility(View.INVISIBLE);
            holder.itemCheckBox.setChecked(zoneList.get(position).isSelected());
            holder.view.setVisibility(position < zoneList.size() - 1 ? View.VISIBLE : View.INVISIBLE);
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: ");
        }
    }

    @Override
    public int getItemCount() {
        if (zoneList.size() > 0 || zoneList != null) {
            return zoneList.size();
        } else {
            return 0;
        }

    }

    @Override
    public Filter getFilter() {
        if (zoneFilter == null) {
            zoneFilter = new ZoneFilter();
        }
        return zoneFilter;
    }

    public class ZoneViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemCheckBox)
        CheckBox itemCheckBox;
        @BindViews({R.id.txtdeptname, R.id.txt_code_name})
        List<TextView> list;
        @BindView(R.id.item_decorator)
        View view;


        public ZoneViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @SuppressWarnings("unchecked")
    private class ZoneFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<BrandVendorModel> filterList = new ArrayList<>();
                for (int i = 0; i < duplicateList.size(); i++) {
                    if (duplicateList.get(i).getZoneED().toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            zoneList = (ArrayList<BrandVendorModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
