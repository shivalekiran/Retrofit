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
import com.aperotechnologies.retrofit.view.adapter.MCAdapter;
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
import static com.aperotechnologies.retrofit.service.FilterConstant.MC;
import static com.aperotechnologies.retrofit.service.FilterConstant.MC_LEVEL;
import static com.aperotechnologies.retrofit.service.FilterConstant.REQUEST_TYPE_PRODUCT;
import static com.aperotechnologies.retrofit.service.FilterConstant.SELECTED_LIST;


/**
 * Created by pamrutkar on 21/05/18.
 */

@SuppressWarnings("ALL")
public class MCFragment extends Fragment implements NotifyFragmentAdapter {

    private final String TAG = MCFragment.class.getSimpleName();

    private Context context;
    private String userId;
    private String bearerToken;
    private String geoLevel2Code;
    private String lobId;
    @BindView(R.id.recycler_filter)
    RecyclerView recyclerFilter;


    @BindView(R.id.serchEditText)
    EditText searchEditText;

    @BindView(R.id.progressBarRelativeLayout)
    public RelativeLayout progressBarRelativeLayout;

    private SharedPreferences sharedPreferences;
    private String[] hierarchyList;
    private RequestQueue queue;
    private Gson gson;
    private ArrayList<BrandVendorModel> mMCList;
    //    private ArrayList<BrandVendorModel> mMCHeaderList;
    private ArrayList<BrandVendorModel> mSelectedMCList;
    private boolean hasLazyLoading;
    private MCAdapter mcAdapter;
    private String mFilterHeader = "";
    private FilterFragmentInteraction fragmentInteraction;
    private boolean hasFilterHeader;
    private int offset = 500;
    private final int limit = 500;
    private boolean mHeaderChange = false;

    public MCFragment() {

    }

