package com.aperotechnologies.retrofit.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
public class VectorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {


    private static final String TAG = VectorAdapter.class.getSimpleName();
    private final Context mContext;
    private boolean showfooter;
    public ArrayList<BrandVendorModel> vectorList;
    @SuppressWarnings("CanBeFinal")
    private ArrayList<BrandVendorModel> duplicateList;
    @SuppressWarnings("CanBeFinal")
    private ArrayList<BrandVendorModel> selectedList;
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;
    private VendorFilter vendorFilter;

    public VectorAdapter(Context context, ArrayList<BrandVendorModel> mVectorList, ArrayList<BrandVendorModel> arrayList) {
        this.mContext = context;
        this.vectorList = mVectorList;
        this.selectedList = arrayList;
        this.duplicateList = mVectorList;
        getFilter();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View filterList = LayoutInflater.from(mContext).inflate(R.layout.ed_filter_list_item, null);
            return new VectorViewHolder(filterList);
        } else if (viewType == TYPE_FOOTER) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.activity_skwtransfer_footer, parent, false);
            return new FooterViewHolder(itemView);
        } else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof VectorViewHolder) {
                ((VectorViewHolder) holder).list.get(0).setText(vectorList.get(position).getVendorDesc());
                ((VectorViewHolder) holder).list.get(1).setText(vectorList.get(position).getVendorCode());
                ((VectorViewHolder) holder).list.get(1).setVisibility(View.INVISIBLE);
                ((VectorViewHolder) holder).itemCheckBox.setChecked(vectorList.get(position).isSelected());
                ((VectorViewHolder) holder).view.setVisibility(position < vectorList.size() - 1 ? View.VISIBLE : View.INVISIBLE);
            } else {
                ((FooterViewHolder) holder).lazy_loader.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.ed_total_sales), android.graphics.PorterDuff.Mode.MULTIPLY);

                ((FooterViewHolder) holder).footer.setVisibility(showfooter ? View.VISIBLE : View.GONE);
            }
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: ");
        }
    }

    @Override
    public int getItemCount() {
        if (vectorList.size() > 0 || vectorList != null) {
            return vectorList.size();
        } else {
            return 0;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == vectorList.size()) {
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
        if (vendorFilter == null) {
            vendorFilter = new VendorFilter();
        }
        return vendorFilter;
    }


    public class VectorViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemCheckBox)
        CheckBox itemCheckBox;
        @BindViews({R.id.txtdeptname, R.id.txt_code_name})
        List<TextView> list;

        @BindView(R.id.item_decorator)
        View view;
        public VectorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @SuppressWarnings("unchecked")
    private class VendorFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<BrandVendorModel> filterList = new ArrayList<>();
                for (int i = 0; i < duplicateList.size(); i++) {
                    if (duplicateList.get(i).getVendorDesc().toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            vectorList = (ArrayList<BrandVendorModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
