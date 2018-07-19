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
 * Created by pamrutkar on 21/05/18.
 */

@SuppressWarnings({"ALL", "unused"})
public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> implements Filterable {

    private static final String TAG = SubDeptAdapter.class.getSimpleName();
    private final Context mContext;
    public ArrayList<BrandVendorModel> classList;
    private final ArrayList<BrandVendorModel> duplicateList;
    private final ArrayList<BrandVendorModel> selectedList;
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;
    private boolean showfooter;
    private ClassFilter classFilter;


    public ClassAdapter(Context context, ArrayList<BrandVendorModel> mClassList, ArrayList<BrandVendorModel> arrayList) {
        this.mContext = context;
        this.classList = mClassList;
        this.selectedList = arrayList;
        this.duplicateList = mClassList;
        getFilter();
    }


    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View filterList = LayoutInflater.from(mContext).inflate(R.layout.ed_filter_list_item, null);
        return new ClassViewHolder(filterList);
    }

    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position) {
        try {
            holder.list.get(0).setText(classList.get(position).getProdLevel4Desc());
            holder.list.get(1).setText(classList.get(position).getProdLevel4Code());
            holder.list.get(1).setVisibility(View.INVISIBLE);
            holder.itemCheckBox.setChecked(classList.get(position).isSelected());
            holder.view.setVisibility(position < classList.size() - 1 ? View.VISIBLE : View.INVISIBLE);
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: ");
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == classList.size()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (classList != null) {
            return classList.size();
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
        if (classFilter == null) {
            classFilter = new ClassFilter();
        }
        return classFilter;
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemCheckBox)
        CheckBox itemCheckBox;
        @BindViews({R.id.txtdeptname, R.id.txt_code_name})
        List<TextView> list;

        @BindView(R.id.item_decorator)
        View view;
        public ClassViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @SuppressWarnings("unchecked")
    private class ClassFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0)
            {
                ArrayList<BrandVendorModel> filterList = new ArrayList<>();
                for (int i = 0; i < duplicateList.size(); i++)
                {
                    if (duplicateList.get(i).getProdLevel4Desc().toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            classList = (ArrayList<BrandVendorModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
