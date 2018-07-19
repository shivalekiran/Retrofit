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
public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.DistrictViewHolder> implements Filterable {


    private static final String TAG = DistrictAdapter.class.getSimpleName();
    private final Context mContext;
    public ArrayList<BrandVendorModel> districtList;
    private final ArrayList<BrandVendorModel> selectedList;
    private final ArrayList<BrandVendorModel> duplicateList;
    private DistrictFilter districtFilter;
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;
    private boolean showfooter;



    public DistrictAdapter(Context context, ArrayList<BrandVendorModel> mDistrictList, ArrayList<BrandVendorModel> arrayList)
    {
        this.mContext = context;
        this.districtList = mDistrictList;
        this.selectedList = arrayList;
        this.duplicateList = mDistrictList;
        getFilter();
    }


    @Override
    public DistrictViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View filterList = LayoutInflater.from(mContext).inflate(R.layout.ed_filter_list_item, null);
        return new DistrictViewHolder(filterList);
    }

    @Override
    public void onBindViewHolder(DistrictViewHolder holder, int position)
    {
        try
        {
            holder.list.get(0).setText(districtList.get(position).getDistrictED());
            holder.view.setVisibility(position < districtList.size() - 1 ? View.VISIBLE : View.INVISIBLE);
            holder.list.get(1).setVisibility(View.INVISIBLE);
            holder.itemCheckBox.setChecked(districtList.get(position).isSelected());
        }
        catch(Exception e)
        {
            Log.e(TAG, "onBindViewHolder: " );
        }
    }

    public void setfooter(boolean showfooter) {
        this.showfooter = showfooter;
    }

    public boolean getFooter() {
        return !this.showfooter;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == districtList.size()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }


    @Override
    public int getItemCount()
    {
        if (districtList.size() > 0 || districtList != null)
        {
            return districtList.size();
        } else {
            return 0;
        }

    }

    @Override
    public Filter getFilter() {
        if (districtFilter == null) {
            districtFilter = new DistrictFilter();
        }
        return districtFilter;
    }

    public class DistrictViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.itemCheckBox)
        CheckBox itemCheckBox;
        @BindViews({R.id.txtdeptname, R.id.txt_code_name})
        List<TextView> list;

        @BindView(R.id.item_decorator)
        View view;
        public DistrictViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @SuppressWarnings("unchecked")
    private class DistrictFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0)
            {
                ArrayList<BrandVendorModel> filterList = new ArrayList<>();
                for (int i = 0; i < duplicateList.size(); i++)
                {
                    if (duplicateList.get(i).getDistrictED().toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            return results;        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            districtList = (ArrayList<BrandVendorModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
