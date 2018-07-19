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
public class MCAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {


    private static final String TAG = SubDeptAdapter.class.getSimpleName();
    private final Context mContext;
    public ArrayList<BrandVendorModel> mcList;
    private final ArrayList<BrandVendorModel> selectedList;
    private ArrayList<BrandVendorModel> duplicateList;
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;
    private boolean showfooter;
    private MCFilter mcFilter;


    public MCAdapter(Context context, ArrayList<BrandVendorModel> mcList, ArrayList<BrandVendorModel> arrayList) {
        this.mContext = context;
        this.mcList = mcList;
        this.selectedList = arrayList;
        this.duplicateList = mcList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View filterList = LayoutInflater.from(mContext).inflate(R.layout.ed_filter_list_item, parent, false);
            return new MCViewHolder(filterList);
        } else if (viewType == TYPE_FOOTER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_skwtransfer_footer, parent, false);
            return new FooterViewHolder(itemView);
        } else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof MCViewHolder) {
                ((MCViewHolder) holder).list.get(0).setText(mcList.get(position).getProdLevel6Desc());
                ((MCViewHolder) holder).list.get(1).setText(mcList.get(position).getProdLevel6Code());
                ((MCViewHolder) holder).list.get(1).setVisibility(View.INVISIBLE);
                ((MCViewHolder) holder).itemCheckBox.setChecked(mcList.get(position).isSelected());
                ((MCViewHolder) holder).view.setVisibility(position < mcList.size() - 1 ? View.VISIBLE : View.INVISIBLE);

            }else if (holder instanceof FooterViewHolder){
                ((FooterViewHolder) holder).footer.setVisibility(showfooter ? View.VISIBLE : View.GONE);
            }
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: ");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mcList.size()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (mcList != null) {
            return mcList.size();
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
        if (mcFilter == null) {
            mcFilter = new MCFilter();
        }
        return mcFilter;
    }


    public class MCViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemCheckBox)
        CheckBox itemCheckBox;
        @BindViews({R.id.txtdeptname, R.id.txt_code_name})
        List<TextView> list;

        @BindView(R.id.item_decorator)
        View view;
        public MCViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @SuppressWarnings("unchecked")
    private class MCFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0)
            {
                ArrayList<BrandVendorModel> filterList = new ArrayList<>();
                for (int i = 0; i < duplicateList.size(); i++)
                {
                    if (duplicateList.get(i).getProdLevel6Desc().toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            mcList = (ArrayList<BrandVendorModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
