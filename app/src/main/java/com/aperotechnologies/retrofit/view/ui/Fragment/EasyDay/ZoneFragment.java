package com.aperotechnologies.retrofit.view.ui.Fragment.EasyDay;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.aperotechnologies.retrofit.service.FilterConstant;
import com.aperotechnologies.retrofit.service.FilterFragmentInteraction;
import com.aperotechnologies.retrofit.service.NotifyFragmentAdapter;
import com.aperotechnologies.retrofit.service.model.BrandVendorModel;
import com.aperotechnologies.retrofit.view.adapter.ZoneAdapter;
import com.aperotechnologies.retrofit.view.ui.RecyclerItemClickListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pamrutkar on 21/05/18.
 */
@SuppressWarnings("ALL")
public class ZoneFragment extends Fragment implements NotifyFragmentAdapter
{

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
    private String[] hierarchyList;
    private RequestQueue queue;
    private Gson gson;
    private ArrayList<BrandVendorModel> mZoneList;
    private ArrayList<BrandVendorModel> mSelectedZoneList;
    private boolean hasLazyLoading; //todo offset and limit will not given and all data will come without passing offset and limit param , lazy loading is not applicable for location hierarchy
    private String mFilterData = "";
    private String mFilterHeader = "";
    private boolean hasFilterHeader;

    private FilterFragmentInteraction fragmentInteraction;
    private ZoneAdapter zoneAdapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mZoneList = (ArrayList<BrandVendorModel>) bundle.getSerializable(FilterConstant.ADAPTER_LIST);
        mSelectedZoneList = (ArrayList<BrandVendorModel>) bundle.getSerializable(FilterConstant.SELECTED_LIST);
        hasLazyLoading = bundle.getBoolean(FilterConstant.HAS_LAZY_LOADING,false);
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
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerFilter.setLayoutManager(layoutManager);
        recyclerFilter.setHasFixedSize(true);
        zoneAdapter = new ZoneAdapter(context, mZoneList, mSelectedZoneList);
        recyclerFilter.setAdapter(zoneAdapter);
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
                        zoneAdapter.getFilter().filter(searchData);

                    }
                    else
                    {
                        zoneAdapter = new ZoneAdapter(context,mZoneList,mSelectedZoneList);
                        recyclerFilter.setAdapter(zoneAdapter);
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
                    if(zoneAdapter.zoneList.size() != mZoneList.size() && searchEditText.getText().toString().length() != 0)
                    {
                        zoneAdapter.zoneList.get(position).setSelected(!zoneAdapter.zoneList.get(position).isSelected());
                        if (zoneAdapter.zoneList.get(position).isSelected()) {
                            mSelectedZoneList.add(zoneAdapter.zoneList.get(position));
                        } else {
                            for (BrandVendorModel selectedModel : mSelectedZoneList) {
                                if (selectedModel.getZoneED().equals(zoneAdapter.zoneList.get(position).getZoneED()))
                                {
                                    mSelectedZoneList.remove(mSelectedZoneList.indexOf(selectedModel));
                                    break;
                                }
                            }
//                            if (mSelectedZoneList.contains(zoneAdapter.zoneList.get(position))) {
//                                mSelectedZoneList.remove(zoneAdapter.zoneList.get(position));
//                            }
                        }
                    }
                    else
                    {
                        mZoneList.get(position).setSelected(!mZoneList.get(position).isSelected());
                        if (mZoneList.get(position).isSelected()) {
                            mSelectedZoneList.add(mZoneList.get(position));
                        } else {
                            for (BrandVendorModel selectedModel : mSelectedZoneList) {
                                if (selectedModel.getZoneED().equals(mZoneList.get(position).getZoneED()))
                                {
                                    mSelectedZoneList.remove(mSelectedZoneList.indexOf(selectedModel));
                                    break;
                                }
                            }
                        }
                    }


                    if (mSelectedZoneList.size() > 0) {
                        mFilterData = "&zoneED=";
                        for (int i = 0; i < mSelectedZoneList.size(); i++)
                        {
                            if (i == mSelectedZoneList.size() - 1)
                            {
                                mFilterData += mSelectedZoneList.get(i).getZoneED();
                            }
                            else
                            {
                                mFilterData += mSelectedZoneList.get(i).getZoneED() + ",";
                            }
                        }
                    } else {
                        mFilterData = "";
                    }
                    fragmentInteraction.onItemClick(FilterConstant.ZONE, mSelectedZoneList);
                    zoneAdapter.notifyDataSetChanged();
                }catch (Exception e)
                {
                    Log.e(TAG, "onItemClick: ", e);

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
            throw new RuntimeException("Please implement FilterFragmentInteraction interface...");

    }

    public static Fragment getInstance(Bundle bundle)
    {
        ZoneFragment zoneFragment = new ZoneFragment();
        zoneFragment.setArguments(bundle);
        return zoneFragment;
    }

    @Override
    public void notifyAdapter(ArrayList<BrandVendorModel> list)
    {
        try
        {
            if (list.size() != mZoneList.size()) {
                mZoneList.clear();
                mZoneList.addAll(list);
            }
            mSelectedZoneList.clear();
            mFilterData = "";
            if (zoneAdapter != null)
            {
                zoneAdapter.notifyDataSetChanged();
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "notifyAdapter: ", e);
        }

    }
}
