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
import com.aperotechnologies.retrofit.view.ui.FooterViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by pamrutkar on 22/05/18.
 */

@SuppressWarnings("ALL")
public class BrandTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {


    private static final String TAG = BrandTypeAdapter.class.getSimpleName();
    private Context mContext;
    private boolean showfooter;
    public ArrayList<BrandVendorModel> brandTypeList;
    private ArrayList<BrandVendorModel> duplicateList;
    private ArrayList<BrandVendorModel> selectedList;
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;
    private BrandTypeFilter brandTypeFilter;


    public BrandTypeAdapter(Context context, ArrayList<BrandVendorModel> mBrandTypeList, ArrayList<BrandVendorModel> arrayList)
    {
        this.mContext = context;
        this.brandTypeList = mBrandTypeList;
        this.selectedList = arrayList;
        this.duplicateList = mBrandTypeList;
        getFilter();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == TYPE_ITEM) {
            View filterList = LayoutInflater.from(mContext).inflate(R.layout.ed_filter_list_item, null);
            return new BrandTypeViewHolder(filterList);
        }
        else if (viewType == TYPE_FOOTER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_skwtransfer_footer, parent, false);
            return new FooterViewHolder(itemView);
        }else return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        try
        {
            if (holder instanceof BrandTypeViewHolder) {
                ((BrandTypeViewHolder)holder).list.get(0).setText(brandTypeList.get(position).getBrandLevel1Code());
                ((BrandTypeViewHolder)holder).list.get(1).setVisibility(View.INVISIBLE);
                ((BrandTypeViewHolder)holder).itemCheckBox.setChecked(brandTypeList.get(position).isSelected());
                ((BrandTypeViewHolder)holder).view.setVisibility(position < brandTypeList.size() - 1 ? View.VISIBLE : View.INVISIBLE);
            }
            else {
                ((FooterViewHolder) holder).footer.setVisibility(showfooter ? View.VISIBLE : View.GONE);
            }
        }
        catch(Exception e)
        {
            Log.e(TAG, "onBindViewHolder: " );
        }
    }

    @Override
    public int getItemCount()
    {
        if (brandTypeList.size() > 0 || brandTypeList != null)
        {
            return brandTypeList.size();
        } else {
            return 0;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == brandTypeList.size()) {
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
        if (brandTypeFilter == null) {
            brandTypeFilter = new BrandTypeFilter();
        }
        return brandTypeFilter;
    }

    public class BrandTypeViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.itemCheckBox)
        CheckBox itemCheckBox;
        @BindViews({R.id.txtdeptname, R.id.txt_code_name})
        List<TextView> list;
        @BindView(R.id.item_decorator)
        View view;
        public BrandTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @SuppressWarnings("unchecked")
    private class BrandTypeFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0)
            {
                ArrayList<BrandVendorModel> filterList = new ArrayList<>();
                for (int i = 0; i < duplicateList.size(); i++)
                {
                    if (duplicateList.get(i).getBrandLevel1Code().toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            brandTypeList = (ArrayList<BrandVendorModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
