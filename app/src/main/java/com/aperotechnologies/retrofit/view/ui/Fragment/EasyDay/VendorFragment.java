package com.aperotechnologies.retrofit.view.ui.Fragment.EasyDay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.aperotechnologies.retrofit.R;
import com.aperotechnologies.retrofit.service.ApiHandlerAsyncTask;
import com.aperotechnologies.retrofit.service.ConstsCore;
import com.aperotechnologies.retrofit.service.FilterConstant;
import com.aperotechnologies.retrofit.service.FilterEndlessRecyclerOnScrollListener;
import com.aperotechnologies.retrofit.service.FilterFragmentInteraction;
import com.aperotechnologies.retrofit.service.NotifyFragmentAdapter;
import com.aperotechnologies.retrofit.service.Reusable_Functions;
import com.aperotechnologies.retrofit.service.model.BrandVendorModel;
import com.aperotechnologies.retrofit.view.adapter.VectorAdapter;
import com.aperotechnologies.retrofit.view.ui.RecyclerItemClickListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pamrutkar on 21/05/18.
 */

@SuppressWarnings("ALL")
public class VendorFragment extends Fragment implements NotifyFragmentAdapter {
    private final String TAG = VendorFragment.class.getSimpleName();
    private Context context;
    private String userId;
    private String bearerToken;
    private String geoLevel2Code;
    private String lobId;

    @BindView(R.id.recycler_filter)
    RecyclerView recyclerFilter;
    @BindView(R.id.serchEditText)
    EditText searchEditText;

