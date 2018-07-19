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

@SuppressWarnings({"ALL", "BooleanMethodIsAlwaysInverted"})
public class BrandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {


    private static final String TAG = BrandAdapter.class.getSimpleName();
    private final Context mContext;
    public ArrayList<BrandVendorModel> brandList;
    private ArrayList<BrandVendorModel> duplicateList;

    private ArrayList<BrandVendorModel> selectedList;
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;
    private boolean showfooter;
    private BrandFilter brandFilter;




    public BrandAdapter(Context context, ArrayList<BrandVendorModel> mBrandList, ArrayList<BrandVendorModel> arrayList)
    {
        this.mContext = context;
        this.brandList = mBrandList;
        this.selectedList = arrayList;
        if(mBrandList != null || mBrandList.size() != 0 || mBrandList.size() > 0) {
            this.duplicateList = mBrandList;
        }
        else
        {
            this.duplicateList = arrayList;
        }
        getFilter();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == TYPE_ITEM) {
            View filterList = LayoutInflater.from(mContext).inflate(R.layout.ed_filter_list_item, null);
            return new BrandViewHolder(filterList);
        }
        else if (viewType == TYPE_FOOTER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_skwtransfer_footer, parent, false);
            return new FooterViewHolder(itemView);
        }else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        try
        {
            if (holder instanceof BrandViewHolder) {
                ((BrandViewHolder)holder).list.get(0).setText(brandList.get(position).getBrandLevel3Desc());
                ((BrandViewHolder)holder).list.get(1).setText(brandList.get(position).getBrandLevel3Code());
                ((BrandViewHolder)holder).list.get(1).setVisibility(View.INVISIBLE);
                ((BrandViewHolder) holder).itemCheckBox.setChecked(brandList.get(position).isSelected());
                ((BrandViewHolder) holder).view.setVisibility(position < brandList.size() - 1 ? View.VISIBLE : View.INVISIBLE);
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
        if (brandList.size() > 0 || brandList != null)
        {
            return brandList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == brandList.size()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }


    public void setfooter(boolean showfooter) {
        this.showfooter = showfooter;
    }

    public boolean getFooter() {
        return this.showfooter;
    }

    @Override
    public Filter getFilter() {
        if (brandFilter == null) {
            brandFilter = new BrandFilter();
        }
        return brandFilter;    }


    public class BrandViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.itemCheckBox)
        CheckBox itemCheckBox;
        @BindViews({R.id.txtdeptname, R.id.txt_code_name})
        List<TextView> list;
        @BindView(R.id.item_decorator)
        View view;
        public BrandViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @SuppressWarnings("unchecked")
    private class BrandFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0)
            {
                ArrayList<BrandVendorModel> filterList = new ArrayList<>();
                for (int i = 0; i < duplicateList.size(); i++)
                {
                    if (duplicateList.get(i).getBrandLevel3Desc().toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            brandList = (ArrayList<BrandVendorModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
