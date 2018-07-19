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
import com.aperotechnologies.retrofit.view.adapter.DeptAdapter;
import com.aperotechnologies.retrofit.view.ui.RecyclerItemClickListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by pamrutkar on 21/05/18.
 */

@SuppressWarnings("ALL")
public class DeptFragment extends Fragment implements NotifyFragmentAdapter {
    private final String TAG = this.getClass().getName();

    @BindView(R.id.recycler_filter)
    RecyclerView recyclerFilter;

    @BindView(R.id.serchEditText)
    EditText searchEditText;

    private Context mContext;
    private String userId, bearerToken, geoLevel2Code, lobId;
    private SharedPreferences sharedPreferences;
    private String[] hierarchyList;
    private RequestQueue queue;
    private Gson gson;

    private DeptAdapter deptAdapter;
    private ArrayList<BrandVendorModel> mDepartmentList;
    private ArrayList<BrandVendorModel> mDepartmentHeaderList;
    private ArrayList<BrandVendorModel> mSelectedDepartmentList;
    private boolean hasLazyLoading;
    private String mFilterData = "";
    private String mFilterHeader = "";
    private FilterFragmentInteraction fragmentInteraction;
    private boolean hasFilterHeader;
    private int offset = 500;
    private final int limit = 500;

    public DeptFragment() {
    }

    public static Fragment getInstance(Bundle bundle) {
        DeptFragment deptFragment = new DeptFragment();
        deptFragment.setArguments(bundle);
        return deptFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mDepartmentList = (ArrayList<BrandVendorModel>) bundle.getSerializable(FilterConstant.ADAPTER_LIST);
        mSelectedDepartmentList = (ArrayList<BrandVendorModel>) bundle.getSerializable(FilterConstant.SELECTED_LIST);
        hasLazyLoading = bundle.getBoolean(FilterConstant.HAS_LAZY_LOADING, false);
        hasFilterHeader = bundle.getBoolean(FilterConstant.FILTER_APPLIED, false);
        mFilterHeader = bundle.getString(FilterConstant.FILTER_DATA);
        networkAccess();
    }

    private void fetchFilterHeaderList(boolean hasFilterHeader, String mFilterHeader) {
        if (hasLazyLoading) {
            Log.e(TAG, "fetchFilterHeaderList: enabling lazy loading");
            offset = mDepartmentList.size();
            setUpOnScrollListener();
        }
        if (mFilterHeader != null)
            if (hasFilterHeader && !mFilterHeader.isEmpty()) {
                offset = 0;
                Log.e(TAG, "fetchFilterHeaderList: headerValue: " + mFilterHeader);
                callProductApi();
            }
    }

    private void setUpOnScrollListener() {
        recyclerFilter.addOnScrollListener(new FilterEndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                try {
                    if (Reusable_Functions.chkStatus(mContext)) {
                        if (deptAdapter.getFooter()) {
                            offset = offset + limit;
                            callProductApi();
                            deptAdapter.setfooter(true);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onLoadMore: ", e);
                }
            }
        });
    }

    private void callProductApi() {
        String[] networkParam = new String[2];
        networkParam[0] = getUrl();
        networkParam[1] = bearerToken;
        ApiHandlerAsyncTask apiHandlerAsyncTask = new ApiHandlerAsyncTask(new ApiResponseHandler(), 0, FilterConstant.REQUEST_TYPE_PRODUCT);
        apiHandlerAsyncTask.execute(networkParam);
    }

    private String getUrl() {
        return ConstsCore.web_url + "/v1/display/producthierarchyDisplayEDSF/"
                + userId
//                + "?offset=" + offset
//                + "&limit=" + limit
                + "?level=" + 1;
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
        initialise_ui(view);
        fetchFilterHeaderList(hasFilterHeader, mFilterHeader);
    }

