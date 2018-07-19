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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.aperotechnologies.retrofit.R;
import com.aperotechnologies.retrofit.service.ApiHandlerAsyncTask;
import com.aperotechnologies.retrofit.service.ConstsCore;
import com.aperotechnologies.retrofit.service.FilterEndlessRecyclerOnScrollListener;
import com.aperotechnologies.retrofit.service.FilterFragmentInteraction;
import com.aperotechnologies.retrofit.service.NotifyFragmentAdapter;
import com.aperotechnologies.retrofit.service.Reusable_Functions;
import com.aperotechnologies.retrofit.service.model.BrandVendorModel;
import com.aperotechnologies.retrofit.view.adapter.StoreAdapter;
import com.aperotechnologies.retrofit.view.ui.RecyclerItemClickListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aperotechnologies.retrofit.service.FilterConstant.ADAPTER_LIST;
import static com.aperotechnologies.retrofit.service.FilterConstant.FILTER_APPLIED;
import static com.aperotechnologies.retrofit.service.FilterConstant.FILTER_DATA;
import static com.aperotechnologies.retrofit.service.FilterConstant.FILTER_HEADER_DATA_CHANGE;
import static com.aperotechnologies.retrofit.service.FilterConstant.HAS_LAZY_LOADING;
import static com.aperotechnologies.retrofit.service.FilterConstant.REQUEST_TYPE_PRODUCT;
import static com.aperotechnologies.retrofit.service.FilterConstant.SELECTED_LIST;
import static com.aperotechnologies.retrofit.service.FilterConstant.STORE;
import static com.aperotechnologies.retrofit.service.FilterConstant.parseResponse;

/**
 * Created by pamrutkar on 21/05/18.
 */

@SuppressWarnings("ALL")
public class StoreFragment extends Fragment implements NotifyFragmentAdapter {

    private final String TAG = StoreFragment.class.getSimpleName();
    private Context context;
    private String userId;
    private String bearerToken;
    private String geoLevel2Code;
    private String lobId;

