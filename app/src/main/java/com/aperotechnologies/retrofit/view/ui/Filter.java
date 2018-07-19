package com.aperotechnologies.retrofit.view.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.aperotechnologies.retrofit.R;
import com.aperotechnologies.retrofit.service.FilterConstant;
import com.aperotechnologies.retrofit.service.FilterFragmentInteraction;
import com.aperotechnologies.retrofit.service.OnFilterDataFetchComplete;
import com.aperotechnologies.retrofit.service.Reusable_Functions;
import com.aperotechnologies.retrofit.service.di.LocationFactory;
import com.aperotechnologies.retrofit.service.model.BrandVendorModel;
import com.aperotechnologies.retrofit.service.model.FilterAppliedData;
import com.aperotechnologies.retrofit.service.model.ProductHierarchyModel;
import com.aperotechnologies.retrofit.view.adapter.HierarchyAdapter;
import com.aperotechnologies.retrofit.view.ui.Fragment.EasyDay.BrandFragment;
import com.aperotechnologies.retrofit.view.ui.Fragment.EasyDay.BrandTypeFragment;
import com.aperotechnologies.retrofit.view.ui.Fragment.EasyDay.ClassFragment;
import com.aperotechnologies.retrofit.view.ui.Fragment.EasyDay.DeptFragment;
import com.aperotechnologies.retrofit.view.ui.Fragment.EasyDay.DistrictFragment;
import com.aperotechnologies.retrofit.view.ui.Fragment.EasyDay.MCFragment;
import com.aperotechnologies.retrofit.view.ui.Fragment.EasyDay.MarketFragment;
import com.aperotechnologies.retrofit.view.ui.Fragment.EasyDay.StoreFragment;
import com.aperotechnologies.retrofit.view.ui.Fragment.EasyDay.SubClassFragment;
import com.aperotechnologies.retrofit.view.ui.Fragment.EasyDay.SubDeptFragment;
import com.aperotechnologies.retrofit.view.ui.Fragment.EasyDay.VendorFragment;
import com.aperotechnologies.retrofit.view.ui.Fragment.EasyDay.ZoneFragment;
import com.aperotechnologies.retrofit.viewmodel.LocationViewModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;
import static com.aperotechnologies.retrofit.service.FilterConstant.ADAPTER_LIST;
import static com.aperotechnologies.retrofit.service.FilterConstant.BRAND;
import static com.aperotechnologies.retrofit.service.FilterConstant.BRAND_TYPE;
import static com.aperotechnologies.retrofit.service.FilterConstant.CHECK_FROM;
import static com.aperotechnologies.retrofit.service.FilterConstant.CLASS;
import static com.aperotechnologies.retrofit.service.FilterConstant.DEPT;
import static com.aperotechnologies.retrofit.service.FilterConstant.DISTRICT;
import static com.aperotechnologies.retrofit.service.FilterConstant.FILTERED_APPLIED;
import static com.aperotechnologies.retrofit.service.FilterConstant.FILTER_APPLIED;
import static com.aperotechnologies.retrofit.service.FilterConstant.FILTER_CLEARED;
import static com.aperotechnologies.retrofit.service.FilterConstant.FILTER_DATA;
import static com.aperotechnologies.retrofit.service.FilterConstant.FILTER_HEADER_DATA_CHANGE;
import static com.aperotechnologies.retrofit.service.FilterConstant.FILTER_LEVEL;
import static com.aperotechnologies.retrofit.service.FilterConstant.FILTER_SELECTED;
import static com.aperotechnologies.retrofit.service.FilterConstant.HAS_LAZY_LOADING;
import static com.aperotechnologies.retrofit.service.FilterConstant.HEADER_VALUES;
import static com.aperotechnologies.retrofit.service.FilterConstant.MARKET;
import static com.aperotechnologies.retrofit.service.FilterConstant.MC;
import static com.aperotechnologies.retrofit.service.FilterConstant.PVA_PRODUCT;
import static com.aperotechnologies.retrofit.service.FilterConstant.SELECTED_LIST;
import static com.aperotechnologies.retrofit.service.FilterConstant.STORE;
import static com.aperotechnologies.retrofit.service.FilterConstant.SUBCLASS;
import static com.aperotechnologies.retrofit.service.FilterConstant.SUBDEPT;
import static com.aperotechnologies.retrofit.service.FilterConstant.VENDOR;
import static com.aperotechnologies.retrofit.service.FilterConstant.ZONE;

/**
 * Created by pamrutkar on 17/05/18.
 */

@SuppressWarnings("ALL")
public class Filter extends FilterDataClass implements FilterFragmentInteraction, OnFilterDataFetchComplete {
    private static final String TAG = Filter.class.getName();

    @BindView(R.id.recycler_Filter)
    RecyclerView recyclerViewFilter;

    @BindView(R.id.button_clearAll)
    Button lin_clearAll;

    @BindView(R.id.button_close)
    Button mCloseButton;

    @BindView(R.id.button_apply)
    Button mApplyFilterButton;

    @BindView(R.id.progressBarRelativeLayout)
    public RelativeLayout progressBarRelativeLayout;

    private String userId, bearerToken, geoLevel2Code, lobId, storeCode;
    private ArrayList<String> mHierarchyArrayList;
    private ArrayList<Integer> mHierarchySelectCount;
    private ArrayList<Boolean> mHierarchySetBackgroundList;
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private Gson gson;
    private HierarchyAdapter mHierarchyAdapter;
    private ArrayList<ProductHierarchyModel> productHierarchyModels;
    //    private UserDetails userDetails;
    private FilterAppliedData mFilterSelectedData;
    private String mFrom;
    //    private FilterSharedPreferences mFilterSharedPreferences;
    private ArrayList<BrandVendorModel> mDepartmentList, mSubDeptList, mClassList, mSubClassList, mMCList, mZoneList, mMarketList, mDistrictList, mStoreList, mBrandTypeList, mBrandList, mVendorList, mPvAProductsList;
    private ArrayList<BrandVendorModel> mDepartmentAPIList, mSubDeptAPIList, mClassAPIList, mSubClassAPIList, mMCAPIList, mZoneAPIList, mMarketAPIList, mDistrictAPIList, mStoreAPIList, mBrandTypeAPIList, mBrandAPIList, mVendorAPIList, mPvAProductsAPIList;
    private String mFilterData = "";
    private String mHierarchySelected;
    private FragmentTransaction mFragmentTransaction;
    private FragmentManager manager;
    private Fragment mLastAddedFragment;
    private boolean mFilterRest = false;
    private int mFilterLevel;
    private String view;
    private String mProductHeader = "";
    private String mLocationHeader = "";
    private String mSubDeptPreviousHeader = "", mClassPreviousHeader = "", mSubClassPreviousHeader = "", mMCPreviousHeader = "", mBrandPreviousHeader = "";
    private String mBradTypeHeader = "";
    private String mMarketPreviousHeader = "", mDistrictPreviousHeader = "", mStorePreviousHeaer = "";
    private String bearertoken;
    private static int retry = 0;
    private boolean filterStart = true;
    private AlertDialog dialog;
    private String previousSelected;
    private String mPreviousSelection = "";
    private LocationViewModel mLocationViewModel;
    private static boolean store = false, district = false, zone = false, market = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//            userDetails = Reusable_Functions.getUserDetails(sharedPreferences);
            setContentView(R.layout.activity_easyday_filter);
            ButterKnife.bind(this);
            progressBarRelativeLayout.setVisibility(View.VISIBLE);
            userId = sharedPreferences.getString("userId", "");
            bearertoken = sharedPreferences.getString("bearerToken", "");
            android.app.ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }

            networkaccess();
            getIntentData();
            intialise_ui();
            if (mFilterSelectedData != null) {
                displaySelectedCount();
            }
            defineViewModels();
            initlizeFilterData();
//            filterStart = false;
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Error", e);
            Toast.makeText(this, "Loading Filter data please wait...", Toast.LENGTH_SHORT).show();
            progressBarRelativeLayout.setVisibility(View.VISIBLE);
