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
 * Created by pamrutkar on 21/05/18.
 */

@SuppressWarnings("ALL")
public class SubDeptAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private static final String TAG = SubDeptAdapter.class.getSimpleName();
    private final Context mContext;
    public ArrayList<BrandVendorModel> subdept_List;
    private ArrayList<BrandVendorModel> duplicateList;
    private ArrayList<BrandVendorModel> selectedList;
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;
    private boolean showfooter;
    private SubDeptFilter subDeptFilter;


    public SubDeptAdapter(Context context, ArrayList<BrandVendorModel> mSubDeptList, ArrayList<BrandVendorModel> arrayList) {
        this.mContext = context;
        this.subdept_List = mSubDeptList;
        this.selectedList = arrayList;
        this.duplicateList = mSubDeptList;
        getFilter();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View filterList = LayoutInflater.from(mContext).inflate(R.layout.ed_filter_list_item, parent, false);
            return new SubDeptViewHolder(filterList);
        } else if (viewType == TYPE_FOOTER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_skwtransfer_footer, parent, false);
            return new FooterViewHolder(itemView);
        } else return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof SubDeptViewHolder) {
                ((SubDeptViewHolder) holder).list.get(0).setText(subdept_List.get(position).getProdLevel3Desc());
                ((SubDeptViewHolder) holder).list.get(1).setText(subdept_List.get(position).getProdLevel3Code());
                ((SubDeptViewHolder) holder).list.get(1).setVisibility(View.INVISIBLE);
                ((SubDeptViewHolder) holder).itemCheckBox.setChecked(subdept_List.get(position).isSelected());
                ((SubDeptViewHolder) holder).view.setVisibility(position < subdept_List.size() - 1 ? View.VISIBLE : View.INVISIBLE);
            }else if (holder instanceof FooterViewHolder){
                ((FooterViewHolder) holder).footer.setVisibility(showfooter ? View.VISIBLE : View.GONE);
            }
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: ");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == subdept_List.size()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (subdept_List != null) {
            return subdept_List.size();
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
        if (subDeptFilter == null) {
            subDeptFilter = new SubDeptFilter();
        }
        return subDeptFilter;
    }

    public class SubDeptViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemCheckBox)
        CheckBox itemCheckBox;
        @BindViews({R.id.txtdeptname, R.id.txt_code_name})
        List<TextView> list;

        @BindView(R.id.item_decorator)
        View view;
        public SubDeptViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @SuppressWarnings("unchecked")
    private class SubDeptFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0)
            {
                ArrayList<BrandVendorModel> filterList = new ArrayList<>();
                for (int i = 0; i < duplicateList.size(); i++)
                {
                    if (duplicateList.get(i).getProdLevel3Desc().toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            subdept_List = (ArrayList<BrandVendorModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
