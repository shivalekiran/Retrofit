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
import com.aperotechnologies.retrofit.service.FilterConstant;
import com.aperotechnologies.retrofit.service.FilterEndlessRecyclerOnScrollListener;
import com.aperotechnologies.retrofit.service.FilterFragmentInteraction;
import com.aperotechnologies.retrofit.service.NotifyFragmentAdapter;
import com.aperotechnologies.retrofit.service.Reusable_Functions;
import com.aperotechnologies.retrofit.service.model.BrandVendorModel;
import com.aperotechnologies.retrofit.view.adapter.MarketAdapter;
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
import static com.aperotechnologies.retrofit.service.FilterConstant.MARKET;
import static com.aperotechnologies.retrofit.service.FilterConstant.REQUEST_TYPE_PRODUCT;
import static com.aperotechnologies.retrofit.service.FilterConstant.SELECTED_LIST;


/**
 * Created by pamrutkar on 21/05/18.
 */

@SuppressWarnings("ALL")
public class MarketFragment extends Fragment implements NotifyFragmentAdapter {

    private final String TAG = ClassFragment.class.getSimpleName();
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
    private ArrayList<BrandVendorModel> mMarketList;
    private ArrayList<BrandVendorModel> mSelectedMarketList;
    private boolean hasLazyLoading;
    private String mFilterData = "";
    private String mFilterHeader = "";
    private boolean hasFilterHeader;
    private int offset = 500;
    private final int limit = 500;
    private FilterFragmentInteraction fragmentInteraction;
    private MarketAdapter marketAdapter;