//            initlizeFilterData();
        }

    }

    private void defineViewModels() {
        LocationFactory factory = new LocationFactory(getApplication());
        mLocationViewModel = ViewModelProviders.of(this, factory).get(LocationViewModel.class);

        mLocationViewModel.getUpdatedLocationStoreList().observe(this, new Observer<List<BrandVendorModel>>() {
            @Override
            public void onChanged(@Nullable List<BrandVendorModel> models) {
                Log.e(TAG, "onChanged: Store List Count: " + (models != null ? models.size() : 0));
                if (models != null) {
                    mStoreList = new ArrayList<>(models);
                    store = true;
//                    onHierarchyClickListener(mHierarchySelected);
                }
            }
        });
        mLocationViewModel.getUpdatedDistrictList().observe(this, new Observer<List<BrandVendorModel>>() {
            @Override
            public void onChanged(@Nullable List<BrandVendorModel> models) {
                Log.e(TAG, "onChanged: District List Count: " + (models != null ? models.size() : 0));
                if (models != null) {
                    mDistrictList = new ArrayList<>(models);
                    district = true;onHierarchyClickListener(mHierarchySelected);
                }
            }
        });
        mLocationViewModel.getUpdatedMarketList().observe(this, new Observer<List<BrandVendorModel>>() {
            @Override
            public void onChanged(@Nullable List<BrandVendorModel> models) {
                Log.e(TAG, "onChanged: Market List Count: " + (models != null ? models.size() : 0));
                if (models != null) {
                    mMarketList = new ArrayList<>(models);
                    market = true;onHierarchyClickListener(mHierarchySelected);
                }
            }
        });
        mLocationViewModel.getUpdatedZoneList().observe(this, new Observer<List<BrandVendorModel>>() {
            @Override
            public void onChanged(@Nullable List<BrandVendorModel> models) {
                Log.e(TAG, "onChanged: Zone List Count: " + (models != null ? models.size() : 0));
                if (models != null) {
                    mZoneList = new ArrayList<>(models);
                    zone = true;onHierarchyClickListener(mHierarchySelected);
                }
            }
        });

    }

    // used for display selected list add here if new list added in list
    private void displaySelectedCount() {
        for (String s : mHierarchyArrayList) {
            switch (s) {
                case DEPT:
                    updateCount(s, mFilterSelectedData.getDepartmentSelectedList().size());
                    break;
                case SUBDEPT:
                    updateCount(s, mFilterSelectedData.getSubDeptSelectedList().size());
                    break;
                case CLASS:
                    updateCount(s, mFilterSelectedData.getClassSelectedList().size());
                    break;
                case SUBCLASS:
                    updateCount(s, mFilterSelectedData.getSubClassSelectedList().size());
                    break;
                case MC:
                    updateCount(s, mFilterSelectedData.getMCSelectedList().size());
                    break;
                case ZONE:
                    updateCount(s, mFilterSelectedData.getZoneSelectedList().size());
                    break;
                case MARKET:
                    updateCount(s, mFilterSelectedData.getMarketSelectedList().size());
                    break;
                case DISTRICT:
                    updateCount(s, mFilterSelectedData.getDistrictSelectedList().size());
                    break;
                case STORE:
                    updateCount(s, mFilterSelectedData.getStoreSelectedList().size());
                    break;
                case BRAND_TYPE:
                    updateCount(s, mFilterSelectedData.getBrandTypeSelectedList().size());
                    break;
                case BRAND:
                    updateCount(s, mFilterSelectedData.getBrandSelectedList().size());
                    break;
                case VENDOR:
                    updateCount(s, mFilterSelectedData.getVendorSelectedList().size());
                    break;
                case PVA_PRODUCT:
                    updateCount(s, mFilterSelectedData.getPvAProductList().size());
                    break;

            }
        }
    }

    private void checkFilterValues(String from) {
       /* switch (from) {
            case Dashboard:
                break;
            case Sales:
                break;
        }*/
    }

    private void getIntentData() {
        if (getIntent().getSerializableExtra(FilterConstant.FILTER_ARRAY) != null) {
            mFrom = getIntent().getStringExtra(CHECK_FROM);
            mFilterSelectedData = (FilterAppliedData) getIntent().getSerializableExtra(FILTER_SELECTED);
            //noinspection unchecked
            mHierarchyArrayList = (ArrayList<String>) getIntent().getSerializableExtra(FilterConstant.FILTER_ARRAY);
            if (mHierarchyArrayList != null) {
                mHierarchySelectCount = new ArrayList<>();
                mHierarchySetBackgroundList = new ArrayList<>();
                for (int i = 0; i < mHierarchyArrayList.size(); i++) {
                    if ((i == 0 || i == 6 || i == 11 || i == 14) && mFrom.equals("Sales")) {
                        mHierarchySetBackgroundList.add(true);
                        mHierarchySelectCount.add(0);
                    } else {
                        mHierarchySelectCount.add(0);
                        mHierarchySetBackgroundList.add(false);
                    }
                }
            } else {
                throw new NullPointerException("Please pass Hierarchy list in intent...");
            }
            if (mFrom.equals("Sales")) {
                mHierarchySelected = mHierarchyArrayList.get(1);
                mHierarchySetBackgroundList.set(1, true);
            } else {
                mHierarchySelected = mHierarchyArrayList.get(0);
                mHierarchySetBackgroundList.set(0, true);
            }
        } else {
            throw new RuntimeException("Please pass array list into intent for Left recycle view");
        }

        view = getIntent().getStringExtra("view");
        updateApplyCount(mFilterSelectedData);
    }

    private void initlizeFilterData() {
        if (mFrom != null) {
            switch (mFrom) {
                case "Dashboard":
                    prepareFilterList("Dashboard");
                    break;
                case "Member_Dashboard":
                case "PlannedCardFragment":
                    prepareFilterList("Member_Dashboard");
                    break;
                case "MEMBER_VISIT_DETAILS":
                    prepareFilterList("MEMBER_VISIT_DETAILS");
                    break;
                case "Sales":
                    prepareFilterList("Sales");
                    break;

            }
        }
    }

    private void prepareFilterList(String from) {
        try {

            if (mFilterSelectedData == null) mFilterSelectedData = new FilterAppliedData();
            switch (from) {
//                case "Sales":
//                    mDepartmentList = addSelectedValues(mFrom, mFilterSelectedData.getDepartmentSelectedList(), FilterSharedPreferences.Key.ED_DEPT);
//                    mDepartmentAPIList = new ArrayList<>();
//                    mSubDeptList = addSelectedValues(mFrom, mFilterSelectedData.getSubDeptSelectedList(), FilterSharedPreferences.Key.ED_SUB_DEPT);
//                    mSubDeptAPIList = new ArrayList<>();
//                    mClassList = addSelectedValues(mFrom, mFilterSelectedData.getClassSelectedList(), FilterSharedPreferences.Key.ED_CLASS);
//                    mClassAPIList = new ArrayList<>();
//                    mSubClassList = addSelectedValues(mFrom, mFilterSelectedData.getSubClassSelectedList(), FilterSharedPreferences.Key.ED_SUB_CLASS);
//                    mSubClassAPIList = new ArrayList<>();
//                    mMCList = addSelectedValues(mFrom, mFilterSelectedData.getMCSelectedList(), FilterSharedPreferences.Key.ED_MC);
//                    mMCAPIList = new ArrayList<>();
//                    mZoneList = addSelectedValues(mFrom, mFilterSelectedData.getZoneSelectedList(), FilterSharedPreferences.Key.ZONE);
//                    mZoneAPIList = new ArrayList<>();
//                    mMarketList = addSelectedValues(mFrom, mFilterSelectedData.getMarketSelectedList(), FilterSharedPreferences.Key.ED_MARKER);
//                    mMarketAPIList = new ArrayList<>();
//                    mDistrictList = addSelectedValues(mFrom, mFilterSelectedData.getDistrictSelectedList(), FilterSharedPreferences.Key.ED_DISTRICT);
//                    mDistrictAPIList = new ArrayList<>();
//                    mStoreList = addSelectedValues(mFrom, mFilterSelectedData.getStoreSelectedList(), FilterSharedPreferences.Key.STORE);
//                    mStoreAPIList = new ArrayList<>();
//                    mBrandTypeList = addSelectedValues(mFrom, mFilterSelectedData.getBrandTypeSelectedList(), FilterSharedPreferences.Key.ED_BRAND_TYPE);
//                    mBrandTypeAPIList = new ArrayList<>();
//                    mBrandList = addSelectedValues(mFrom, mFilterSelectedData.getBrandSelectedList(), FilterSharedPreferences.Key.ED_BRAND);
//                    mBrandAPIList = new ArrayList<>();
//                    mVendorList = addSelectedValues(mFrom, mFilterSelectedData.getVendorSelectedList(), FilterSharedPreferences.Key.ED_VENDOR);
//                    mVendorAPIList = new ArrayList<>();
//                    break;
                case "Dashboard":

                    mDistrictList= (ArrayList<BrandVendorModel>) mLocationViewModel.getmDistrictList().getValue();
                    mZoneList= (ArrayList<BrandVendorModel>) mLocationViewModel.getmZoneList().getValue();
                    mZoneAPIList = new ArrayList<>();
                    mMarketAPIList = new ArrayList<>();
                    mMarketList= (ArrayList<BrandVendorModel>) mLocationViewModel.getmMarketList().getValue();
                    mDistrictAPIList = new ArrayList<>();
                    mStoreList = (ArrayList<BrandVendorModel>) mLocationViewModel.getmStoreList().getValue();
                    mStoreAPIList = new ArrayList<>();
                    Log.e(TAG, "prepareFilterList: data load" );
                    break;
//                case MemberDashboard_Ed_Activity.Member_Dashboard:
//                case PlannedCardFragment:
//                    mZoneList = addSelectedValues(mFrom, mFilterSelectedData.getZoneSelectedList(), FilterSharedPreferences.Key.ZONE);
//                    mZoneAPIList = new ArrayList<>();
//                    mMarketList = addSelectedValues(mFrom, mFilterSelectedData.getMarketSelectedList(), FilterSharedPreferences.Key.ED_MARKER);
//                    mMarketAPIList = new ArrayList<>();
//                    mDistrictList = addSelectedValues(mFrom, mFilterSelectedData.getDistrictSelectedList(), FilterSharedPreferences.Key.ED_DISTRICT);
//                    mDistrictAPIList = new ArrayList<>();
//                    mStoreList = addSelectedValues(mFrom, mFilterSelectedData.getStoreSelectedList(), FilterSharedPreferences.Key.STORE);
//                    mStoreAPIList = new ArrayList<>();
//                    break;
//                case MEMBER_VISIT_DETAILS:
//                    mZoneList = addSelectedValues(mFrom, mFilterSelectedData.getZoneSelectedList(), FilterSharedPreferences.Key.ZONE);
//                    mZoneAPIList = new ArrayList<>();
//                    mMarketList = addSelectedValues(mFrom, mFilterSelectedData.getMarketSelectedList(), FilterSharedPreferences.Key.ED_MARKER);
//                    mMarketAPIList = new ArrayList<>();
//                    mDistrictList = addSelectedValues(mFrom, mFilterSelectedData.getDistrictSelectedList(), FilterSharedPreferences.Key.ED_DISTRICT);
//                    mDistrictAPIList = new ArrayList<>();
//                    mStoreList = addSelectedValues(mFrom, mFilterSelectedData.getStoreSelectedList(), FilterSharedPreferences.Key.STORE);
//                    mStoreAPIList = new ArrayList<>();
//                    break;

            }
            onHierarchyClickListener(mHierarchySelected);
            progressBarRelativeLayout.setVisibility(View.GONE);

        } catch (Exception e) {
            Log.e(TAG, "prepareFilterList: ", e);
            progressBarRelativeLayout.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Please wait, loading filter data...", Toast.LENGTH_SHORT).show();
            if (retry < 2) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initlizeFilterData();
                        retry++;
                    }
                }, 5 * 1000);
            } else {
                updateEasyDayFilterData(this, bearertoken, userId, queue, geoLevel2Code, lobId);
            }
        }
    }

    private ArrayList<BrandVendorModel> addSelectedValues(String mFrom, ArrayList<BrandVendorModel> selectedList, String level) {
//        ArrayList<BrandVendorModel> list = parseListFromSharedPreference(level);
//        if (selectedList != null && selectedList.size() > 0) {
//            for (BrandVendorModel model : selectedList) {
//                addSelectedModel(mFrom, model, list, level);
//            }
//        }
        return null;
    }