    private SharedPreferences sharedPreferences;
    private String[] hierarchyList;
    private RequestQueue queue;
    private Gson gson;
    private ArrayList<BrandVendorModel> mVendorList;
    private ArrayList<BrandVendorModel> mSelectedVendorList;
    private boolean hasLazyLoading;
    private String mFilterData = "";
    private String mFilterHeader = "";
    private FilterFragmentInteraction fragmentInteraction;
    private boolean hasFilterHeader;
    private int offset = 500;
    private final int limit = 500;
    private VectorAdapter vendorAdapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mVendorList = (ArrayList<BrandVendorModel>) bundle.getSerializable(FilterConstant.ADAPTER_LIST);
        mSelectedVendorList = (ArrayList<BrandVendorModel>) bundle.getSerializable(FilterConstant.SELECTED_LIST);
        hasLazyLoading = bundle.getBoolean(FilterConstant.HAS_LAZY_LOADING, false);
        hasFilterHeader = bundle.getBoolean(FilterConstant.FILTER_APPLIED, false);
        mFilterHeader = bundle.getString(FilterConstant.FILTER_DATA);
        NetworkAccess();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View sales_view = inflater.inflate(R.layout.activity_common_filter, container, false);
        Log.e(TAG, "onCreateView: ");
        ButterKnife.bind(this, sales_view);
        return sales_view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initialise_ui(view);
        fetchFilterHeaderList(hasFilterHeader, mFilterHeader);
    }


    private void fetchFilterHeaderList(boolean hasFilterHeader, String mFilterHeader)
    {
        if (hasLazyLoading)
        {
            Log.e(TAG, "fetchFilterHeaderList: enabling lazy loading");
            offset = mVendorList.size();
            setUpOnScrollListener();
        }


    }

    private void setUpOnScrollListener()
    {
        recyclerFilter.addOnScrollListener(new FilterEndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                try {
                    if (Reusable_Functions.chkStatus(context)) {
                        if (vendorAdapter.getFooter()) {
                            offset = offset + limit;
                            callProductApi();
                            vendorAdapter.setfooter(true);
                        }
                    }
                    Log.e(TAG, "onLoadMoreSize: " + mVendorList.size() );
                } catch (Exception e) {
                    Log.e(TAG, "onLoadMore: ", e);
                }
            }
        });
    }

    private void callProductApi()
    {
        String[] networkParam = new String[2];
        networkParam[0] = getUrl();
        networkParam[1] = bearerToken;
        ApiHandlerAsyncTask apiHandlerAsyncTask = new ApiHandlerAsyncTask(new ApiResponseHandler(), 0, FilterConstant.REQUEST_TYPE_PRODUCT);
        apiHandlerAsyncTask.execute(networkParam);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated: ");
        View v = getView();
        setHasOptionsMenu(true);


    }

    private void initialise_ui(View v)
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerFilter.setLayoutManager(layoutManager);
        recyclerFilter.setHasFixedSize(true);
        vendorAdapter = new VectorAdapter(context,mVendorList,mSelectedVendorList);
        recyclerFilter.setAdapter(vendorAdapter);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                Boolean handled = false;
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_NONE) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
                    searchEditText.clearFocus();
                    InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(searchEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    handled = true;
                }
                return handled;
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(searchEditText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                try {
                    String searchData = searchEditText.getText().toString();
                    Log.e("s :", "" + searchData);
                    if(!searchData.isEmpty() || !searchData.equals(""))
                    {
                    vendorAdapter.getFilter().filter(searchData);}
                    else
                    {
                        vendorAdapter = new VectorAdapter(context,mVendorList,mSelectedVendorList);
                        recyclerFilter.setAdapter(vendorAdapter);
                    }

                }catch (Exception e)
                {
                    Log.e(TAG, "onTextChanged: " );
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerFilter.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position)
            {
                try {
                    if(vendorAdapter.vectorList.size() != mVendorList.size() && searchEditText.getText().toString().length() != 0)
                    {
                        vendorAdapter.vectorList.get(position).setSelected(!vendorAdapter.vectorList.get(position).isSelected());
                        if (vendorAdapter.vectorList.get(position).isSelected()) {
                            mSelectedVendorList.add(vendorAdapter.vectorList.get(position));
                        }
                        else
                        {
                            for (BrandVendorModel selectedModel : mSelectedVendorList) {
                                if (selectedModel.getVendorDesc().equals(vendorAdapter.vectorList.get(position).getVendorDesc()) ||
                                        selectedModel.getVendorCode().equals(vendorAdapter.vectorList.get(position).getVendorCode()) )
                                {
                                    mSelectedVendorList.remove(mSelectedVendorList.indexOf(selectedModel));
                                    break;
                                }
                            }
//                            if (mSelectedVendorList.contains(vendorAdapter.vectorList.get(position))) {
//                                mSelectedVendorList.remove(vendorAdapter.vectorList.get(position));
//                            }
                        }
                    }
                    else
                    {
                        mVendorList.get(position).setSelected(!mVendorList.get(position).isSelected());
                        if (mVendorList.get(position).isSelected()) {
                            mSelectedVendorList.add(mVendorList.get(position));
                        } else
                         {
                             for (BrandVendorModel selectedModel : mSelectedVendorList) {
                                 if (selectedModel.getVendorDesc().equals(mVendorList.get(position).getVendorDesc()) ||
                                         selectedModel.getVendorCode().equals(mVendorList.get(position).getVendorCode()) )
                                 {
                                     mSelectedVendorList.remove(mSelectedVendorList.indexOf(selectedModel));
                                     break;
                                 }
                             }

//                            if (mSelectedVendorList.contains(mVendorList.get(position))) {
//                                mSelectedVendorList.remove(mVendorList.get(position));
//                            }
                        }

                    }

                    if (mSelectedVendorList.size() > 0) {
                        mFilterData = "&vendorCode=";
                        for (int i = 0; i < mSelectedVendorList.size(); i++) {
                            if (i == mSelectedVendorList.size() - 1) {
                                mFilterData += mSelectedVendorList.get(i).getVendorCode();
                            } else {
                                mFilterData += mSelectedVendorList.get(i).getVendorCode() + ",";
                            }
                        }
                    } else {
                        mFilterData = "";
                    }
                    fragmentInteraction.onItemClick(FilterConstant.VENDOR, mSelectedVendorList);


                    vendorAdapter.notifyDataSetChanged();
                }
                catch (Exception e)
                {
                    Log.e(TAG, "onItemClick: " );
                }
            }
        }));

    }

    private void NetworkAccess()
    {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearerToken = sharedPreferences.getString("bearerToken", "");
        geoLevel2Code = sharedPreferences.getString("concept", "");
        String hierarchyLevels = sharedPreferences.getString("hierarchyLevels", "");
        hierarchyList = hierarchyLevels.split(",");
        Log.e("NetworkAccess: ", "" + hierarchyLevels);
        for (int i = 0; i < hierarchyList.length; i++)
        {
            hierarchyList[i] = hierarchyList[i].trim();
        }
        lobId = sharedPreferences.getString("lobid", "");
        final Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();

    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.context = context;
        if (context instanceof FilterFragmentInteraction)
            this.fragmentInteraction = (FilterFragmentInteraction) context;
        else
            throw  new RuntimeException("Please implement FilterFragmentInteraction interface...");


    }

    public static Fragment getInstance(Bundle bundle)
    {
        VendorFragment vendorFragment = new VendorFragment();
        vendorFragment.setArguments(bundle);
        return vendorFragment;
    }

    public void notifyAdapter(ArrayList<BrandVendorModel> list)
    {
        try
        {
            if (list.size() != mVendorList.size()) {
                mVendorList.clear();
                mVendorList.addAll(list);
            }
            mSelectedVendorList.clear();
            mFilterData = "";
            if (vendorAdapter != null)
            {
                vendorAdapter.notifyDataSetChanged();
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "notifyAdapter: ", e);
        }
    }

    private String getUrl()
    {
        String url = ConstsCore.web_url + "/v1/display/VendorEDSF/"
                + userId
                + "?offset=" + offset
                + "&limit=" + limit          ;
        Log.e(TAG, "getUrl: " +url );
        return url;
    }

    @SuppressLint("HandlerLeak")
    private class ApiResponseHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            try {
                String response = (String) msg.obj;
                int level = msg.arg1;
                int requestType = msg.arg2;
                mVendorList.addAll(parseResponse(response));
                vendorAdapter.setfooter(false);
                vendorAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<BrandVendorModel> parseResponse(String response) {
        ArrayList<BrandVendorModel> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(response);
            if (array != null || array.length() > 0)
            {
                for (int i = 0; i < array.length(); i++)
                {
                    BrandVendorModel model = gson.fromJson(array.get(i).toString(), BrandVendorModel.class);
                    list.add(model);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "parseResponse: ", e);
        }
        return list;
    }
}
