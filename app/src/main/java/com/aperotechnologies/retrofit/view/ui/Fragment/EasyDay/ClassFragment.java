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
import com.aperotechnologies.retrofit.view.adapter.ClassAdapter;
import com.aperotechnologies.retrofit.view.ui.RecyclerItemClickListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aperotechnologies.retrofit.service.FilterConstant.ADAPTER_LIST;
import static com.aperotechnologies.retrofit.service.FilterConstant.CLASS;
import static com.aperotechnologies.retrofit.service.FilterConstant.CLASS_LEVEL;
import static com.aperotechnologies.retrofit.service.FilterConstant.FILTER_APPLIED;
import static com.aperotechnologies.retrofit.service.FilterConstant.FILTER_DATA;
import static com.aperotechnologies.retrofit.service.FilterConstant.FILTER_HEADER_DATA_CHANGE;
import static com.aperotechnologies.retrofit.service.FilterConstant.HAS_LAZY_LOADING;
import static com.aperotechnologies.retrofit.service.FilterConstant.REQUEST_TYPE_PRODUCT;
import static com.aperotechnologies.retrofit.service.FilterConstant.SELECTED_LIST;

/**
 * Created by pamrutkar on 21/05/18.
 */

@SuppressWarnings("ALL")
public class ClassFragment extends Fragment implements NotifyFragmentAdapter {
    private final String TAG = ClassFragment.class.getSimpleName();
    private Context context;
    private String userId;
    private String bearerToken;
    private String geoLevel2Code;
    private String lobId;

    @BindView(R.id.recycler_filter)
    public
    RecyclerView recyclerFilter;

    @BindView(R.id.serchEditText)
    public
    EditText searchEditText;

    @BindView(R.id.progressBarRelativeLayout)
    public RelativeLayout progressBarRelativeLayout;

    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private Gson gson;
    private ArrayList<BrandVendorModel> mClassList;
    //    private ArrayList<BrandVendorModel> mClassHeaderList;
    private ArrayList<BrandVendorModel> mSelectedClassList;
    private boolean hasLazyLoading;
    private String mFilterHeader = "";
    private FilterFragmentInteraction fragmentInteraction;
    private boolean hasFilterHeader;
    private int offset = 500;
    private final int limit = 500;
    private boolean mHeaderChange = false;
    private ClassAdapter classAdapter;

    public ClassFragment() {
    }

    public static Fragment getInstance(Bundle bundle) {
        ClassFragment classFragment = new ClassFragment();
        classFragment.setArguments(bundle);
        return classFragment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle bundle = getArguments();
            mClassList = (ArrayList<BrandVendorModel>) bundle.getSerializable(ADAPTER_LIST);
            mSelectedClassList = (ArrayList<BrandVendorModel>) bundle.getSerializable(SELECTED_LIST);
            mHeaderChange = bundle.getBoolean(FILTER_HEADER_DATA_CHANGE);
            hasLazyLoading = bundle.getBoolean(HAS_LAZY_LOADING);
            hasFilterHeader = bundle.getBoolean(FILTER_APPLIED, false);
            mFilterHeader = bundle.getString(FILTER_DATA);
//        mClassHeaderList = new ArrayList<>();
            NetworkAccess();
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e);
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
            Log.e(TAG, "onViewCreated: ", e);
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

        classAdapter = new ClassAdapter(context, mClassList, mSelectedClassList);
        recyclerFilter.setAdapter(classAdapter);

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
                    if (!searchData.equals("") || !searchData.isEmpty()) {
                        classAdapter.getFilter().filter(searchData);

                    } else {
                        classAdapter = new ClassAdapter(context, mClassList, mSelectedClassList);
                        recyclerFilter.setAdapter(classAdapter);
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
                    if (classAdapter.classList.size() != mClassList.size() && searchEditText.getText().toString().length() != 0) {
                        classAdapter.classList.get(position).setSelected(!classAdapter.classList.get(position).isSelected());
                        if (classAdapter.classList.get(position).isSelected()) {
                            mSelectedClassList.add(classAdapter.classList.get(position));
                        } else {
                            for (BrandVendorModel selectedModel : mSelectedClassList) {
                                if (selectedModel.getProdLevel4Code().equals(classAdapter.classList.get(position).getProdLevel4Code()) ||
                                        selectedModel.getProdLevel4Desc().equals(classAdapter.classList.get(position).getProdLevel4Desc())) {
                                    mSelectedClassList.remove(mSelectedClassList.indexOf(selectedModel));
                                    break;
                                }
                            }

                        }
                    } else {
                        mClassList.get(position).setSelected(!mClassList.get(position).isSelected());
                        if (mClassList.get(position).isSelected()) {
                            mSelectedClassList.add(mClassList.get(position));
                        } else {
                            for (BrandVendorModel selectedModel : mSelectedClassList) {
                                if (selectedModel.getProdLevel4Code().equals(mClassList.get(position).getProdLevel4Code()) ||
                                        selectedModel.getProdLevel4Desc().equals(mClassList.get(position).getProdLevel4Desc())) {
                                    mSelectedClassList.remove(mSelectedClassList.indexOf(selectedModel));
                                    break;
                                }
                            }

                        }

                    }

                    classAdapter.notifyDataSetChanged();
                    fragmentInteraction.onItemClick(CLASS, mSelectedClassList);
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
        String[] hierarchyList = hierarchyLevels.split(",");
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
            if (list != mClassList) {
                Log.e(TAG, "notifyAdapter: not equal");
            }
            if (list.size() != mClassList.size()) {
                mClassList.clear();
                mClassList.addAll(list);
            }
            mSelectedClassList.clear();
            if (classAdapter != null) {
                classAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e(TAG, "notifyAdapter: ", e);
        }
    }