    public static Fragment getInstance(Bundle bundle) {
        MCFragment mcFragment = new MCFragment();
        mcFragment.setArguments(bundle);
        return mcFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle bundle = getArguments();
            mMCList = (ArrayList<BrandVendorModel>) bundle.getSerializable(ADAPTER_LIST);
            mSelectedMCList = (ArrayList<BrandVendorModel>) bundle.getSerializable(SELECTED_LIST);
            mHeaderChange = bundle.getBoolean(FILTER_HEADER_DATA_CHANGE);
            hasLazyLoading = bundle.getBoolean(HAS_LAZY_LOADING);
            hasFilterHeader = bundle.getBoolean(FILTER_APPLIED, false);
            mFilterHeader = bundle.getString(FILTER_DATA);

//            mMCHeaderList = new ArrayList<>();
            NetworkAccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View sales_view = inflater.inflate(R.layout.activity_common_filter, container, false);
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
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void initialise_ui(View v) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerFilter.setLayoutManager(layoutManager);
        recyclerFilter.setHasFixedSize(true);
//        if (hasFilterHeader) {
//            mcAdapter = new MCAdapter(context, mMCHeaderList, mSelectedMCList);
//        } else {
//        }
        mcAdapter = new MCAdapter(context, mMCList, mSelectedMCList);
        recyclerFilter.setAdapter(mcAdapter);

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
                    if(!searchData.isEmpty() || !searchData.equals(""))
                    {
                        mcAdapter.getFilter().filter(searchData);
                    }
                    else
                    {
                        mcAdapter = new MCAdapter(context, mMCList, mSelectedMCList);
                        recyclerFilter.setAdapter(mcAdapter);
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
                try
                {
                    if(mcAdapter.mcList.size() != mMCList.size() && searchEditText.getText().toString().length() != 0)
                    {
                        mcAdapter.mcList.get(position).setSelected(!mcAdapter.mcList.get(position).isSelected());
                        if (mcAdapter.mcList.get(position).isSelected()) {
                            mSelectedMCList.add(mcAdapter.mcList.get(position));
                        } else {
                            for (BrandVendorModel selectedModel : mSelectedMCList) {
                                if (selectedModel.getProdLevel6Code().equals(mcAdapter.mcList.get(position).getProdLevel6Code()) ||
                                        selectedModel.getProdLevel6Desc().equals(mcAdapter.mcList.get(position).getProdLevel6Desc())) {
                                    mSelectedMCList.remove(mSelectedMCList.indexOf(selectedModel));
                                    break;
                                }
                            }
                        }
                    }
                    else
                    {
                        mMCList.get(position).setSelected(!mMCList.get(position).isSelected());
                        if (mMCList.get(position).isSelected()) {
                            mSelectedMCList.add(mMCList.get(position));
                        } else {
                            for (BrandVendorModel selectedModel : mSelectedMCList) {
                                if (selectedModel.getProdLevel6Code().equals(mMCList.get(position).getProdLevel6Code()) ||
                                        selectedModel.getProdLevel6Desc().equals(mMCList.get(position).getProdLevel6Desc())) {
                                    mSelectedMCList.remove(mSelectedMCList.indexOf(selectedModel));
                                    break;
                                }
                            }
                        }
                    }

                    mcAdapter.notifyDataSetChanged();
                    fragmentInteraction.onItemClick(MC, mSelectedMCList);
                } catch (Exception e) {
                    e.printStackTrace();
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
        Log.e("NetworkAccess: ", "" + hierarchyLevels);
        for (int i = 0; i < hierarchyList.length; i++) {
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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof FilterFragmentInteraction) {
            this.fragmentInteraction = (FilterFragmentInteraction) context;
        } else
            throw new RuntimeException("Please implement FilterFragmentInteraction interface...");

    }

    @Override
    public void notifyAdapter(ArrayList<BrandVendorModel> list) {
        try {
            if (list.size() != mMCList.size()) {
                mMCList.clear();
                mMCList.addAll(list);
            }
            mSelectedMCList.clear();
            if (mcAdapter != null) {
                mcAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e(TAG, "notifyAdapter: ", e);
        }
    }

    private void fetchFilterHeaderList(boolean hasFilterHeader, String mFilterHeader) {
        if (hasLazyLoading) {
            Log.e(TAG, "fetchFilterHeaderList: enabling lazy loading");
            offset = 0;
            setUpOnScrollListener();
        }
        if (mFilterHeader != null && mFilterHeader.length() > 0) {
            if (hasFilterHeader && !mFilterHeader.isEmpty() && (mHeaderChange || mMCList.size() == 0)) {
                offset = 0;
                mMCList.clear();
//                mSelectedMCList.clear();
                fragmentInteraction.onItemClick(MC, mSelectedMCList);
                if (progressBarRelativeLayout != null) {
                    progressBarRelativeLayout.setVisibility(View.VISIBLE);
                }
                Log.e(TAG, "fetchFilterHeaderList: headerValue: " + mFilterHeader);
                callAPis();
            } else {
//                offset = mMCList.size();
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
                        if (mcAdapter.getFooter()) {
                            offset += limit;
                            callAPis();
                            mcAdapter.setfooter(true);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onLoadMore: ", e);
                }
            }
        });
    }

    private  void callAPis(){
        if (hasLazyLoading && !hasFilterHeader && mFilterHeader.isEmpty()){
            callDisplayAPI();
        }else {
            callProductApi();
        }
    }

    private void callProductApi() {
        String[] networkParam = new String[2];
        networkParam[0] = getUrl();
        networkParam[1] = bearerToken;
        ApiHandlerAsyncTask apiHandlerAsyncTask = new ApiHandlerAsyncTask(new ApiResponseHandler(), 0, REQUEST_TYPE_PRODUCT);
        apiHandlerAsyncTask.execute(networkParam);
    }

    private void callDisplayAPI() {
        String[] networkParam = new String[2];
        networkParam[0] = getDisplayUrl();
        networkParam[1] = bearerToken;
        ApiHandlerAsyncTask apiHandlerAsyncTask = new ApiHandlerAsyncTask(new ApiResponseHandler(), 0, REQUEST_TYPE_PRODUCT);
        apiHandlerAsyncTask.execute(networkParam);
    }

    private String getDisplayUrl() {
        return ConstsCore.web_url + "/v1/display/producthierarchyDisplayEDSF/"
                + userId
                + "?offset=" + offset
                + "&limit=" + limit
                + "&level=" + 5;
    }

    private String getUrl() {
        return ConstsCore.web_url + "/v1/display/producthierarchyEDSFNew/"            // producthierarchyDisplayEDSF
                + userId
                + "?level=" + 5
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
//                if (hasFilterHeader) {
//                    mMCHeaderList.addAll(FilterConstant.parseResponse(response, gson));
//                } else {
//                }

                ArrayList<BrandVendorModel> list;
                if (hasLazyLoading && !hasFilterHeader){
                    list = FilterConstant.parseResponse(response, gson);
                }else {
                    list = FilterConstant.parseResponse(response, gson, MC_LEVEL);
                }
                ArrayList<BrandVendorModel> temp = new ArrayList<>(mSelectedMCList);
                for (BrandVendorModel model :temp ) {
//                    int index = FilterConstant.addSelectedModel(model, list, MC);
//                    if (index < 0 && list.size() != limit) {
//                        mSelecte/dMCList.remove(mSelectedMCList.indexOf(model)); // removing model if its not in api and resp in less than limit
//                    }
                }
                fragmentInteraction.onItemClick(MC, mSelectedMCList);
                mMCList.addAll(list);
                mcAdapter.setfooter(false);
                mcAdapter.notifyDataSetChanged();
                if (mMCList.size() == 0){
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
