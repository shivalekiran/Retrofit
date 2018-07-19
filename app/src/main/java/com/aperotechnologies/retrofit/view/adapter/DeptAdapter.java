package com.aperotechnologies.retrofit.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
public class DeptAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {


    private static final String TAG = DeptAdapter.class.getName();
    private final Context mContext;
    private boolean showfooter;

    public ArrayList<BrandVendorModel> dept_List;
    private final ArrayList<BrandVendorModel> duplicateList;
    private final ArrayList<BrandVendorModel> selectedList;
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;
    private DeptFilter deptFilter;
    private final boolean[] itemChecked;

    public DeptAdapter(Context context, ArrayList<BrandVendorModel> arrayList, ArrayList<BrandVendorModel> selectedList) {
        this.mContext = context;
        this.dept_List = arrayList;
        this.selectedList = selectedList;
        this.duplicateList = dept_List;
        itemChecked = new boolean[dept_List.size()];
        getFilter();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == TYPE_ITEM)
        {
            View filterList = LayoutInflater.from(mContext).inflate(R.layout.ed_filter_list_item, parent, false);
            return new DeptViewHolder(filterList);
        }
        else if (viewType == TYPE_FOOTER)
        {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_skwtransfer_footer, parent, false);
            return new FooterViewHolder(itemView);
        } else return null;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DeptViewHolder)
        {
            ((DeptViewHolder) holder).list.get(0).setText(dept_List.get(position).getProdLevel2Desc());
            ((DeptViewHolder) holder).itemCheckBox.setChecked(dept_List.get(position).isSelected());
            ((DeptViewHolder) holder).view.setVisibility(position < dept_List.size() - 1 ? View.VISIBLE : View.INVISIBLE);
        } else {
            ((FooterViewHolder) holder).footer.setVisibility(showfooter ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == dept_List.size()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }


    @Override
    public int getItemCount() {
        if (dept_List != null) {
            return dept_List.size();
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
        if (deptFilter == null) {
            deptFilter = new DeptFilter();
        }
        return deptFilter;
    }


    public class DeptViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemCheckBox)
        CheckBox itemCheckBox;
        @BindViews({R.id.txtdeptname, R.id.txt_code_name})
        List<TextView> list;

        @BindView(R.id.item_decorator)
        View view;
        public DeptViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    @SuppressWarnings("unchecked")
    private class DeptFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
               ArrayList<BrandVendorModel> filterList = new ArrayList<>();
                for (int i = 0; i < duplicateList.size(); i++) {
                    if (duplicateList.get(i).getProdLevel2Desc().toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            dept_List = (ArrayList<BrandVendorModel>) results.values;
            notifyDataSetChanged();

        }
    }
}
