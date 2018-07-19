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
import com.aperotechnologies.retrofit.view.adapter.SubClassAdapter;
import com.aperotechnologies.retrofit.view.ui.RecyclerItemClickListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pamrutkar on 21/05/18.
 */

@SuppressWarnings("ALL")
public class SubClassFragment extends Fragment implements NotifyFragmentAdapter {
    private final String TAG = ClassFragment.class.getSimpleName();
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
    private ArrayList<BrandVendorModel> mSubClassList;
    private ArrayList<BrandVendorModel> mSelectedSubClassList;
    private boolean hasLazyLoading;

    private String mFilterHeader = "";
    private FilterFragmentInteraction fragmentInteraction;
    private boolean hasFilterHeader;
    private int offset = 500;
    private final int limit = 500;
    private SubClassAdapter subClassAdapter;
    //    private ArrayList<BrandVendorModel> mSubClassHeaderList;
    private boolean mHeaderChange = false;

    public SubClassFragment() {
    }

    public static Fragment getInstance(Bundle bundle) {
        SubClassFragment subclassFragment = new SubClassFragment();
        subclassFragment.setArguments(bundle);
        return subclassFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle bundle = getArguments();
            mSubClassList = (ArrayList<BrandVendorModel>) bundle.getSerializable(FilterConstant.ADAPTER_LIST);
            mSelectedSubClassList = (ArrayList<BrandVendorModel>) bundle.getSerializable(FilterConstant.SELECTED_LIST);
            mHeaderChange = bundle.getBoolean(FilterConstant.FILTER_HEADER_DATA_CHANGE);
            hasLazyLoading = bundle.getBoolean(FilterConstant.HAS_LAZY_LOADING);
            hasFilterHeader = bundle.getBoolean(FilterConstant.FILTER_APPLIED, false);
            mFilterHeader = bundle.getString(FilterConstant.FILTER_DATA);
//            mSubClassHeaderList= new ArrayList<>();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
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

    private void initialise_ui(View v) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerFilter.setLayoutManager(layoutManager);
        recyclerFilter.setHasFixedSize(true);
//        if (hasFilterHeader){
//            subClassAdapter = new SubClassAdapter(context, mSubClassHeaderList, mSelectedSubClassList);
//        }else {
//        }
        subClassAdapter = new SubClassAdapter(context, mSubClassList, mSelectedSubClassList);
        recyclerFilter.setAdapter(subClassAdapter);

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
                inputManager.hideSoftInputFromWindow(searchEditText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                try {
                    String searchData = searchEditText.getText().toString();
                    Log.e("s :", "" + searchData);
                    if (!searchData.isEmpty() || !searchData.equals("")) {
                        subClassAdapter.getFilter().filter(searchData);
                    } else {
                        subClassAdapter = new SubClassAdapter(context, mSubClassList, mSelectedSubClassList);
                        recyclerFilter.setAdapter(subClassAdapter);
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
                    if (subClassAdapter.subclassList.size() != mSubClassList.size() && searchEditText.getText().toString().length() != 0) {
                        subClassAdapter.subclassList.get(position).setSelected(!subClassAdapter.subclassList.get(position).isSelected());
                        if (subClassAdapter.subclassList.get(position).isSelected()) {
                            mSelectedSubClassList.add(subClassAdapter.subclassList.get(position));
                        } else {
                            for (BrandVendorModel selectedModel : mSelectedSubClassList) {
                                if (selectedModel.getProdLevel5Code().equals(subClassAdapter.subclassList.get(position).getProdLevel5Code()) ||
                                        selectedModel.getProdLevel5Code().equals(subClassAdapter.subclassList.get(position).getProdLevel5Code())) {
                                    mSelectedSubClassList.remove(mSelectedSubClassList.indexOf(selectedModel));
                                    break;
                                }
                            }
                        }
                    } else {
                        mSubClassList.get(position).setSelected(!mSubClassList.get(position).isSelected());
                        if (mSubClassList.get(position).isSelected()) {
                            mSelectedSubClassList.add(mSubClassList.get(position));
                        } else {
                            for (BrandVendorModel selectedModel : mSelectedSubClassList) {
                                if (selectedModel.getProdLevel5Code().equals(mSubClassList.get(position).getProdLevel5Code()) ||
                                        selectedModel.getProdLevel5Code().equals(mSubClassList.get(position).getProdLevel5Code())) {
                                    mSelectedSubClassList.remove(mSelectedSubClassList.indexOf(selectedModel));
                                    break;
                                }
                            }
                        }

                    }

                    subClassAdapter.notifyDataSetChanged();
                    fragmentInteraction.onItemClick(FilterConstant.SUBCLASS, mSelectedSubClassList);
                } catch (Exception e) {
                    Log.e(TAG, "onItemClick: ", e);
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

    @Override
    public void notifyAdapter(ArrayList<BrandVendorModel> list) {
        try {
            if (list.size() != mSubClassList.size()) {
                mSubClassList.clear();
                mSubClassList.addAll(list);
            }
            mSelectedSubClassList.clear();
            if (subClassAdapter != null) {
                subClassAdapter.notifyDataSetChanged();
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
        if (mFilterHeader != null && mFilterHeader.length() > 0)
            if (hasFilterHeader && !mFilterHeader.isEmpty() && (mHeaderChange || mSubClassList.size() == 0)) {
                offset = 0;
                if (progressBarRelativeLayout != null) {
                    progressBarRelativeLayout.setVisibility(View.VISIBLE);
                }
//                mSelectedSubClassList.clear();
                fragmentInteraction.onItemClick(FilterConstant.SUBCLASS, mSelectedSubClassList);
                mSubClassList.clear();
                Log.e(TAG, "fetchFilterHeaderList: headerValue: " + mFilterHeader);
                callAPis();
            } else {
//                offset = mSubClassList.size();
//                setUpOnScrollListener();
            }
    }

    private void setUpOnScrollListener() {
        recyclerFilter.addOnScrollListener(new FilterEndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                try {
                    if (Reusable_Functions.chkStatus(context)) {
                        if (subClassAdapter.getFooter()) {
                            offset += limit;
                            callAPis();
                            subClassAdapter.setfooter(true);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onLoadMore: ", e);
                }
            }
        });
    }

    private void callAPis() {
        if (hasLazyLoading && !hasFilterHeader && mFilterHeader.isEmpty()) {
            callDisplayAPI();
        } else {
            callProductApi();
        }
    }

    private void callProductApi() {
        String[] networkParam = new String[2];
        networkParam[0] = getUrl();
        networkParam[1] = bearerToken;
        ApiHandlerAsyncTask apiHandlerAsyncTask = new ApiHandlerAsyncTask(new ApiResponseHandler(), 0, FilterConstant.REQUEST_TYPE_PRODUCT);
        apiHandlerAsyncTask.execute(networkParam);
    }

    private void callDisplayAPI() {
        String[] networkParam = new String[2];
        networkParam[0] = getDisplayUrl();
        networkParam[1] = bearerToken;
        ApiHandlerAsyncTask apiHandlerAsyncTask = new ApiHandlerAsyncTask(new ApiResponseHandler(), 0, FilterConstant.REQUEST_TYPE_PRODUCT);
        apiHandlerAsyncTask.execute(networkParam);
    }

    private String getUrl() {
        return ConstsCore.web_url + "/v1/display/producthierarchyEDSFNew/"            // producthierarchyDisplayEDSF
                + userId
                + "?level=" + 4
                + (Reusable_Functions.isStringEmpty(mFilterHeader) ? "" : mFilterHeader.replaceAll(" ", "%20"));
    }

    private String getDisplayUrl() {
        return ConstsCore.web_url + "/v1/display/producthierarchyDisplayEDSF/"            // producthierarchyDisplayEDSF
                + userId
                + "?offset=" + offset
                + "&limit=" + limit
                + "&level=" + 4;
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
//                    mSubClassHeaderList.addAll(FilterConstant.parseResponse(response, gson));
//                } else {
//                }
                ArrayList<BrandVendorModel> list;
                if (hasLazyLoading && !hasFilterHeader) {
                    list = FilterConstant.parseResponse(response, gson);
                } else {
                    list = FilterConstant.parseResponse(response, gson, FilterConstant.SUBCLASS_LEVEL);
                }
                ArrayList<BrandVendorModel> temp = new ArrayList<>(mSelectedSubClassList);
                for (BrandVendorModel model : temp) {
//                    int index = FilterConstant.addSelectedModel(model, list, FilterConstant.SUBCLASS);
//                    if (index < 0 && list.size() != limit) {
//                        mSelectedSubClassList.remove(mSelectedSubClassList.indexOf(model)); // removing model if its not in api and resp in less than limit
//                    }
                }
                fragmentInteraction.onItemClick(FilterConstant.SUBCLASS, mSelectedSubClassList);
                mSubClassList.addAll(list);
                subClassAdapter.setfooter(false);
                subClassAdapter.notifyDataSetChanged();
                if (mSubClassList.size() == 0) {
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
