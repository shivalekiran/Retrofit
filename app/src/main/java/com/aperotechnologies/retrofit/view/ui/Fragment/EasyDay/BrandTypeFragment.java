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
import com.aperotechnologies.retrofit.view.adapter.BrandTypeAdapter;
import com.aperotechnologies.retrofit.view.ui.RecyclerItemClickListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aperotechnologies.retrofit.service.FilterConstant.ADAPTER_LIST;
import static com.aperotechnologies.retrofit.service.FilterConstant.FILTER_APPLIED;
import static com.aperotechnologies.retrofit.service.FilterConstant.FILTER_DATA;
import static com.aperotechnologies.retrofit.service.FilterConstant.HAS_LAZY_LOADING;
import static com.aperotechnologies.retrofit.service.FilterConstant.REQUEST_TYPE_PRODUCT;
import static com.aperotechnologies.retrofit.service.FilterConstant.SELECTED_LIST;

/**
 * Created by pamrutkar on 21/05/18.
 */

@SuppressWarnings("ALL")
public class BrandTypeFragment extends Fragment implements NotifyFragmentAdapter {
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
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private Gson gson;
    private ArrayList<BrandVendorModel> mBrandTypeList;
    private ArrayList<BrandVendorModel> mBrandTypeHEaderList;

    private ArrayList<BrandVendorModel> mSelectedBrandTypeList;
    private boolean hasLazyLoading;
    private String mFilterData = "";
    private String mFilterHeader = "";
    private FilterFragmentInteraction fragmentInteraction;
    private boolean hasFilterHeader;
    private int offset = 0;
    private final int limit = 500;
    private BrandTypeAdapter brandTypeAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mBrandTypeList = (ArrayList<BrandVendorModel>) bundle.getSerializable(ADAPTER_LIST);
        mSelectedBrandTypeList = (ArrayList<BrandVendorModel>) bundle.getSerializable(SELECTED_LIST);
        hasLazyLoading = bundle.getBoolean(HAS_LAZY_LOADING, false);
        hasFilterHeader = bundle.getBoolean(FILTER_APPLIED, false);
        mFilterHeader = bundle.getString(FILTER_DATA);
        NetworkAccess();
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

    private void fetchFilterHeaderList(boolean hasFilterHeader, String mFilterHeader)
    {
        if (hasLazyLoading)
        {
            Log.e(TAG, "fetchFilterHeaderList: enabling lazy loading");
            offset = mBrandTypeList.size();
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
                        if (brandTypeAdapter.getFooter()) {
                            offset = offset + limit;
                            callProductApi();
                            brandTypeAdapter.setfooter(true);
                        }
                    }
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
            brandTypeAdapter = new BrandTypeAdapter(context, mBrandTypeList, mSelectedBrandTypeList);
            recyclerFilter.setAdapter(brandTypeAdapter);

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

                    if(!searchData.equals("") || !searchData.isEmpty())
                    {
                        brandTypeAdapter.getFilter().filter(searchData);

                    }
                    else
                    {
                        brandTypeAdapter = new BrandTypeAdapter(context, mBrandTypeList, mSelectedBrandTypeList);
                        recyclerFilter.setAdapter(brandTypeAdapter);

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
                    if(brandTypeAdapter.brandTypeList.size() != mBrandTypeList.size() && searchEditText.getText().toString().length() != 0)
                    {
                        brandTypeAdapter.brandTypeList.get(position).setSelected(!brandTypeAdapter.brandTypeList.get(position).isSelected());
                        if (brandTypeAdapter.brandTypeList.get(position).isSelected()) {
                            mSelectedBrandTypeList.add(brandTypeAdapter.brandTypeList.get(position));
                        } else
                        {
//                            if (mSelectedBrandTypeList.contains(brandTypeAdapter.brandTypeList.get(position))) {
//                                mSelectedBrandTypeList.remove(brandTypeAdapter.brandTypeList.get(position));
//                            }
                            for (BrandVendorModel selectedModel : mSelectedBrandTypeList) {
                                if (selectedModel.getBrandLevel1Code().equals(brandTypeAdapter.brandTypeList.get(position).getBrandLevel1Code()))
                                {
                                    mSelectedBrandTypeList.remove(mSelectedBrandTypeList.indexOf(selectedModel));
                                    break;
                                }
                            }
                        }
                    }
                    else
                    {
                        mBrandTypeList.get(position).setSelected(!mBrandTypeList.get(position).isSelected());
                        if (mBrandTypeList.get(position).isSelected()) {
                            mSelectedBrandTypeList.add(mBrandTypeList.get(position));
                        } else {
//                            if (mSelectedBrandTypeList.contains(mBrandTypeList.get(position))) {
//                                mSelectedBrandTypeList.remove(mBrandTypeList.get(position));
//                            }
                            for (BrandVendorModel selectedModel : mSelectedBrandTypeList) {
                                if (selectedModel.getBrandLevel1Code().equals(mBrandTypeList.get(position).getBrandLevel1Code()))
                                {
                                    mSelectedBrandTypeList.remove(mSelectedBrandTypeList.indexOf(selectedModel));
                                    break;
                                }
                            }
                        }
                    }

                    if (mSelectedBrandTypeList.size() > 0) {
                        mFilterData = "&brandLevel1Code=";
                        for (int i = 0; i < mSelectedBrandTypeList.size(); i++) {
                            if (i == mSelectedBrandTypeList.size() - 1) {
                                mFilterData += mSelectedBrandTypeList.get(i).getBrandLevel3Code();
                            } else {
                                mFilterData += mSelectedBrandTypeList.get(i).getBrandLevel3Code() + ",";
                            }
                        }
                    } else {
                        mFilterData = "";
                    }
                    fragmentInteraction.onItemClick(FilterConstant.BRAND_TYPE, mSelectedBrandTypeList);
                    brandTypeAdapter.notifyDataSetChanged();
                }
                catch (Exception e)
                {
                    Log.e(TAG, "onItemClick: " );
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

    public static Fragment getInstance(Bundle bundle) {
        BrandTypeFragment brandTypeFragment = new BrandTypeFragment();
        brandTypeFragment.setArguments(bundle);
        return brandTypeFragment;
    }

    public void notifyAdapter(ArrayList<BrandVendorModel> list)
    {
        try
        {
            if (list.size() != mBrandTypeList.size()) {
                mBrandTypeList.clear();
                mBrandTypeList.addAll(list);
            }
            mSelectedBrandTypeList.clear();
            mFilterData = "";
            if (brandTypeAdapter != null)
            {
                brandTypeAdapter.notifyDataSetChanged();
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "notifyAdapter: ", e);
        }
    }

    private String getUrl()
    {
        return ConstsCore.web_url + "/v1/display/brandEDSF/"
                + userId
                + "?offset=" + offset
                + "&limit=" + limit
                + "&level=" + 1;
    }

    @SuppressWarnings("unused")
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
                mBrandTypeList.addAll(parseResponse(response));
                brandTypeAdapter.setfooter(false);
                brandTypeAdapter.notifyDataSetChanged();

                if (mBrandTypeList.size() == 0){
                    Toast.makeText(context, "No values to display...", Toast.LENGTH_SHORT).show();
                }
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
