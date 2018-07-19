package com.aperotechnologies.retrofit.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aperotechnologies.retrofit.R;
import com.aperotechnologies.retrofit.databinding.AdapterMbrCustomerDetailsBinding;
import com.aperotechnologies.retrofit.service.model.MemberDetails;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by pamrutkar on 14/12/17.
 */

public class MemberCustDtlsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private boolean showfooter;
    private Context context;
    List<MemberDetails> arr_custdetails;
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;
    private String update_date;


    public MemberCustDtlsAdapter(Context context, List<MemberDetails> arr_custdetails) {
        this.context = context;
        this.showfooter = showfooter;
        this.arr_custdetails = arr_custdetails;
    }

    public void setfooter(boolean showfooter) {
        this.showfooter = showfooter;
    }


    public boolean getfooter() {
        return this.showfooter;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            AdapterMbrCustomerDetailsBinding adapterMbrCustomerDetailsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                    , R.layout.adapter_mbr_customer_details
                    , parent
                    ,false);
            return new CustDtlsHolder(adapterMbrCustomerDetailsBinding);

        } else
            return null;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof CustDtlsHolder) {
            if (position < arr_custdetails.size()) {
                Date date = null;
                try {
                    Calendar calendar = Calendar.getInstance();
                    String dateInString = arr_custdetails.get(position).getLastVisitDate();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    if (dateInString != null) {
                        date = formatter.parse(dateInString);
                        ((CustDtlsHolder) holder).adapterMbrCustomerDetailsBinding.txtMbrDate.setText("" + date.getDay());
                        ((CustDtlsHolder) holder).adapterMbrCustomerDetailsBinding.txtMbrYear.setText(new SimpleDateFormat("MMM").format(date) + " " + new SimpleDateFormat("yyyy").format(date));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                ((CustDtlsHolder) holder).adapterMbrCustomerDetailsBinding.setMember(arr_custdetails.get(position));
               /* try {
                    String dateInString = arr_custdetails.get(position).getLastVisitDate();
                    Date date = null;
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    date = formatter.parse(dateInString);
                    SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MMM/yyyy");
                    update_date = formatter2.format(date);
                    update_date = update_date.replace("/", " ");
                    ((CustDtlsHolder) holder).txt_mbr_date.setText(update_date.substring(0, 2));
                    ((CustDtlsHolder) holder).txt_mbr_year.setText(update_date.substring(3, 6) + " " + update_date.substring(7, 11));
                } catch (Exception e) {
                    Log.e("TAG", "Exception: " + e.getMessage());
                } finally {
                    ((CustDtlsHolder) holder).txt_mbr_name.setText(arr_custdetails.get(position).getFullName());
                    ((CustDtlsHolder) holder).txt_mbr_email.setText(arr_custdetails.get(position).getEmailAddress());
                    ((CustDtlsHolder) holder).txt_mbr_phone.setText(arr_custdetails.get(position).getMobileNumber());
                    if (arr_custdetails.get(position).getLast90dayVisit() == null || arr_custdetails.get(position).getLast90dayVisit().equals("")) {
                        ((CustDtlsHolder) holder).txt_mbr_avgVisits_val.setText("");
                    } else {
                        ((CustDtlsHolder) holder).txt_mbr_avgVisits_val.setText(arr_custdetails.get(position).getLast90dayVisit() + " in Last 90 days");
                    }
                    ((CustDtlsHolder) holder).txt_mbr_noofvisit.setText(arr_custdetails.get(position).getMbrVisitCount());
                    ((CustDtlsHolder) holder).txt_mbr_avgSize_val.setText("" + Math.round(arr_custdetails.get(position).getAvgBasketVal()) + " , " + Math.round(arr_custdetails.get(position).getAvgBasketQty()) + " " + "Units");

                }*/

            }
        }

    }

    @Override
    public int getItemCount() {
        if (arr_custdetails == null) return 0;
        else return arr_custdetails.size() ;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }


    public class CustDtlsHolder extends RecyclerView.ViewHolder {

        AdapterMbrCustomerDetailsBinding adapterMbrCustomerDetailsBinding;
        public CustDtlsHolder( AdapterMbrCustomerDetailsBinding adapterMbrCustomerDetailsBinding) {
            super(adapterMbrCustomerDetailsBinding.getRoot());
            this.adapterMbrCustomerDetailsBinding = adapterMbrCustomerDetailsBinding;
        }

    }
}