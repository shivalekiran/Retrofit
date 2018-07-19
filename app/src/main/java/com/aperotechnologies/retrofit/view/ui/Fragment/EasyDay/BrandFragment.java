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
import com.aperotechnologies.retrofit.view.adapter.BrandAdapter;
import com.aperotechnologies.retrofit.view.ui.RecyclerItemClickListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aperotechnologies.retrofit.service.FilterConstant.ADAPTER_LIST;
import static com.aperotechnologies.retrofit.service.FilterConstant.BRAND;
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
public class BrandFragment extends Fragment implements NotifyFragmentAdapter {
    private final String TAG = BrandFragment.class.getSimpleName();
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
    private RequestQueue queue;
    private Gson gson;
    private ArrayList<BrandVendorModel> mBrandList;
    private ArrayList<BrandVendorModel> mSelectedBrandList;
    private boolean hasLazyLoading;
    private String mFilterData = "";
    private String mFilterHeader = "";
    private FilterFragmentInteraction fragmentInteraction;
    private boolean hasFilterHeader;
    private int offset = 0;
    private final int limit = 500;
    private BrandAdapter brandAdapter;
    private boolean mHeaderChange = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle bundle = getArguments();
            mBrandList = (ArrayList<BrandVendorModel>) bundle.getSerializable(ADAPTER_LIST);
            mSelectedBrandList = (ArrayList<BrandVendorModel>) bundle.getSerializable(SELECTED_LIST);
            hasLazyLoading = bundle.getBoolean(HAS_LAZY_LOADING, false);
            hasFilterHeader = bundle.getBoolean(FILTER_APPLIED, false);
            mFilterHeader = bundle.getString(FILTER_DATA);
            mHeaderChange = bundle.getBoolean(FILTER_HEADER_DATA_CHANGE);
            NetworkAccess();
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
        initialise_ui(view);
        fetchFilterHeaderList(hasFilterHeader, mFilterHeader);
    }


    private void fetchFilterHeaderList(boolean hasFilterHeader, String mFilterHeader) {
        if (hasLazyLoading) {
            Log.e(TAG, "fetchFilterHeaderList: enabling lazy loading");
            offset = mBrandList.size();
            setUpOnScrollListener();
        }
        if (mFilterHeader != null)
            if (hasFilterHeader && !mFilterHeader.isEmpty() && (mHeaderChange || mBrandList.size() == 0)) {
                if (progressBarRelativeLayout != null) {
                    progressBarRelativeLayout.setVisibility(View.VISIBLE);
                }
                offset = 0;
                mBrandList.clear();
//                mSelectedBrandList.clear();
                fragmentInteraction.onItemClick(BRAND, mSelectedBrandList);
                Log.e(TAG, "fetchFilterHeaderList: headerValue: " + mFilterHeader);
                callProductApi();
            } else {
                offset = mBrandList.size();
                setUpOnScrollListener();
            }


    }

    private void setUpOnScrollListener() {
        recyclerFilter.addOnScrollListener(new FilterEndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                try {
                    if (Reusable_Functions.chkStatus(context)) {
                        if (!brandAdapter.getFooter()) {
                            offset = offset + limit;
                            callProductApi();
                            brandAdapter.setfooter(true);
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
        ApiHandlerAsyncTask apiHandlerAsyncTask = new ApiHandlerAsyncTask(new ApiResponseHandler(), 0, REQUEST_TYPE_PRODUCT);
        apiHandlerAsyncTask.execute(networkParam);
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
        brandAdapter = new BrandAdapter(context, mBrandList, mSelectedBrandList);
        recyclerFilter.setAdapter(brandAdapter);
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
                    if(!searchData.equals(" ") || !searchData.isEmpty())
                    {
                        brandAdapter.getFilter().filter(searchData);

                    }
                    else
                    {
                        brandAdapter = new BrandAdapter(context, mBrandList, mSelectedBrandList);
                        recyclerFilter.setAdapter(brandAdapter);
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
                    if(brandAdapter.brandList.size() != mBrandList.size() && searchEditText.getText().toString().length() != 0)
                    {
                        brandAdapter.brandList.get(position).setSelected(!brandAdapter.brandList.get(position).isSelected());
                        if (brandAdapter.brandList.get(position).isSelected()) {
                            mSelectedBrandList.add(brandAdapter.brandList.get(position));
                        }
                        else
                        {
                            for (BrandVendorModel selectedModel : mSelectedBrandList) {
                                if (selectedModel.getBrandLevel3Code().equals(brandAdapter.brandList.get(position).getBrandLevel3Code()) ||
                                        selectedModel.getBrandLevel3Desc().equals(brandAdapter.brandList.get(position).getBrandLevel3Desc()) ) {
                                    mSelectedBrandList.remove(mSelectedBrandList.indexOf(selectedModel));
                                    break;
                                }
                            }
//                            if (mSelectedBrandList.contains(brandAdapter.brandList.get(position))) {
//                                mSelectedBrandList.remove(brandAdapter.brandList.get(position));
//                            }
                        }
                    }
                    else
                    {
                        mBrandList.get(position).setSelected(!mBrandList.get(position).isSelected());
                        if (mBrandList.get(position).isSelected()) {
                            mSelectedBrandList.add(mBrandList.get(position));
                        }
                        else
                        {
                            for (BrandVendorModel selectedModel : mSelectedBrandList) {
                                if (selectedModel.getBrandLevel3Code().equals(mBrandList.get(position).getBrandLevel3Code()) ||
                                        selectedModel.getBrandLevel3Desc().equals(mBrandList.get(position).getBrandLevel3Desc()) ) {
                                    mSelectedBrandList.remove(mSelectedBrandList.indexOf(selectedModel));
                                    break;
                                }
                            }
//                            if (mSelectedBrandList.contains(mBrandList.get(position))) {
//                                mSelectedBrandList.remove(mBrandList.get(position));
//                            }
                        }
                    }


                    if (mSelectedBrandList.size() > 0) {
                        mFilterData = "&brandLevel3Code=";
                        for (int i = 0; i < mSelectedBrandList.size(); i++) {
                            if (i == mSelectedBrandList.size() - 1) {
                                mFilterData += mSelectedBrandList.get(i).getBrandLevel3Code();
                            } else {
                                mFilterData += mSelectedBrandList.get(i).getBrandLevel3Code() + ",";
                            }
                        }
                    } else {
                        mFilterData = "";
                    }
                    fragmentInteraction.onItemClick(BRAND, mSelectedBrandList);

                    brandAdapter.notifyDataSetChanged();
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
        String[] hierarchyList = hierarchyLevels.split(",");
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
        if (context instanceof FilterFragmentInteraction)
            this.fragmentInteraction = (FilterFragmentInteraction) context;
        else
            throw new RuntimeException("Please implement FilterFragmentInteraction interface...");

    }

    public static Fragment getInstance(Bundle bundle) {
        BrandFragment brandFragment = new BrandFragment();
        brandFragment.setArguments(bundle);
        return brandFragment;
    }

    public void notifyAdapter(ArrayList<BrandVendorModel> list) {
        try {
            if (list.size() != mBrandList.size()) {
                mBrandList.clear();
                mBrandList.addAll(list);
            }
            mSelectedBrandList.clear();
            mFilterData = "";
            if (brandAdapter != null) {
                brandAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e(TAG, "notifyAdapter: ", e);
        }
    }

    private String getUrl() {
        return ConstsCore.web_url + "/v1/display/brandEDSF/"
                + userId
                + "?offset=" + offset
                + (Reusable_Functions.isStringEmpty(mFilterHeader) ? "" : mFilterHeader.replaceAll(" ", "%20"))
                + "&level=" + 2;
    }

    @SuppressWarnings("unused")
    @SuppressLint("HandlerLeak")
    private class ApiResponseHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                String response = (String) msg.obj;
                int level = msg.arg1;
                int requestType = msg.arg2;
                ArrayList<BrandVendorModel> list = parseResponse(response);
                ArrayList<BrandVendorModel> temp = new ArrayList<>(mSelectedBrandList);
                for (BrandVendorModel model :temp ) {
//                    int index = FilterConstant.addSelectedModel(model, list, BRAND);
//                    if (index < 0 && list.size() != limit) {
//                        mSelectedBrandList.remove(mSelectedBrandList.indexOf(model)); // removing model if its not in api and resp in less than limit
//                    }
                }
                fragmentInteraction.onItemClick(BRAND, mSelectedBrandList);
                mBrandList.addAll(list);
                brandAdapter.setfooter(false);
                brandAdapter.notifyDataSetChanged();
                if (mBrandList.size() == 0){
                    Toast.makeText(context, "No values to display...", Toast.LENGTH_SHORT).show();
                }
                if (progressBarRelativeLayout != null) {
                    progressBarRelativeLayout.setVisibility(View.GONE);
                }
                if (mBrandList.size() == 0){
                    Toast.makeText(context, "No values to display...", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (progressBarRelativeLayout != null) {
                    progressBarRelativeLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    private ArrayList<BrandVendorModel> parseResponse(String response) {
        ArrayList<BrandVendorModel> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(response);
            if (array != null || array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
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