    private void fetchFilterHeaderList(boolean hasFilterHeader, String mFilterHeader) {
        if (hasLazyLoading) {
            Log.e(TAG, "fetchFilterHeaderList: enabling lazy loading");
            offset = mClassList.size();
            setUpOnScrollListener();
        }
        if (mFilterHeader != null && mFilterHeader.length() > 0)
            if (hasFilterHeader && !mFilterHeader.isEmpty() && (mHeaderChange || mClassList.size() == 0)) {
                offset = 0;
                if (progressBarRelativeLayout != null) {
                    progressBarRelativeLayout.setVisibility(View.VISIBLE);
                }
//                mSelectedClassList.clear();
                fragmentInteraction.onItemClick(CLASS, mSelectedClassList);
                mClassList.clear();
                Log.e(TAG, "fetchFilterHeaderList: headerValue: " + mFilterHeader);
                callAPis();
            } else {
//                offset = mClassList.size();
//                setUpOnScrollListener();
            }
    }

    private void setUpOnScrollListener() {
        recyclerFilter.addOnScrollListener(new FilterEndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                try {
                    if (Reusable_Functions.chkStatus(context)) {
                        if (classAdapter.getFooter()) {
                            offset = offset + limit;
                            callAPis();
                            classAdapter.setfooter(true);
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
            callDisplayApi();
        } else {
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

    private void callDisplayApi() {
        String[] networkParam = new String[2];
        networkParam[0] = getDisplayUrl();
        networkParam[1] = bearerToken;
        ApiHandlerAsyncTask apiHandlerAsyncTask = new ApiHandlerAsyncTask(new ApiResponseHandler(), 0, REQUEST_TYPE_PRODUCT);
        apiHandlerAsyncTask.execute(networkParam);
    }

    private String getDisplayUrl() {
        String url = "";
        try {
            url = ConstsCore.web_url + "/v1/display/producthierarchyDisplayEDSF/"            // producthierarchyEDSFNew
                    + userId
                    + "?level=" + 3
                    + "&offset=" + offset
                    + "&limit=" + limit  ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    private String getUrl() {
        String url = "";
        try {
            url = ConstsCore.web_url + "/v1/display/producthierarchyEDSFNew/"            // producthierarchyDisplayEDSF
                    + userId
                    + "?level=" + 3  
                    + (Reusable_Functions.isStringEmpty(mFilterHeader) ? "" : mFilterHeader.replace(" ", "%20"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
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
//                    mClassHeaderList.addAll(FilterConstant.parseResponse(response, gson));
//                } else {
//                }

                ArrayList<BrandVendorModel> list;
                if (hasLazyLoading && !hasFilterHeader) {
                    list = FilterConstant.parseResponse(response, gson);
                } else {
                    list = FilterConstant.parseResponse(response, gson, CLASS_LEVEL);
                }
                ArrayList<BrandVendorModel> temp = new ArrayList<>(mSelectedClassList);
                for (BrandVendorModel model : temp) {
//                    int index = FilterConstant.addSelectedModel(model, list, CLASS);
//                    if (index < 0 && list.size() != limit) {
//                        mSelectedClassList.remove(mSelectedClassList.indexOf(model)); // removing model if its not in api and resp in less than limit
//                    }
                }
                fragmentInteraction.onItemClick(CLASS, mSelectedClassList);
                mClassList.addAll(list);
                classAdapter.setfooter(false);
                classAdapter.notifyDataSetChanged();
                if (mClassList.size() == 0) {
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