//    private void addSelectedModel(String mFrom, BrandVendorModel model, ArrayList<BrandVendorModel> list, String level) {
//        switch (mFrom) {
//            case EasyDaySalesFragment.Sales:
//            case MEMBER_VISIT_DETAILS:
//            case MemberDashboard_Ed_Activity.Member_Dashboard:
//            case Dashboard:
//            case PlannedCardFragment:
//                switch (level) {
//                    case FilterSharedPreferences.Key.ED_DEPT:
//                    case DEPT:
//                        for (BrandVendorModel m : list) {
//                            if (m.getProdLevel2Code().equals(model.getProdLevel2Code()) || m.getProdLevel2Desc().equals(model.getProdLevel2Desc())) {
//                                m.setSelected(true);
//                                break;
//                            }
//                        }
//                        break;
//                    case FilterSharedPreferences.Key.ED_SUB_DEPT:
//                    case SUBDEPT:
//                        for (BrandVendorModel m : list) {
//                            if (m.getProdLevel3Code().equals(model.getProdLevel3Code()) || m.getProdLevel3Desc().equals(model.getProdLevel3Desc())) {
//                                m.setSelected(true);
//                                break;
//                            }
//                        }
//                        break;
//                    case FilterSharedPreferences.Key.ED_CLASS:
//                    case CLASS:
//                        for (BrandVendorModel m : list) {
//                            if (m.getProdLevel4Code().equals(model.getProdLevel4Code()) || m.getProdLevel4Desc().equals(model.getProdLevel4Desc())) {
//                                m.setSelected(true);
//                                break;
//                            }
//                        }
//                        break;
//                    case FilterSharedPreferences.Key.ED_SUB_CLASS:
//                    case SUBCLASS:
//                        for (BrandVendorModel m : list) {
//                            if (m.getProdLevel5Code().equals(model.getProdLevel5Code()) || m.getProdLevel5Desc().equals(model.getProdLevel5Desc())) {
//                                m.setSelected(true);
//                                break;
//                            }
//                        }
//                        break;
//                    case FilterSharedPreferences.Key.ED_MC:
//                    case MC:
//                        for (BrandVendorModel m : list) {
//                            if (m.getProdLevel6Code().equals(model.getProdLevel6Code()) || m.getProdLevel6Desc().equals(model.getProdLevel6Desc())) {
//                                m.setSelected(true);
//                                break;
//                            }
//                        }
//                        break;
//                    case FilterSharedPreferences.Key.ZONE:
//                    case ZONE:
//                        for (BrandVendorModel m : list) {
//                            if (m.getZoneED().equals(model.getZoneED())) {
//                                m.setSelected(true);
//                                break;
//                            }
//                        }
//                        break;
//                    case FilterSharedPreferences.Key.ED_MARKER:
//                    case MARKET:
//                        for (BrandVendorModel m : list) {
//                            if (m.getMarketED().equals(model.getMarketED())) {
//                                m.setSelected(true);
//                                break;
//                            }
//                        }
//                        break;
//                    case FilterSharedPreferences.Key.ED_DISTRICT:
//                    case DISTRICT:
//                        for (BrandVendorModel m : list) {
//                            if (m.getDistrictED().equals(model.getDistrictED())) {
//                                m.setSelected(true);
//                                break;
//                            }
//                        }
//                        break;
//                    case FilterSharedPreferences.Key.STORE:
//                    case STORE:
//                        for (BrandVendorModel m : list) {
//                            if (m.getStoreCode().equals(model.getStoreCode()) || m.getStoreCodeParam().equals(model.getStoreCodeParam())) {
//                                m.setSelected(true);
//                                break;
//                            }
//                        }
//                        break;
//                    case FilterSharedPreferences.Key.ED_BRAND_TYPE:
//                    case BRAND_TYPE:
//                        for (BrandVendorModel m : list) {
//                            if (m.getBrandLevel1Code().equals(model.getBrandLevel1Code())) {
//                                m.setSelected(true);
//                                break;
//                            }
//                        }
//                        break;
//                    case FilterSharedPreferences.Key.ED_BRAND:
//                    case BRAND:
//                        for (BrandVendorModel m : list) {
//                            if (m.getBrandLevel3Code().equals(model.getBrandLevel3Code()) || m.getBrandLevel3Desc().equals(model.getBrandLevel3Desc())) {
//                                m.setSelected(true);
//                                break;
//                            }
//                        }
//                        break;
//                    case FilterSharedPreferences.Key.ED_VENDOR:
//                    case VENDOR:
//                        for (BrandVendorModel m : list) {
//                            if (m.getVendorCode().equals(model.getVendorCode()) || m.getVendorDesc().equals(model.getVendorDesc())) {
//                                m.setSelected(true);
//                                break;
//                            }
//                        }
//                        break;
//                }
//                break;
//
//        }
//    }

    private ArrayList<BrandVendorModel> parseListFromSharedPreference(String key) {
        Log.e("key ", " " + key);
        String listData = null;//mFilterSharedPreferences.getString(key);
        ArrayList<BrandVendorModel> list = new ArrayList<>();
        if (!listData.isEmpty()) {
            try {
                JSONArray array;
                array = new JSONArray(listData);
                for (int i = 0; i < array.length(); i++) {
                    BrandVendorModel model = gson.fromJson(array.get(i).toString(), BrandVendorModel.class);

                    list.add(model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

//    private ArrayList<BrandVendorModel> getSelectedList(String key) {
//        ArrayList<BrandVendorModel> list = null;
//        switch (key) {
//            case FilterSharedPreferences.Key.ED_DEPT:
//                list = mFilterSelectedData.getDepartmentSelectedList();
//                break;
//            case FilterSharedPreferences.Key.ED_SUB_DEPT:
//                list = mFilterSelectedData.getSubDeptSelectedList();
//                break;
//            case FilterSharedPreferences.Key.ED_CLASS:
//                list = mFilterSelectedData.getClassSelectedList();
//                break;
//            case FilterSharedPreferences.Key.ED_SUB_CLASS:
//                list = mFilterSelectedData.getSubClassSelectedList();
//                break;
//            case FilterSharedPreferences.Key.ED_MC:
//                list = mFilterSelectedData.getMCSelectedList();
//                break;
//            case FilterSharedPreferences.Key.ZONE:
//                list = mFilterSelectedData.getZoneSelectedList();
//                break;
//            case FilterSharedPreferences.Key.ED_MARKER:
//                list = mFilterSelectedData.getMarketSelectedList();
//                break;
//            case FilterSharedPreferences.Key.ED_DISTRICT:
//                list = mFilterSelectedData.getDistrictSelectedList();
//                break;
//            case FilterSharedPreferences.Key.EZ_STORE:
//                list = mFilterSelectedData.getStoreSelectedList();
//                break;
//            case FilterSharedPreferences.Key.ED_BRAND_TYPE:
//                list = mFilterSelectedData.getBrandTypeSelectedList();
//                break;
//            case FilterSharedPreferences.Key.ED_BRAND:
//                list = mFilterSelectedData.getBrandSelectedList();
//                break;
//            case FilterSharedPreferences.Key.ED_VENDOR:
//                list = mFilterSelectedData.getVendorSelectedList();
//                break;
//        }
//
//        return list;
//    }

    private void intialise_ui() {
        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerViewFilter.setLayoutManager(layoutManager);
            mHierarchyAdapter = new HierarchyAdapter(this, mHierarchyArrayList, mHierarchySelectCount, mHierarchySetBackgroundList, mFrom);
            recyclerViewFilter.setHasFixedSize(true);
            recyclerViewFilter.setAdapter(mHierarchyAdapter);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(0, 0, 0, 0);
            progressBarRelativeLayout.setLayoutParams(params);
            ItemClickFun();
        } catch (Exception e) {
            Log.e(TAG, "intialise_ui: ", e);
            onBackPressed();
        }
    }

    private void ItemClickFun() {
        recyclerViewFilter.addOnItemTouchListener(new RecyclerItemClickListener(Filter.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    if (!mHierarchySelected.equals(mHierarchyArrayList.get(position).toString())) {
                        mPreviousSelection = mHierarchySelected;
                        mHierarchySelected = mHierarchyArrayList.get(position).toString();
                        Log.e(TAG, "onItemClick: " + mHierarchySelected);
                        if (position < mHierarchyArrayList.size()) {
                            if ((position == 0 || position == 6 || position == 11 || position == 14) && mFrom.equals("Sales")) {
                                mHierarchySetBackgroundList.set(position, true);
                            } else {
                                if (!mPreviousSelection.equals(mHierarchySelected) && checkNextLevelValues(mHierarchySelected)) {
                                    Log.e(TAG, "onItemClick: " + mPreviousSelection + " " + mHierarchySelected);
                                    // to check last level of hierarchy
                                    int nextLevel = mHierarchyArrayList.indexOf(mHierarchySelected) + 1;
                                    int LastLevel = getLastLevelOf(mHierarchySelected);
                                    for (int i = nextLevel; i <= LastLevel; i++) {
                                        if (hasSelecedValues(mHierarchyArrayList.get(i))) {
                                            mPreviousSelection = mHierarchyArrayList.get(i);
                                        }
                                    }
                                    getDialog();
                                    onHierarchyClickListener(mPreviousSelection);
                                    mHierarchySelected = mPreviousSelection;
                                    // for clear previous selection
                                    for (int i = 0; i < mHierarchyArrayList.size(); i++) {
                                        if (mHierarchyArrayList.indexOf(mPreviousSelection) == i) {
                                            mHierarchySetBackgroundList.set(i, true);
                                        } else {
                                            if ((i == 0 || i == 6 || i == 11 || i == 14) && mFrom.equals("Sales")) {
                                                mHierarchySetBackgroundList.set(i, true);
                                            } else {
                                                mHierarchySetBackgroundList.set(i, false);
                                            }
                                        }
                                    }
                                } else {
                                    onHierarchyClickListener(mHierarchySelected);
                                    for (int i = 0; i < mHierarchyArrayList.size(); i++) {
                                        if (position == i) {
                                            mHierarchySetBackgroundList.set(i, true);
                                        } else {
                                            if ((i == 0 || i == 6 || i == 11 || i == 14) && mFrom.equals("Sales")) {
                                                mHierarchySetBackgroundList.set(i, true);

                                            } else {
                                                mHierarchySetBackgroundList.set(i, false);

                                            }
                                        }
                                    }
                                }
                            }
                            mHierarchyAdapter.notifyDataSetChanged();
                        }
                    }

                } catch (Exception e) {
                    Log.e(TAG, "onItemClick: ", e);
                }
            }
        }));
    }

    private boolean hasSelecedValues(String level) {
        switch (level) {
            case DEPT:
                return mFilterSelectedData.getDepartmentSelectedList().size() > 0;
            case SUBDEPT:
                return mFilterSelectedData.getSubDeptSelectedList().size() > 0;
            case CLASS:
                return mFilterSelectedData.getClassSelectedList().size() > 0;
            case SUBCLASS:
                return mFilterSelectedData.getSubClassSelectedList().size() > 0;
            case MC:
                return mFilterSelectedData.getMCSelectedList().size() > 0;
            case ZONE:
                return mFilterSelectedData.getZoneSelectedList().size() > 0;
            case MARKET:
                return mFilterSelectedData.getMarketSelectedList().size() > 0;
            case DISTRICT:
                return mFilterSelectedData.getDistrictSelectedList().size() > 0;
            case STORE:
                return mFilterSelectedData.getStoreSelectedList().size() > 0;
            case BRAND_TYPE:
                return mFilterSelectedData.getBrandTypeSelectedList().size() > 0;
            case BRAND:
                return mFilterSelectedData.getBrandSelectedList().size() > 0;
            case VENDOR:
                return false;
        }
        return false;
    }

    private boolean checkNextLevelValues(String itemClicked) {
        switch (itemClicked) {
            case DEPT:
                return (mFilterSelectedData.getSubDeptSelectedList().size() > 0
                        || mFilterSelectedData.getClassSelectedList().size() > 0
                        || mFilterSelectedData.getSubClassSelectedList().size() > 0
                        || mFilterSelectedData.getMCSelectedList().size() > 0) && !filterStart;

            case SUBDEPT:
                return (mFilterSelectedData.getClassSelectedList().size() > 0
                        || mFilterSelectedData.getSubClassSelectedList().size() > 0
                        || mFilterSelectedData.getMCSelectedList().size() > 0);
            case CLASS:
                return (mFilterSelectedData.getSubClassSelectedList().size() > 0
                        || mFilterSelectedData.getMCSelectedList().size() > 0);
            case SUBCLASS:
                return (mFilterSelectedData.getMCSelectedList().size() > 0);
            case MC:
                return false;
            case ZONE:
                return (mFilterSelectedData.getMarketSelectedList().size() > 0 ||
                        mFilterSelectedData.getDistrictSelectedList().size() > 0 ||
                        mFilterSelectedData.getStoreSelectedList().size() > 0) && !filterStart;
            case MARKET:
                return (mFilterSelectedData.getDistrictSelectedList().size() > 0 ||
                        mFilterSelectedData.getStoreSelectedList().size() > 0);

            case DISTRICT:
                return mFilterSelectedData.getStoreSelectedList().size() > 0;
            case STORE:
                return false;
            case BRAND_TYPE:
                return mFilterSelectedData.getBrandSelectedList().size() > 0 && !filterStart;

            case BRAND:
                return false;
            case VENDOR:
                break;
        }
        return false;

    }

    private void onHierarchyClickListener(String itemClicked) {
//        if (zone && market && store && district){
            try {
                boolean headerChange = false;
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                switch (itemClicked) {
                    case DEPT:

                        bundle.putSerializable(ADAPTER_LIST, mDepartmentList);
                        bundle.putSerializable(SELECTED_LIST, mFilterSelectedData.getDepartmentSelectedList());
                        bundle.putBoolean(HAS_LAZY_LOADING, mDepartmentList.size() == 500);

                        Fragment deptFilter = getFragmentByCaller(mFrom, itemClicked, bundle);
                        mLastAddedFragment = deptFilter;
                        mFragmentTransaction.replace(R.id.framelayoutList, deptFilter);
                        mFragmentTransaction.commit();

                        break;
                    case SUBDEPT:
//                    if (mFilterSelectedData.getClassSelectedList().size() > 0
//                            || mFilterSelectedData.getSubClassSelectedList().size() > 0
//                            || mFilterSelectedData.getMCSelectedList().size() > 0)
//                    {
//                        getDialog();
//                    }
//                        Toast.makeText(this, "Previous selection cleared in lower level ", Toast.LENGTH_SHORT).show();
////                        mFilterSelectedData = new FilterAppliedData();
//                        mFilterSelectedData.getClassSelectedList().clear();
//                        mFilterSelectedData.getSubClassSelectedList().clear();
//                        mFilterSelectedData.getMCSelectedList().clear();
//                        int nextLevel = mHierarchyArrayList.indexOf(itemClicked) + 1;
//                        int lastLevel = getLastLevelOf(itemClicked);
//                        for (int i = nextLevel; i <= lastLevel; i++) {
//                            clearSelectValues(mHierarchyArrayList.get(i));
//                            updateCount(mHierarchyArrayList.get(i), 0);
//                        }
//                        updateApplyCount(mFilterSelectedData);
//                    }

                        updateFilterHeaderValues(mFrom, itemClicked);
                        if (mSubDeptPreviousHeader.length() != mProductHeader.length()) {
                            mSubDeptPreviousHeader = mProductHeader;
                            headerChange = true;
                        }
                        bundle.putSerializable(ADAPTER_LIST, Reusable_Functions.isStringEmpty(mProductHeader) ? mSubDeptList : mSubDeptAPIList);
                        bundle.putSerializable(SELECTED_LIST, mFilterSelectedData.getSubDeptSelectedList());
                        bundle.putBoolean(HAS_LAZY_LOADING, Reusable_Functions.isStringEmpty(mProductHeader) ? mSubDeptList.size() == 500 : false);
                        bundle.putBoolean(FILTER_APPLIED, mProductHeader.length() > 0);
                        bundle.putString(FILTER_DATA, mProductHeader);
                        bundle.putBoolean(FILTER_HEADER_DATA_CHANGE, headerChange);
                        Fragment subdeptFilter = getFragmentByCaller(mFrom, itemClicked, bundle);
                        mLastAddedFragment = subdeptFilter;
                        mFragmentTransaction.replace(R.id.framelayoutList, subdeptFilter);
                        mFragmentTransaction.commit();

                        break;
                    case CLASS:
                        updateFilterHeaderValues(mFrom, itemClicked);
                        if (mClassPreviousHeader.length() != mProductHeader.length()) {
                            mClassPreviousHeader = mProductHeader;
                            headerChange = true;
                        }
                        bundle.putSerializable(ADAPTER_LIST, Reusable_Functions.isStringEmpty(mProductHeader) ? mClassList : mClassAPIList);
                        bundle.putSerializable(SELECTED_LIST, mFilterSelectedData.getClassSelectedList());
                        bundle.putBoolean(HAS_LAZY_LOADING, Reusable_Functions.isStringEmpty(mProductHeader) ? mClassList.size() == 500 : false);
                        bundle.putBoolean(FILTER_APPLIED, mProductHeader.length() > 0);
                        bundle.putString(FILTER_DATA, mProductHeader);
                        bundle.putBoolean(FILTER_HEADER_DATA_CHANGE, headerChange);
                        Fragment classFilter = getFragmentByCaller(mFrom, itemClicked, bundle);
                        mLastAddedFragment = classFilter;
                        mFragmentTransaction.replace(R.id.framelayoutList, classFilter);
                        mFragmentTransaction.commit();
                        break;
                    case SUBCLASS:
//                    if (mFilterSelectedData.getMCSelectedList().size() > 0) {
////                        Toast.makeText(this, "Previous selection cleared in lower level ", Toast.LENGTH_SHORT).show();
//////                        mFilterSelectedData = new FilterAppliedData();
////                        mFilterSelectedData.getMCSelectedList().clear();
////                        int nextLevel = mHierarchyArrayList.indexOf(itemClicked) + 1;
////                        int lastLevel = getLastLevelOf(itemClicked);
////                        for (int i = nextLevel; i <= lastLevel; i++) {
////                            clearSelectValues(mHierarchyArrayList.get(i));
////                            updateCount(mHierarchyArrayList.get(i), 0);
////                        }
////                        updateApplyCount(mFilterSelectedData);
//                        getDialog();
//                    }

                        updateFilterHeaderValues(mFrom, itemClicked);
                        if (mSubClassPreviousHeader.length() != mProductHeader.length()) {
                            mSubClassPreviousHeader = mProductHeader;
                            headerChange = true;
                        }
                        bundle.putSerializable(ADAPTER_LIST
                                , Reusable_Functions.isStringEmpty(mProductHeader) ? mSubClassList : mSubClassAPIList);
                        bundle.putSerializable(SELECTED_LIST, mFilterSelectedData.getSubClassSelectedList());
                        bundle.putBoolean(HAS_LAZY_LOADING, Reusable_Functions.isStringEmpty(mProductHeader) ? mSubClassList.size() == 500 : false);
                        bundle.putBoolean(FILTER_APPLIED, mProductHeader.length() > 0);
                        bundle.putString(FILTER_DATA, mProductHeader);
                        bundle.putBoolean(FILTER_HEADER_DATA_CHANGE, headerChange);
                        Fragment subClassFilter = getFragmentByCaller(mFrom, itemClicked, bundle);
                        mLastAddedFragment = subClassFilter;
                        mFragmentTransaction.replace(R.id.framelayoutList, subClassFilter);
                        mFragmentTransaction.commit();
                        break;
                    case MC:
                        updateFilterHeaderValues(mFrom, itemClicked);
                        if (mMCPreviousHeader.length() != mProductHeader.length()) {
                            mMCPreviousHeader = mProductHeader;
                            headerChange = true;
                        }
                        bundle.putSerializable(ADAPTER_LIST
                                , Reusable_Functions.isStringEmpty(mProductHeader) ? mMCList : mMCAPIList);
                        bundle.putSerializable(SELECTED_LIST, mFilterSelectedData.getMCSelectedList());
                        bundle.putBoolean(HAS_LAZY_LOADING, Reusable_Functions.isStringEmpty(mProductHeader) ? mMCList.size() == 500 : false);
                        bundle.putBoolean(FILTER_APPLIED, mProductHeader.length() > 0);
                        bundle.putString(FILTER_DATA, mProductHeader);
                        bundle.putBoolean(FILTER_HEADER_DATA_CHANGE, headerChange);

                        Fragment mcFilter = getFragmentByCaller(mFrom, itemClicked, bundle);
                        mLastAddedFragment = mcFilter;
                        mFragmentTransaction.replace(R.id.framelayoutList, mcFilter);
                        mFragmentTransaction.commit();
                        break;

                    case ZONE:

                        bundle.putSerializable(ADAPTER_LIST, mZoneList);
                        bundle.putSerializable(SELECTED_LIST, mFilterSelectedData.getZoneSelectedList());
                        bundle.putBoolean(HAS_LAZY_LOADING, mZoneList.size() == 500);
                        bundle.putString(FILTER_DATA, mLocationHeader);

                        Fragment zoneFilter = getFragmentByCaller(mFrom, itemClicked, bundle);
                        mLastAddedFragment = zoneFilter;
                        mFragmentTransaction.replace(R.id.framelayoutList, zoneFilter);
                        mFragmentTransaction.commit();

                        break;

                    case MARKET:
                        updateFilterHeaderValues(mFrom, itemClicked);
                        if (!mMarketPreviousHeader.toLowerCase().equals(mLocationHeader.toLowerCase())) {
                            mMarketPreviousHeader = mLocationHeader;
                            headerChange = true;
                        }
                        bundle.putSerializable(ADAPTER_LIST, Reusable_Functions.isStringEmpty(mLocationHeader) ? mMarketList : mMarketAPIList);
                        bundle.putSerializable(SELECTED_LIST, mFilterSelectedData.getMarketSelectedList());
                        bundle.putBoolean(HAS_LAZY_LOADING, Reusable_Functions.isStringEmpty(mLocationHeader) ? mMarketList.size() == 500 : false);
                        bundle.putBoolean(FILTER_APPLIED, mLocationHeader.length() > 0);
                        bundle.putString(FILTER_DATA, mLocationHeader);
                        bundle.putBoolean(FILTER_HEADER_DATA_CHANGE, headerChange);
                        Fragment marketFilter = getFragmentByCaller(mFrom, itemClicked, bundle);
                        mLastAddedFragment = marketFilter;
                        mFragmentTransaction.replace(R.id.framelayoutList, marketFilter);
                        mFragmentTransaction.commit();

                        break;
                    case DISTRICT:
                        updateFilterHeaderValues(mFrom, itemClicked);
                        if (!mDistrictPreviousHeader.toLowerCase().equals(mLocationHeader.toLowerCase())) {
                            mDistrictPreviousHeader = mLocationHeader;
                            headerChange = true;
                        }
                        bundle.putSerializable(ADAPTER_LIST, Reusable_Functions.isStringEmpty(mLocationHeader) ? mDistrictList : mDistrictAPIList);
                        bundle.putSerializable(SELECTED_LIST, mFilterSelectedData.getDistrictSelectedList());
                        bundle.putBoolean(HAS_LAZY_LOADING, Reusable_Functions.isStringEmpty(mLocationHeader) ? mDistrictList.size() == 500 : false);
                        bundle.putBoolean(FILTER_APPLIED, mLocationHeader.length() > 0);
                        bundle.putString(FILTER_DATA, mLocationHeader);
                        bundle.putBoolean(FILTER_HEADER_DATA_CHANGE, headerChange);
                        Fragment districtFilter = getFragmentByCaller(mFrom, itemClicked, bundle);
                        mLastAddedFragment = districtFilter;
                        mFragmentTransaction.replace(R.id.framelayoutList, districtFilter);
                        mFragmentTransaction.commit();

                        break;
                    case STORE:
                        updateFilterHeaderValues(mFrom, itemClicked);
                        if (!mStorePreviousHeaer.toLowerCase().equals(mLocationHeader.toLowerCase())) {
                            mStorePreviousHeaer = mLocationHeader;
                            headerChange = true;
                        }
                        bundle.putSerializable(ADAPTER_LIST, Reusable_Functions.isStringEmpty(mLocationHeader) ? mStoreList : mStoreAPIList);
                        bundle.putSerializable(SELECTED_LIST, mFilterSelectedData.getStoreSelectedList());
                        bundle.putBoolean(HAS_LAZY_LOADING, Reusable_Functions.isStringEmpty(mLocationHeader) ? mStoreList.size() == 500 : false);
                        bundle.putBoolean(FILTER_APPLIED, mLocationHeader.length() > 0);
                        bundle.putString(FILTER_DATA, mLocationHeader);
                        bundle.putBoolean(FILTER_HEADER_DATA_CHANGE, headerChange);
                        Fragment storeFilter = getFragmentByCaller(mFrom, itemClicked, bundle);
                        mLastAddedFragment = storeFilter;
                        mFragmentTransaction.replace(R.id.framelayoutList, storeFilter);
                        mFragmentTransaction.commit();
                        break;
                    case BRAND_TYPE:
                        bundle.putSerializable(ADAPTER_LIST, mBrandTypeList);
                        bundle.putSerializable(SELECTED_LIST, mFilterSelectedData.getBrandTypeSelectedList());
                        bundle.putBoolean(HAS_LAZY_LOADING, mBrandTypeList.size() == 500);
                        Fragment brandTypeFilter = getFragmentByCaller(mFrom, itemClicked, bundle);
                        mLastAddedFragment = brandTypeFilter;
                        mFragmentTransaction.replace(R.id.framelayoutList, brandTypeFilter);
                        mFragmentTransaction.commit();
                        break;
                    case BRAND:
                        updateFilterHeaderValues(mFrom, itemClicked);
                        if (!mBrandPreviousHeader.toLowerCase().equals(mBradTypeHeader.toLowerCase())) {
                            mBrandPreviousHeader = mBradTypeHeader;
                            headerChange = true;
                        }
                        bundle.putSerializable(ADAPTER_LIST, Reusable_Functions.isStringEmpty(mBradTypeHeader) ? mBrandList : mBrandAPIList);  // added adapter list if header is not null/empty
                        bundle.putSerializable(SELECTED_LIST, mFilterSelectedData.getBrandSelectedList());
                        bundle.putBoolean(HAS_LAZY_LOADING, Reusable_Functions.isStringEmpty(mBradTypeHeader) ? mBrandList.size() == 500 : false); //  lazy loading on if header is null and adapter list size =500
                        bundle.putString(FILTER_DATA, mBradTypeHeader);
                        bundle.putBoolean(FILTER_APPLIED, mBradTypeHeader.length() > 0);
                        bundle.putBoolean(FILTER_HEADER_DATA_CHANGE, headerChange);
                        Fragment brandFilter = getFragmentByCaller(mFrom, itemClicked, bundle);
                        mLastAddedFragment = brandFilter;
                        mFragmentTransaction.replace(R.id.framelayoutList, brandFilter);
                        mFragmentTransaction.commit();
                        break;
                    case VENDOR:
                        bundle.putSerializable(ADAPTER_LIST, mVendorList);
                        bundle.putSerializable(SELECTED_LIST, mFilterSelectedData.getVendorSelectedList());
                        bundle.putBoolean(HAS_LAZY_LOADING, mVendorList.size() == 500);
                        Fragment vendorFilter = getFragmentByCaller(mFrom, itemClicked, bundle);
                        mLastAddedFragment = vendorFilter;
                        mFragmentTransaction.replace(R.id.framelayoutList, vendorFilter);
                        mFragmentTransaction.commit();
                        break;
                    case PVA_PRODUCT:
                        bundle.putSerializable(ADAPTER_LIST, mPvAProductsList);
                        bundle.putSerializable(SELECTED_LIST, mFilterSelectedData.getPvAProductList());
                        bundle.putBoolean(HAS_LAZY_LOADING, mPvAProductsList.size() == 500);
                        Fragment product = getFragmentByCaller(mFrom, itemClicked, bundle);
                        mLastAddedFragment = product;
                        mFragmentTransaction.replace(R.id.framelayoutList, product);
                        mFragmentTransaction.commit();
                        break;
                }
                Log.e(TAG, "onHierarchyClickListener: mProductHeader: " + mProductHeader + " mLocationHeader: " + mLocationHeader);

            } catch (Exception e) {
                e.printStackTrace();
            }
//        }else {
//            initFilteList();
//        }
    }

    private void initFilteList() {
        mStoreList = (ArrayList<BrandVendorModel>) mLocationViewModel.getmStoreList().getValue();
        mMarketList= (ArrayList<BrandVendorModel>) mLocationViewModel.getmMarketList().getValue();
        mDistrictList= (ArrayList<BrandVendorModel>) mLocationViewModel.getmDistrictList().getValue();
        mZoneList= (ArrayList<BrandVendorModel>) mLocationViewModel.getmZoneList().getValue();
    }

    private void getDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

        builder.setTitle("Filter Alert")
                .setMessage("Please clear " + mPreviousSelection + " level selection")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                        dialog.dismiss();
                    }
                });
        dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

    }

    private int getLastLevelOf(String itemClicked) {
        switch (itemClicked) {
            case DEPT:
            case SUBDEPT:
            case CLASS:
            case SUBCLASS:
            case MC:
                return mHierarchyArrayList.indexOf(MC);
            case ZONE:
            case MARKET:
            case DISTRICT:
            case STORE:
                return mHierarchyArrayList.indexOf(STORE);
            case BRAND_TYPE:
            case BRAND:
                return mHierarchyArrayList.indexOf(BRAND);
            case VENDOR:
                break;
        }
        return 0;
    }

    private void updateFilterHeaderValues(String from, String itemClicked) {
        mLocationHeader = "";
        mProductHeader = "";
        mBradTypeHeader = "";
//        switch (from) {
//            case Dashboard:
//            case MemberDashboard_Ed_Activity.Member_Dashboard:
//            case MEMBER_VISIT_DETAILS:
//            case EasyDaySalesFragment.Sales:
//            case PlannedCardFragment:
//                updateEDHierarchyFilterData(getEDFirstLevelOf(itemClicked), itemClicked);
//                break;
//        }
    }

    private Fragment getFragmentByCaller(String mFrom, String itemClicked, Bundle bundle) {
        if (mFrom != null) {
            switch (mFrom) {
                case "Dashboard":
                    switch (itemClicked) {
                        case ZONE:
                            return ZoneFragment.getInstance(bundle);
                        case MARKET:
                            return MarketFragment.getInstance(bundle);
                        case DISTRICT:
                            return DistrictFragment.getInstance(bundle);
                        case STORE:
                            return StoreFragment.getInstance(bundle);
                    }
                    break;
//                case MemberDashboard_Ed_Activity.Member_Dashboard:
//                case PlannedCardFragment:
//                    switch (itemClicked) {
//                        case ZONE:
//                            return ZoneFragment.getInstance(bundle);
//                        case MARKET:
//                            return MarketFragment.getInstance(bundle);
//                        case DISTRICT:
//                            return DistrictFragment.getInstance(bundle);
//                        case STORE:
//                            return StoreFragment.getInstance(bundle);
//                    }
//                    break;
//                case MEMBER_VISIT_DETAILS:
//                    switch (itemClicked) {
//                        case ZONE:
//                            return ZoneFragment.getInstance(bundle);
//                        case MARKET:
//                            return MarketFragment.getInstance(bundle);
//                        case DISTRICT:
//                            return DistrictFragment.getInstance(bundle);
//                        case STORE:
//                            return StoreFragment.getInstance(bundle);
//                    }
//                    break;
                case "Sales":
                    switch (itemClicked) {
                        case DEPT:
                            return DeptFragment.getInstance(bundle);
                        case SUBDEPT:
                            return SubDeptFragment.getInstance(bundle);
                        case CLASS:
                            return ClassFragment.getInstance(bundle);
                        case SUBCLASS:
                            return SubClassFragment.getInstance(bundle);
                        case MC:
                            return MCFragment.getInstance(bundle);
                        case ZONE:
                            return ZoneFragment.getInstance(bundle);
                        case MARKET:
                            return MarketFragment.getInstance(bundle);
                        case DISTRICT:
                            return DistrictFragment.getInstance(bundle);
                        case STORE:
                            return StoreFragment.getInstance(bundle);
                        case BRAND_TYPE:
                            return BrandTypeFragment.getInstance(bundle);
                        case BRAND:
                            return BrandFragment.getInstance(bundle);
                        case VENDOR:
                            return VendorFragment.getInstance(bundle);
                    }
                    break;
            }
        }
        return null;
    }

    private String getEDFirstLevelOf(String itemClicked) {
        switch (itemClicked) {
            case DEPT:
            case SUBDEPT:
            case CLASS:
            case SUBCLASS:
            case MC:
                return DEPT;
            case ZONE:
            case MARKET:
            case DISTRICT:
            case STORE:
                return ZONE;
            case BRAND_TYPE:
            case BRAND:
                return BRAND_TYPE;
            case VENDOR:
                break;
        }
        return "";
    }


    private void updateEDHierarchyFilterData(String firstLevel, String clickedLevel) {
        mLocationHeader = "";
        mProductHeader = "";
        mBradTypeHeader = "";
        int i = mHierarchyArrayList.indexOf(firstLevel);
        Log.e(TAG, "updateEDHierarchyFilterData: " + mHierarchyArrayList.indexOf(firstLevel) + mHierarchyArrayList.indexOf(clickedLevel));
        do {
            String level = mHierarchyArrayList.get(i);
            switch (level) {
                case DEPT:
                    mProductHeader += getEDSelectedFilterHeaderValue(level);
                    break;
                case SUBDEPT:
                    mProductHeader += getEDSelectedFilterHeaderValue(level);
                    break;
                case CLASS:
                    mProductHeader += getEDSelectedFilterHeaderValue(level);
                    break;
                case SUBCLASS:
                    mProductHeader += getEDSelectedFilterHeaderValue(level);
                    break;
                case MC:
                    break;
                case ZONE:
                    mLocationHeader += getEDSelectedFilterHeaderValue(level);
                    break;
                case MARKET:
                    mLocationHeader += getEDSelectedFilterHeaderValue(level);
                    break;
                case DISTRICT:
                    mLocationHeader += getEDSelectedFilterHeaderValue(level);
                    break;
                case STORE:
                    mLocationHeader += getEDSelectedFilterHeaderValue(level);
                    break;
                case BRAND_TYPE:
                    mBradTypeHeader += getEDSelectedFilterHeaderValue(level);
                    break;
            }
            i++;
        } while (i < mHierarchyArrayList.indexOf(clickedLevel));
    }


    private void networkaccess() {
        manager = getSupportFragmentManager();
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mHierarchyArrayList = new ArrayList<String>();
        userId = sharedPreferences.getString("userId", "");
        bearerToken = sharedPreferences.getString("bearerToken", "");
        geoLevel2Code = sharedPreferences.getString("concept", "");
        lobId = sharedPreferences.getString("lobid", "");
        final Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        queue = new RequestQueue(cache, network);
        queue.start();
        gson = new Gson();
    }

    @OnClick(R.id.button_clearAll)
    public void resetFunction() {
        try {
            mFilterRest = true;
            mFilterData = "";
            mFilterSelectedData = new FilterAppliedData();
            clearSelectValues(mHierarchySelected);
            updateCount(mHierarchySelected, 0);
            updateApplyCount(mFilterSelectedData);
//            initlizeFilterData();  // todo: need to check if relational data is applied then need to set list again to adapter
            for (String h : mHierarchyArrayList) {
                clearSelectValues(h);
                updateCount(h, 0);
            }
//            notifyAllAdapter(mFilterData);
        } catch (Exception e) {
            Log.e(TAG, "resetFunction: ", e);
        }
    }

    private void clearSelectValues(String from) {
        switch (from) {
            case DEPT:
                clearSelectionList(mDepartmentList);
                mDepartmentAPIList = new ArrayList<>();
                notifyAllAdapter(from);
                break;
            case SUBDEPT:
                clearSelectionList(mSubDeptList);
                mSubDeptAPIList = new ArrayList<>();
                notifyAllAdapter(from);
                break;
            case CLASS:
                clearSelectionList(mClassList);
                mClassAPIList = new ArrayList<>();
                notifyAllAdapter(from);
                break;
            case SUBCLASS:
                clearSelectionList(mSubClassList);
                mSubClassAPIList = new ArrayList<>();
                notifyAllAdapter(from);
                break;
            case MC:
                clearSelectionList(mMCList);
                mMCAPIList = new ArrayList<>();
                notifyAllAdapter(from);
                break;
            case ZONE:
                clearSelectionList(mZoneList);
                mZoneAPIList = new ArrayList<>();
                notifyAllAdapter(from);
                break;
            case MARKET:
                clearSelectionList(mMarketList);
                mMarketAPIList = new ArrayList<>();
                notifyAllAdapter(from);
                break;
            case DISTRICT:
                clearSelectionList(mDistrictList);
                mDistrictAPIList = new ArrayList<>();
                notifyAllAdapter(from);
                break;
            case STORE:
                clearSelectionList(mStoreList);
                mStoreAPIList = new ArrayList<>();
                notifyAllAdapter(from);
                break;
            case BRAND_TYPE:
                clearSelectionList(mBrandTypeList);
                mBrandTypeAPIList = new ArrayList<>();
                notifyAllAdapter(from);
                break;
            case BRAND:
                clearSelectionList(mBrandList);
                mBrandAPIList = new ArrayList<>();
                notifyAllAdapter(from);
                break;
            case VENDOR:
                clearSelectionList(mVendorList);
                mVendorAPIList = new ArrayList<>();
                notifyAllAdapter(from);
                break;
        }
    }

    private void clearSelectionList(ArrayList<BrandVendorModel> list) {
        if (list != null) {
            for (BrandVendorModel model : list) {
                model.setSelected(false);
            }

        }
    }

    // please add her your fragment so they will notify adapters;
    private void notifyAllAdapter(String hierarchySelected) {
        switch (hierarchySelected) {
            case DEPT:
                if (mLastAddedFragment instanceof DeptFragment) {
                    ((DeptFragment) mLastAddedFragment).notifyAdapter(mDepartmentList);
                }


                break;
            case SUBDEPT:
                if (mLastAddedFragment instanceof SubDeptFragment) {
                    ((SubDeptFragment) mLastAddedFragment).notifyAdapter(mSubDeptList);
                }

                break;
            case CLASS:
                if (mLastAddedFragment instanceof ClassFragment) {
                    ((ClassFragment) mLastAddedFragment).notifyAdapter(mClassList);
                }

                break;
            case SUBCLASS:
                if (mLastAddedFragment instanceof SubClassFragment) {
                    ((SubClassFragment) mLastAddedFragment).notifyAdapter(mSubClassList);
                }
                break;
            case MC:
                if (mLastAddedFragment instanceof MCFragment) {
                    ((MCFragment) mLastAddedFragment).notifyAdapter(mMCList);
                }

                break;
            case ZONE:
                if (mLastAddedFragment instanceof ZoneFragment) {
                    ((ZoneFragment) mLastAddedFragment).notifyAdapter(mZoneList);
                }

                break;
            case MARKET:
                if (mLastAddedFragment instanceof MarketFragment) {
                    ((MarketFragment) mLastAddedFragment).notifyAdapter(mMarketList);
                }
                break;
            case DISTRICT:
                if (mLastAddedFragment instanceof DistrictFragment) {
                    ((DistrictFragment) mLastAddedFragment).notifyAdapter(mDistrictList);
                }
                break;
            case STORE:
                if (mLastAddedFragment instanceof StoreFragment) {
                    ((StoreFragment) mLastAddedFragment).notifyAdapter(mStoreList);
                }

                break;
            case BRAND_TYPE:
                if (mLastAddedFragment instanceof BrandTypeFragment) {
                    ((BrandTypeFragment) mLastAddedFragment).notifyAdapter(mBrandTypeList);
                }
                break;
            case BRAND:
                if (mLastAddedFragment instanceof BrandFragment) {
                    ((BrandFragment) mLastAddedFragment).notifyAdapter(mBrandList);
                }
                break;
            case VENDOR:
                if (mLastAddedFragment instanceof VendorFragment) {
                    ((VendorFragment) mLastAddedFragment).notifyAdapter(mVendorList);
                }
                break;

        }
    }

    @OnClick(R.id.button_close)
    public void close() {
        onBackPressed();
    }

    @OnClick(R.id.button_apply)
    public void filterApply() {
        try {
            Intent intent = new Intent();
//            mFilterData = prepareFilterHeaderValue();

            if (!mFilterData.isEmpty()) {
//                mFilterData = URLEncoder.encode(mFilterData, "UTF-8");
                intent.putExtra(HEADER_VALUES, mFilterData);
                intent.putExtra(FILTER_LEVEL, mFilterLevel);
                intent.putExtra(FILTER_SELECTED, mFilterSelectedData);
                intent.putExtra("view", view);
                setResult(RESULT_OK, intent);
            } else {
                intent.putExtra(FILTER_CLEARED, mFilterRest);
                intent.putExtra(FILTERED_APPLIED, true);
                intent.putExtra(FILTER_SELECTED, mFilterSelectedData);
                setResult(RESULT_CANCELED, intent);
            }
            Log.e(TAG, "filterApply: header values: " + mFilterData);
            onBackPressed();
        } catch (Exception e) {
            Log.e(TAG, "filterApply: ", e);
        }
    }

    private String prepareFilterHeaderValue() {

//        // here cases as per filter call from and which values to pass for that create method and return value;
//        switch (mFrom) {
//            case Dashboard:
//            case MemberDashboard_Ed_Activity.Member_Dashboard:
//            case PlannedCardFragment:
//            case MEMBER_VISIT_DETAILS:
//            case EasyDaySalesFragment.Sales:
//                for (String s : mHierarchyArrayList) {
//                    mFilterData += getEDSelectedFilterHeaderValue(s);
//                }
//                break;
//
//        }
        return mFilterData;
    }


    private String getEDSelectedFilterHeaderValue(String level) {
        String mFilterData = "";
        ArrayList<BrandVendorModel> list = null;
        if (list != null) list.clear();
        switch (level) {
            case DEPT:
                list = mFilterSelectedData.getDepartmentSelectedList();
                if (list != null && list.size() > 0) {
                    String productHeader = "&prodLevel2Code=";
                    for (int i = 0; i < list.size(); i++) {
                        if (i == list.size() - 1) {
                            productHeader += list.get(i).getProdLevel2Code().replace("&", "%26").replace(" ", "%20");
                        } else {
                            productHeader += list.get(i).getProdLevel2Code().replace("&", "%26").replace(" ", "%20") + ",";
                        }
                    }
                    mFilterData += productHeader;
                    mFilterLevel = 2;
                }
                break;
            case SUBDEPT:
                list = mFilterSelectedData.getSubDeptSelectedList();
                if (list != null && list.size() > 0) {
                    String subdeptheader = "&prodLevel3Code=";
                    for (int i = 0; i < list.size(); i++) {
                        if (i == list.size() - 1) {
                            subdeptheader += list.get(i).getProdLevel3Code().replace("&", "%26").replace(" ", "%20");
                        } else {
                            subdeptheader += list.get(i).getProdLevel3Code().replace("&", "%26").replace(" ", "%20") + ",";
                        }

                    }
                    mFilterData += subdeptheader;
                    mFilterLevel = 3;
                }
                break;
            case CLASS:
                list = mFilterSelectedData.getClassSelectedList();
                if (list != null && list.size() > 0) {
                    String classtHeader = "&prodLevel4Code=";
                    for (int i = 0; i < list.size(); i++) {
                        if (i == list.size() - 1) {
                            classtHeader += list.get(i).getProdLevel4Code().replace("&", "%26").replace(" ", "%20");
                        } else {
                            classtHeader += list.get(i).getProdLevel4Code().replace("&", "%26").replace(" ", "%20") + ",";
                        }
                    }
                    mFilterData += classtHeader;
                    mFilterLevel = 4;
                }
                break;
            case SUBCLASS:
                list = mFilterSelectedData.getSubClassSelectedList();
                if (list != null && list.size() > 0) {
                    String subclassHeader = "&prodLevel5Code=";
                    for (int i = 0; i < list.size(); i++) {
                        if (i == list.size() - 1) {
                            subclassHeader += list.get(i).getProdLevel5Code().replace("&", "%26").replace(" ", "%20");
                        } else {
                            subclassHeader += list.get(i).getProdLevel5Code().replace("&", "%26").replace(" ", "%20") + ",";
                        }
                    }
                    mFilterData += subclassHeader;
                    mFilterLevel = 5;
                }
                break;
            case MC:
                list = mFilterSelectedData.getMCSelectedList();
                if (list != null && list.size() > 0) {
                    String mcHeader = "&prodLevel6Code=";
                    for (int i = 0; i < list.size(); i++) {
                        if (i == list.size() - 1) {
                            mcHeader += list.get(i).getProdLevel6Code().replace("&", "%26").replace(" ", "%20");
                        } else {
                            mcHeader += list.get(i).getProdLevel6Code().replace("&", "%26").replace(" ", "%20") + ",";
                        }
                    }
                    mFilterLevel = 5;
                    mFilterData += mcHeader;
                }
                break;
            case ZONE:
                list = mFilterSelectedData.getZoneSelectedList();
                if (list != null && list.size() > 0) {
                    String zoneED = "&zoneED=";
                    for (int i = 0; i < list.size(); i++) {
                        if (i == list.size() - 1) {
                            zoneED += list.get(i).getZoneED().replace("&", "%26").replace(" ", "%20");
                        } else {
                            zoneED += list.get(i).getZoneED().replace("&", "%26").replace(" ", "%20") + ",";
                        }
                    }
//                    mFilterLevel = ;           // todo: need to check which level to pass for zone store market
                    mFilterData += zoneED;
                }
                break;
            case MARKET:
                list = mFilterSelectedData.getMarketSelectedList();
                if (list != null && list.size() > 0) {
                    String marketED = "&marketED=";
                    for (int i = 0; i < list.size(); i++) {
                        if (i == list.size() - 1) {
                            marketED += list.get(i).getMarketED().replace("&", "%26").replace(" ", "%20");
                        } else {
                            marketED += list.get(i).getMarketED().replace("&", "%26").replace(" ", "%20") + ",";
                        }
                    }
//                    mFilterLevel = 5;           // todo: need to check which level to pass for zone store market
                    mFilterData += marketED;
                }
                break;
            case DISTRICT:
                list = mFilterSelectedData.getDistrictSelectedList();
                if (list != null && list.size() > 0) {
                    String districtED = "&districtED=";
                    for (int i = 0; i < list.size(); i++) {
                        if (i == list.size() - 1) {
                            districtED += list.get(i).getDistrictED().replace("&", "%26").replace(" ", "%20");
                        } else {
                            districtED += list.get(i).getDistrictED().replace("&", "%26").replace(" ", "%20") + ",";
                        }
                    }
//                    mFilterLevel = 5;           // todo: need to check which level to pass for zone store market
                    mFilterData += districtED;
                }
                break;
            case STORE:
                list = mFilterSelectedData.getStoreSelectedList();
                if (list != null && list.size() > 0) {
                    String storeCodeParam = "&storeCode=";
                    for (int i = 0; i < list.size(); i++) {
                        if (i == list.size() - 1) {
                            storeCodeParam += list.get(i).getStoreCodeParam().replace("&", "%26").replace(" ", "%20");
                        } else {
                            storeCodeParam += list.get(i).getStoreCodeParam().replace("&", "%26").replace(" ", "%20") + ",";
                        }
                    }
//                    mFilterLevel = 5;           // todo: need to check which level to pass for zone store market
                    mFilterData += storeCodeParam;
                }
                break;
            case BRAND_TYPE:
                list = mFilterSelectedData.getBrandTypeSelectedList();
                if (list != null && list.size() > 0) {
                    String brandLevel1Code = "&brandLevel1Code=";
                    for (int i = 0; i < list.size(); i++) {
                        if (i == list.size() - 1) {
                            brandLevel1Code += list.get(i).getBrandLevel1Code().replace("&", "%26").replace(" ", "%20");
                        } else {
                            brandLevel1Code += list.get(i).getBrandLevel1Code().replace("&", "%26").replace(" ", "%20") + ",";
                        }
                    }
//                    mFilterLevel = 5;           // todo: need to check which level to pass for zone store market
                    mFilterData += brandLevel1Code;
                }
                break;
            case BRAND:
                list = mFilterSelectedData.getBrandSelectedList();
                if (list != null && list.size() > 0) {
                    String brandLevel3Code = "&brandLevel3Code=";
                    for (int i = 0; i < list.size(); i++) {
                        if (i == list.size() - 1) {
                            brandLevel3Code += list.get(i).getBrandLevel3Code().replace("&", "%26").replace(" ", "%20");
                        } else {
                            brandLevel3Code += list.get(i).getBrandLevel3Code().replace("&", "%26").replace(" ", "%20") + ",";
                        }
                    }
//                    mFilterLevel = 5;           // todo: need to check which level to pass for zone store market
                    mFilterData += brandLevel3Code;
                }
                break;
            case VENDOR://vendorCode

                list = mFilterSelectedData.getVendorSelectedList();
                if (list != null && list.size() > 0) {
                    String vendorCode = "&vendorCode=";
                    for (int i = 0; i < list.size(); i++) {
                        if (i == list.size() - 1) {
                            vendorCode += list.get(i).getVendorCode().replace("&", "%26").replace(" ", "%20");
                        } else {
                            vendorCode += list.get(i).getVendorCode().replace("&", "%26").replace(" ", "%20") + ",";
                        }
                    }
//                    mFilterLevel = 5;           // todo: need to check which level to pass for zone store market
                    mFilterData += vendorCode;
                }
                break;

        }
        return mFilterData;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (queue != null) {
            queue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }
        super.onBackPressed();
    }

    @Override
    public void onItemClick(String level, ArrayList<BrandVendorModel> selectedValues) {
        try {

            updateCount(level, selectedValues.size());
            updateApplyCount(mFilterSelectedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        getEDSelectedFilterHeaderValue(level, selectedValues);
    }

    // add here for count if new hierachy level added
    private void updateApplyCount(FilterAppliedData selectedValues) {
        int count = 0;
        if (mFilterSelectedData != null) {
            count = mFilterSelectedData.getDepartmentSelectedList().size() +
                    mFilterSelectedData.getSubDeptSelectedList().size() +
                    mFilterSelectedData.getClassSelectedList().size() +
                    mFilterSelectedData.getSubClassSelectedList().size() +
                    mFilterSelectedData.getMCSelectedList().size() +
                    mFilterSelectedData.getZoneSelectedList().size() +
                    mFilterSelectedData.getDistrictSelectedList().size() +
                    mFilterSelectedData.getMarketSelectedList().size() +
                    mFilterSelectedData.getStoreSelectedList().size() +
                    mFilterSelectedData.getBrandTypeSelectedList().size() +
                    mFilterSelectedData.getBrandSelectedList().size() +
                    mFilterSelectedData.getVendorSelectedList().size() +
                    mFilterSelectedData.getPvAProductList().size();
        }
        if (count > 0) {
            CharSequence count_char = String.valueOf("APPLY (" + count + ")");
            if (mApplyFilterButton != null) {
                try {
                    SpannableString s = new SpannableString(count_char);
                    s.setSpan(new RelativeSizeSpan(1.0f), 0, 6, SPAN_INCLUSIVE_INCLUSIVE);
                    s.setSpan(new StyleSpan(Typeface.BOLD), 0, 4, SPAN_INCLUSIVE_INCLUSIVE);
                    s.setSpan(new RelativeSizeSpan(0.8f), 6, s.length(), SPAN_INCLUSIVE_INCLUSIVE);
                    mApplyFilterButton.setText(s);
                } catch (Exception e) {
                    e.printStackTrace();
                    mApplyFilterButton.setText(count_char);
                }
            }
        } else {
            SpannableString s = new SpannableString("APPLY");
            s.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length(), 0);
            s.setSpan(new RelativeSizeSpan(1.0f), 0, s.length(), SPAN_INCLUSIVE_INCLUSIVE);
            mApplyFilterButton.setText(s);
        }
    }

    private void updateCount(String level, int size) {
        for (int i = 0; i < mHierarchyArrayList.size(); i++) {
            if (mHierarchyArrayList.get(i).equals(level)) {
                mHierarchySelectCount.set(i, size);
                mHierarchyAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (brandAsync != null) {
            brandAsync.cancel(true);
        }
        if (venderApiTask != null) {
            venderApiTask.cancel(true);
        }
        if (productAsync != null) {
            productAsync.cancel(true);
        }
        if (lovationAsync != null) {
            lovationAsync.cancel(true);
        }
        super.onDestroy();
    }


    @Override
    public void onDataFetchComplete() {

    }
}