    private boolean mHeaderChange = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle bundle = getArguments();
            mMarketList = (ArrayList<BrandVendorModel>) bundle.getSerializable(ADAPTER_LIST);
            mSelectedMarketList = (ArrayList<BrandVendorModel>) bundle.getSerializable(SELECTED_LIST);
            hasLazyLoading = bundle.getBoolean(HAS_LAZY_LOADING, false);
            hasFilterHeader = bundle.getBoolean(FILTER_APPLIED, false);
            mFilterHeader = bundle.getString(FILTER_DATA);
            mHeaderChange = bundle.getBoolean(FILTER_HEADER_DATA_CHANGE);
            NetworkAccess();
            if (hasFilterHeader && !Reusable_Functions.isStringEmpty(mFilterHeader) && mHeaderChange) {
                mMarketList.clear();
            }
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e);
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
//            offset = mMarketList.size();
//            setUpOnScrollListener();
//        }
        if (mFilterHeader != null) {
            if (hasFilterHeader && !mFilterHeader.isEmpty() && (mHeaderChange || mMarketList.size() == 0)) {
                offset = 0;
                mMarketList.clear();
//                mSelectedMarketList.clear(); // todo : save as it is for show previous selection
                fragmentInteraction.onItemClick(MARKET, mSelectedMarketList);
                if (progressBarRelativeLayout != null) {
                    progressBarRelativeLayout.setVisibility(View.VISIBLE);
                }
                Log.e(TAG, "fetchFilterHeaderList: headerValue: " + mFilterHeader);
                callProductApi();
            }
            else
            {
//                offset = mMarketList.size();
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
                        if (marketAdapter.getFooter()) {
                            offset += limit;
                            callProductApi();
                            marketAdapter.setfooter(true);
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
        recyclerFilter.setLayoutManager(layoutManager);
        recyclerFilter.setHasFixedSize(true);
        marketAdapter = new MarketAdapter(context, mMarketList, mSelectedMarketList);
        recyclerFilter.setAdapter(marketAdapter);

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

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                assert inputManager != null;
                inputManager.hideSoftInputFromWindow(searchEditText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                try {
                    String searchData = searchEditText.getText().toString();
                    Log.e("s :", "" + searchData);
                    if(!searchData.equals("") || !searchData.isEmpty())
                    {
                        marketAdapter.getFilter().filter(searchData);
                    }
                    else
                    {
                        marketAdapter = new MarketAdapter(context, mMarketList, mSelectedMarketList);
                        recyclerFilter.setAdapter(marketAdapter);
                    }

                } catch (Exception e) {
                    Log.e(TAG, "onTextChanged: ");
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
                    if(marketAdapter.marketList.size() != mMarketList.size() && searchEditText.getText().toString().length() != 0)
                    {
                        marketAdapter.marketList.get(position).setSelected(!marketAdapter.marketList.get(position).isSelected());
                        if (marketAdapter.marketList.get(position).isSelected()) {
                            mSelectedMarketList.add(marketAdapter.marketList.get(position));
                        } else {
//                        if (mSelectedMarketList.contains(mMarketList.get(position))) {
//                            mSelectedMarketList.remove(mMarketList.get(position));
//                        }
                            for (BrandVendorModel selectedModel : mSelectedMarketList) {
                                if (selectedModel.getMarketED().equals(marketAdapter.marketList.get(position).getMarketED())) {
                                    mSelectedMarketList.remove(mSelectedMarketList.indexOf(selectedModel));
                                    break;
                                }
                            }
                        }
                    }
                    else
                    {
                        mMarketList.get(position).setSelected(!mMarketList.get(position).isSelected());
                        if (mMarketList.get(position).isSelected()) {
                            mSelectedMarketList.add(mMarketList.get(position));
                        } else {
//                        if (mSelectedMarketList.contains(mMarketList.get(position))) {
//                            mSelectedMarketList.remove(mMarketList.get(position));
//                        }
                            for (BrandVendorModel selectedModel : mSelectedMarketList) {
                                if (selectedModel.getMarketED().equals(mMarketList.get(position).getMarketED())) {
                                    mSelectedMarketList.remove(mSelectedMarketList.indexOf(selectedModel));
                                    break;
                                }
                            }
                        }
                    }


//                    if (mSelectedMarketList.size() > 0) {
//                        mFilterData = "&marketED=";
//                        for (int i = 0; i < mSelectedMarketList.size(); i++) {
//                            if (i == mSelectedMarketList.size() - 1) {
//                                mFilterData += mSelectedMarketList.get(i).getMarketED();
//                            } else {
//                                mFilterData += mSelectedMarketList.get(i).getMarketED() + ",";
//                            }
//                        }
//                    } else {
//                        mFilterData = "";
//                    }
                    fragmentInteraction.onItemClick(MARKET, mSelectedMarketList);
                    marketAdapter.notifyDataSetChanged();
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
        MarketFragment marketFragment = new MarketFragment();
        marketFragment.setArguments(bundle);
        return marketFragment;
    }

    public void notifyAdapter(ArrayList<BrandVendorModel> list) {
        try {
            if (list.size() != mMarketList.size()) {
                mMarketList.clear();
                mMarketList.addAll(list);
            }
            mSelectedMarketList.clear();
            mFilterData = "";
            if (marketAdapter != null) {
                marketAdapter.notifyDataSetChanged();
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
                + "?level=" + 2
                + (Reusable_Functions.isStringEmpty(mFilterHeader) ? "" : mFilterHeader);
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
//                if (hasFilterHeader) {
//                    mMCHeaderList.addAll(FilterConstant.parseResponse(response, gson));
//                } else {
//                }

                ArrayList<BrandVendorModel> list = FilterConstant.parseResponse(response,gson);
                ArrayList<BrandVendorModel> temp = new ArrayList<BrandVendorModel>(mSelectedMarketList);
                for (BrandVendorModel model : temp) {
//                    int index = FilterConstant.addSelectedModel(model, list, MARKET);
//                    if (index < 0 ) {
//                        mSelectedMarketList.remove(mSelectedMarketList.indexOf(model)); // removing model if its not in api
//                    }
                }
                fragmentInteraction.onItemClick(MARKET, mSelectedMarketList);
                mMarketList.addAll(list);
                marketAdapter.setfooter(false);
                marketAdapter.notifyDataSetChanged();
                if (mMarketList.size() == 0){
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