    @BindView(R.id.recycler_filter)
    RecyclerView recyclerFilter;
    @BindView(R.id.progressBarRelativeLayout)
    public RelativeLayout progressBarRelativeLayout;
    @BindView(R.id.serchEditText)
    EditText searchEditText;
    private SharedPreferences sharedPreferences;
    private String[] hierarchyList;
    private RequestQueue queue;
    private Gson gson;
    private ArrayList<BrandVendorModel> mStoreList;
    private ArrayList<BrandVendorModel> mSelectedStoreList;
    private boolean hasLazyLoading;
    private String mFilterData = "";
    private String mFilterHeader = "";
    private boolean hasFilterHeader;
    private int offset = 500;
    private final int limit = 500;
    private FilterFragmentInteraction fragmentInteraction;
    private StoreAdapter storeAdapter;
    private boolean mHeaderChange = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle bundle = getArguments();
            mStoreList = (ArrayList<BrandVendorModel>) bundle.getSerializable(ADAPTER_LIST);
            mSelectedStoreList = (ArrayList<BrandVendorModel>) bundle.getSerializable(SELECTED_LIST);
            hasLazyLoading = bundle.getBoolean(HAS_LAZY_LOADING, false);
            hasFilterHeader = bundle.getBoolean(FILTER_APPLIED, false);
            mFilterHeader = bundle.getString(FILTER_DATA);
            mHeaderChange = bundle.getBoolean(FILTER_HEADER_DATA_CHANGE);
            NetworkAccess();
            if (hasFilterHeader && !Reusable_Functions.isStringEmpty(mFilterHeader) && mHeaderChange) {
                mStoreList.clear();
            }
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ",e );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View sales_view = inflater.inflate(R.layout.activity_common_filter, container, false);
        Log.e(TAG, "onCreateView: ");
        ButterKnife.bind(this, sales_view);
        return sales_view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            initialise_ui(view);
            fetchFilterHeaderList(hasFilterHeader, mFilterHeader);
        } catch (Exception e) {
            Log.e(TAG, "onViewCreated: ", e);
        }

    }
    private void fetchFilterHeaderList(boolean hasFilterHeader, String mFilterHeader) {
//        if (hasLazyLoading) {
//            Log.e(TAG, "fetchFilterHeaderList: enabling lazy loading");
//            offset = mStoreList.size();
//            setUpOnScrollListener();
//        }
        if (mFilterHeader != null) {
            if (hasFilterHeader && !mFilterHeader.isEmpty() && (mHeaderChange || mStoreList.size() == 0)) {
                offset = 0;
                mStoreList.clear();
//                mSelectedStoreList.clear();
                fragmentInteraction.onItemClick(STORE, mSelectedStoreList);
                if (progressBarRelativeLayout != null) {
                    progressBarRelativeLayout.setVisibility(View.VISIBLE);
                }
                Log.e(TAG, "fetchFilterHeaderList: headerValue: " + mFilterHeader);
                callProductApi();
            }
            else
            {
//                offset = mStoreList.size();
//                setUpOnScrollListener();
            }
        }
    }

    private void setUpOnScrollListener() {
        recyclerFilter.addOnScrollListener(new FilterEndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                try {
                    if (Reusable_Functions.chkStatus(context)) {
                        if (storeAdapter.getFooter()) {
                            offset += limit;
                            callProductApi();
                            storeAdapter.setfooter(true);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onLoadMore: ", e);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated: ");
        View v = getView();
        setHasOptionsMenu(true);
    }

    private void initialise_ui(View v) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerFilter.setHasFixedSize(true);
        storeAdapter = new StoreAdapter(context, mStoreList, mSelectedStoreList);
        recyclerFilter.setAdapter(storeAdapter);
        recyclerFilter.setLayoutManager(layoutManager);
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
                    if(!searchData.equals("") || !searchData.isEmpty()) {
                        storeAdapter.getFilter().filter(searchData);
                    }
                    else
                    {
                        storeAdapter = new StoreAdapter(context, mStoreList, mSelectedStoreList);
                        recyclerFilter.setAdapter(storeAdapter);

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
            public void onItemClick(View view, int position) {
                try {
                    if(storeAdapter.storeList.size() != mStoreList.size() && searchEditText.getText().toString().length() != 0)
                    {
                        storeAdapter.storeList.get(position).setSelected(!storeAdapter.storeList.get(position).isSelected());
                        if (storeAdapter.storeList.get(position).isSelected()) {
                            mSelectedStoreList.add(storeAdapter.storeList.get(position));
                        } else {

                            for (BrandVendorModel selectedModel : mSelectedStoreList) {
                                if (selectedModel.getStoreCode().equals(storeAdapter.storeList.get(position).getStoreCode()) ||
                                        selectedModel.getStoreCodeParam().equals(storeAdapter.storeList.get(position).getStoreCodeParam()) ) {
                                    mSelectedStoreList.remove(mSelectedStoreList.indexOf(selectedModel));
                                    break;
                                }
                            }
//
                        }
                    }
                    else
                    {
                        mStoreList.get(position).setSelected(!mStoreList.get(position).isSelected());
                        if (mStoreList.get(position).isSelected()) {
                            mSelectedStoreList.add(mStoreList.get(position));
                        } else {

                            for (BrandVendorModel selectedModel : mSelectedStoreList) {
                                if (selectedModel.getStoreCode().equals(mStoreList.get(position).getStoreCode()) ||
                                        selectedModel.getStoreCodeParam().equals(mStoreList.get(position).getStoreCodeParam()) ) {
                                    mSelectedStoreList.remove(mSelectedStoreList.indexOf(selectedModel));
                                    break;
                                }
                            }
//                        if (mSelectedStoreList.contains(mStoreList.get(position))) {
//                            mSelectedStoreList.remove(mStoreList.get(position));
//                        }
                        }
                    }


//                    if (mSelectedStoreList.size() > 0)
//                    {
//                        mFilterData = "&storeCode=";
//                        for (int i = 0; i < mSelectedStoreList.size(); i++) {
//                            if (i == mSelectedStoreList.size() - 1) {
//                                mFilterData += mSelectedStoreList.get(i).getStoreCodeParam();
//                            } else {
//                                mFilterData += mSelectedStoreList.get(i).getStoreCodeParam() + ",";
//                            }
//                        }
//                    }
//                    else
//                    {
//                        mFilterData = "";
//                    }
                    fragmentInteraction.onItemClick(STORE, mSelectedStoreList);

                    storeAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e(TAG, "onItemClick: ");
                }
            }
        }));

    }

    private void NetworkAccess() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearerToken = sharedPreferences.getString("bearerToken", "");
        geoLevel2Code = sharedPreferences.getString("concept", "");
        String hierarchyLevels = sharedPreferences.getString("hierarchyLevels", "");
        hierarchyList = hierarchyLevels.split(",");
        lobId = sharedPreferences.getString("lobid", "");
        final Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof FilterFragmentInteraction)
            this.fragmentInteraction = (FilterFragmentInteraction) context;
        else
            throw new RuntimeException("Please implement FilterFragmentInteraction interface...");
    }

    public static Fragment getInstance(Bundle bundle) {
        StoreFragment marketFragment = new StoreFragment();
        marketFragment.setArguments(bundle);
        return marketFragment;
    }

    public void notifyAdapter(ArrayList<BrandVendorModel> list) {
        try {
            if (list.size() != mStoreList.size()) {
                mStoreList.clear();
                mStoreList.addAll(list);
            }
            mSelectedStoreList.clear();
            mFilterData = "";
            if (storeAdapter != null) {
                storeAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e(TAG, "notifyAdapter: ", e);
        }

    }

    private void callProductApi() {
        String[] networkParam = new String[2];
        networkParam[0] = getUrl();
        networkParam[1] = bearerToken;
        ApiHandlerAsyncTask apiHandlerAsyncTask = new ApiHandlerAsyncTask(new ApiResponseHandler(), 0, REQUEST_TYPE_PRODUCT);
        apiHandlerAsyncTask.execute(networkParam);
    }

    private String getUrl() {
        return ConstsCore.web_url + "/v1/display/storehierarchyEDSF/"
                + userId
                + "?level=" + 4
                + (Reusable_Functions.isStringEmpty(mFilterHeader) ? "" : mFilterHeader.replaceAll(" ", "%20"));
    }

    @SuppressLint("HandlerLeak")
    private class ApiResponseHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                String response = (String) msg.obj;
                int level = msg.arg1;
                int requestType = msg.arg2;

                ArrayList<BrandVendorModel> list = parseResponse(response,gson);
                ArrayList<BrandVendorModel> temp = new ArrayList<BrandVendorModel>(mSelectedStoreList);
                for (BrandVendorModel model : temp) {
//                    int index = FilterConstant.addSelectedModel(model, list, STORE);
//                    if (index < 0 ) {
//                        mSelectedStoreList.remove(mSelectedStoreList.indexOf(model)); // removing model if its not in api
//                    }
                }
                fragmentInteraction.onItemClick(STORE, mSelectedStoreList);
                mStoreList.addAll(list);
                storeAdapter.setfooter(false);
                storeAdapter.notifyDataSetChanged();
                if (mStoreList.size() == 0){
                    Toast.makeText(context, "No values to display...", Toast.LENGTH_SHORT).show();
                }
                if (progressBarRelativeLayout != null) {
                    progressBarRelativeLayout.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (progressBarRelativeLayout != null) {
                    progressBarRelativeLayout.setVisibility(View.GONE);
                }
            }
        }
    }

}