    private void initialise_ui(View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerFilter.setLayoutManager(layoutManager);

        recyclerFilter.setHasFixedSize(true);
        if (hasFilterHeader) {
            deptAdapter = new DeptAdapter(mContext, mDepartmentHeaderList, mSelectedDepartmentList);
        } else {
            deptAdapter = new DeptAdapter(mContext, mDepartmentList, mSelectedDepartmentList);
        }


        recyclerFilter.setAdapter(deptAdapter);
        deptAdapter.notifyDataSetChanged();


//        recyclerFilter.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));


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
                String searchData = "";
                Log.e("s :", "" + searchData);
                try {

                    searchData = searchEditText.getText().toString();
                    Log.e("s :", "" + searchData);
                    if (!searchData.isEmpty() || !searchData.equals("")) {
                        deptAdapter.getFilter().filter(searchData);
                    } else {
                        deptAdapter = new DeptAdapter(mContext, mDepartmentList, mSelectedDepartmentList);
                        recyclerFilter.setAdapter(deptAdapter);
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
                    if (deptAdapter.dept_List.size() != mDepartmentList.size() && searchEditText.getText().toString().length() != 0) {
                        deptAdapter.dept_List.get(position).setSelected(!deptAdapter.dept_List.get(position).isSelected());
                        if (deptAdapter.dept_List.get(position).isSelected()) {
                            mSelectedDepartmentList.add(deptAdapter.dept_List.get(position));
                            //mFilterData += mDepartmentList.get(position).getProdLevel2Code() + ",";
                        } else {
                            for (BrandVendorModel selectedModel : mSelectedDepartmentList) {
                                if (selectedModel.getProdLevel2Code().equals(deptAdapter.dept_List.get(position).getProdLevel2Code()) ||
                                        selectedModel.getProdLevel2Desc().equals(deptAdapter.dept_List.get(position).getProdLevel2Desc())) {
                                    mSelectedDepartmentList.remove(mSelectedDepartmentList.indexOf(selectedModel));
                                    break;
                                }
                            }

                        }
                    } else {
                        mDepartmentList.get(position).setSelected(!mDepartmentList.get(position).isSelected());
                        if (mDepartmentList.get(position).isSelected()) {
                            mSelectedDepartmentList.add(mDepartmentList.get(position));
                            //mFilterData += mDepartmentList.get(position).getProdLevel2Code() + ",";
                        } else {
                            for (BrandVendorModel selectedModel : mSelectedDepartmentList) {
                                if (selectedModel.getProdLevel2Code().equals(mDepartmentList.get(position).getProdLevel2Code()) ||
                                        selectedModel.getProdLevel2Desc().equals(mDepartmentList.get(position).getProdLevel2Desc())) {
                                    mSelectedDepartmentList.remove(mSelectedDepartmentList.indexOf(selectedModel));
                                    break;
                                }
                            }
//                        if (mSelectedDepartmentList.contains(mDepartmentList.get(position))) {
//                            mSelectedDepartmentList.remove(mDepartmentList.get(position));
//                            mFilterData.replaceAll(mDepartmentList.get(position).getProdLevel2Code() + ",", "");
//                        }
                        }

                    }

                    if (mSelectedDepartmentList.size() > 0) {
                        mFilterData = "&prodLevel2Code=";
                        for (int i = 0; i < mSelectedDepartmentList.size(); i++) {
                            if (i == mSelectedDepartmentList.size() - 1) {
                                mFilterData += mSelectedDepartmentList.get(i).getProdLevel2Code();
                            } else {
                                mFilterData += mSelectedDepartmentList.get(i).getProdLevel2Code() + ",";
                            }
                        }
                    } else {
                        mFilterData = "";
                    }
                    fragmentInteraction.onItemClick(FilterConstant.DEPT, mSelectedDepartmentList);
                    deptAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e(TAG, "onItemClick: ", e);
                    Toast.makeText(getActivity(), "Error...", Toast.LENGTH_SHORT).show();
                }
            }
        }));

    }

    private void networkAccess() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        userId = sharedPreferences.getString("userId", "");
        bearerToken = sharedPreferences.getString("bearerToken", "");
        geoLevel2Code = sharedPreferences.getString("concept", "");
        String hierarchyLevels = sharedPreferences.getString("hierarchyLevels", "");
        hierarchyList = hierarchyLevels.split(",");
        Log.e(TAG, "networkAccess:  Hierarchy List: " + hierarchyLevels);
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
        this.mContext = context;
        if (context instanceof FilterFragmentInteraction)
            this.fragmentInteraction = (FilterFragmentInteraction) context;
        else
            throw new RuntimeException("Please implement FilterFragmentInteraction interface...");
    }

    @Override
    public void notifyAdapter(ArrayList<BrandVendorModel> models) {
        try {
            if (models.size() != mDepartmentList.size()) {
                mDepartmentList.clear();
                mDepartmentList.addAll(models);
            }
            mSelectedDepartmentList.clear();
            mFilterData = "";
            if (deptAdapter != null) {
                deptAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e(TAG, "notifyAdapter: ", e);
        }
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
                if (hasFilterHeader) {
                    mDepartmentHeaderList.addAll(FilterConstant.parseResponse(response, gson));
                } else {
                    mDepartmentList.addAll(FilterConstant.parseResponse(response, gson));
                }
                deptAdapter.setfooter(false);
                deptAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
