package com.aperotechnologies.retrofit.view.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.aperotechnologies.retrofit.R;
import com.aperotechnologies.retrofit.databinding.LoginActivityBinding;
import com.aperotechnologies.retrofit.service.FilterConstant;
import com.aperotechnologies.retrofit.service.db.AppDatabase;
import com.aperotechnologies.retrofit.service.di.LocationFactory;
import com.aperotechnologies.retrofit.service.di.LoginFactory;
import com.aperotechnologies.retrofit.service.di.MemberDetailsFactory;
import com.aperotechnologies.retrofit.service.model.BrandVendorModel;
import com.aperotechnologies.retrofit.service.model.Login_StoreList;
import com.aperotechnologies.retrofit.service.model.MemberDetails;
import com.aperotechnologies.retrofit.service.repo.ApiClient;
import com.aperotechnologies.retrofit.service.repo.ApiInterface;
import com.aperotechnologies.retrofit.view.adapter.MemberCustDtlsAdapter;
import com.aperotechnologies.retrofit.viewmodel.LocationViewModel;
import com.aperotechnologies.retrofit.viewmodel.LoginViewModel;
import com.aperotechnologies.retrofit.viewmodel.MemberViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private RecyclerView recycler_mbr_custdetails;
    private List<MemberDetails> arr_custdetails;
    private MemberCustDtlsAdapter custDtlsAdapter;
    private ApiInterface apiService;
    private Call<List<MemberDetails>> memberCall;
    private Gson gson;
    public static String bearerToken;
    private LoginViewModel mLoginViewModel;
    LoginActivityBinding activityLoginBinding;
    private MemberViewModel mMemberViewModel;
    private LocationViewModel  mLocationViewModel;
    private Call<Login_StoreList> loginStoreListCall;
    private AppDatabase databaseInstance;
    private SharedPreferences sharedPreferences;
    public  static  final  String LOGIN = "LOGIN";
    public  static  final  String TOKEN = "TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        databaseInstance = AppDatabase.getDatabase(this);
        databaseInstance.clearAllTables();

        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.login_activity);
        recycler_mbr_custdetails = activityLoginBinding.recyclerMbrCustdetails;
        recycler_mbr_custdetails.setLayoutManager(new LinearLayoutManager(this));

        LoginFactory factory = new LoginFactory(getApplication());
        mLoginViewModel = ViewModelProviders.of(this, factory).get(LoginViewModel.class);
        mLoginViewModel.setIsLoading(new ObservableBoolean(true));
        activityLoginBinding.setLoginModel(mLoginViewModel);

        MemberDetailsFactory detailsFactory = new MemberDetailsFactory(getApplication());
        mMemberViewModel = ViewModelProviders.of(this, detailsFactory).get(MemberViewModel.class);
        mMemberViewModel.getArrayListMutableLiveData().observe(this, new Observer<ArrayList<MemberDetails>>() {
            @Override
            public void onChanged(@Nullable ArrayList<MemberDetails> memberDetails) {
                if (memberDetails != null && memberDetails.size() > 0) {
                    arr_custdetails.addAll(memberDetails);
                    custDtlsAdapter.notifyDataSetChanged();
                    if (arr_custdetails.size() < 100) {
                        callMemberDetails(arr_custdetails.size() + "");
                    }else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }, 1*1000);
                    }
                    mLoginViewModel.getIsLoading().set(false);
                } else {

                }
            }
        });

        arr_custdetails = mMemberViewModel.getArrayListMutableLiveData().getValue();
        custDtlsAdapter = new MemberCustDtlsAdapter(this, arr_custdetails);
        recycler_mbr_custdetails.setAdapter(custDtlsAdapter);
        gson = new Gson();
        apiService = ApiClient.getClient().create(ApiInterface.class);

        if (savedInstanceState == null && bearerToken == null) {
            boolean isLogin = sharedPreferences.getBoolean(LOGIN, false);
            String token = sharedPreferences.getString(TOKEN, null);

            if (!isLogin && token == null){

                final String auth_code = "Basic " + Base64.encodeToString((getString(R.string.ARUNTEST)+ ":" + "A123").getBytes(), Base64.NO_WRAP);
                loginStoreListCall = apiService.login(auth_code);
                loginStoreListCall.enqueue(new Callback<Login_StoreList>() {
                    @Override
                    public void onResponse(Call<Login_StoreList> call, Response<Login_StoreList> response) {
                        Login_StoreList login_storeList = response.body();
                        if (login_storeList.getBearerToken() != null) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(LOGIN, true);
                            editor.putString(TOKEN, login_storeList.getBearerToken());
                            editor.apply();
                            mLoginViewModel.insert(login_storeList);
                            bearerToken = login_storeList.getBearerToken();
                            Toast.makeText(LoginActivity.this, "Login successfully...", Toast.LENGTH_SHORT).show();
                            callMemberDetails("0");
                        } else {
                            Toast.makeText(LoginActivity.this, "Login fails", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Login_StoreList> call, Throwable t) {
                        loginStoreListCall = apiService.login(auth_code);
                    }
                });
            }else {
                bearerToken = sharedPreferences.getString(TOKEN, "");
                GetFilterData();
                callMemberDetails("0");
            }
        } else {
            mLoginViewModel.getIsLoading().set(false);
        }

        mMemberViewModel.getUpdatedMemberList().observe(this, new Observer<List<MemberDetails>>() {
            @Override
            public void onChanged(@Nullable List<MemberDetails> memberDetails) {
                if (memberDetails != null) {
                    Log.d(TAG, "onChanged() returned: count of room db members :" + memberDetails.size());
                    mMemberViewModel.getArrayListMutableLiveData().setValue(new ArrayList<MemberDetails>(memberDetails));
                }
            }
        });

        initLocationFactoryViewModel();

        mLocationViewModel.getUpdatedLocationStoreList().observe(this, new Observer<List<BrandVendorModel>>() {
            @Override
            public void onChanged(@Nullable List<BrandVendorModel> models) {
                Log.e(TAG, "onChanged: Store List Count: "+ (models != null ? models.size() : 0));
            }
        });
        mLocationViewModel.getUpdatedDistrictList().observe(this, new Observer<List<BrandVendorModel>>() {
            @Override
            public void onChanged(@Nullable List<BrandVendorModel> models) {
                Log.e(TAG, "onChanged: District List Count: "+ (models != null ? models.size() : 0));
            }
        });
        mLocationViewModel.getUpdatedMarketList().observe(this, new Observer<List<BrandVendorModel>>() {
            @Override
            public void onChanged(@Nullable List<BrandVendorModel> models) {
                Log.e(TAG, "onChanged: Market List Count: "+ (models != null ? models.size() : 0));
            }
        });
        mLocationViewModel.getUpdatedZoneList().observe(this, new Observer<List<BrandVendorModel>>() {
            @Override
            public void onChanged(@Nullable List<BrandVendorModel> models) {
                Log.e(TAG, "onChanged: Zone List Count: "+ (models != null ? models.size() : 0));
            }
        });
    }

    private void initLocationFactoryViewModel() {
        LocationFactory factory = new LocationFactory(getApplication());
        mLocationViewModel = ViewModelProviders.of(this, factory).get(LocationViewModel.class);
        mLocationViewModel.getMutableLiveData().observe(this, new Observer<List<BrandVendorModel>>() {
            @Override
            public void onChanged(@Nullable List<BrandVendorModel> models) {
                if (models != null) {
                    Log.d(TAG, "onChanged() returned: " + models.size());
                }
            }
        });
    }

    private void GetFilterData() {
        getLocationData(1);

    }

    private void getLocationData(final int level) {
        final Map<String, String> data = new HashMap<>();
        data.put("level", String.valueOf(level));
        Call<List<BrandVendorModel>> locationCall = apiService.getStoreDetails(bearerToken, data);
        Request locationRequest = locationCall.request();
        locationCall.enqueue(new Callback<List<BrandVendorModel>>() {
            @Override
            public void onResponse(Call<List<BrandVendorModel>> call, Response<List<BrandVendorModel>> response) {
                if (response.body() != null) {
                    List<BrandVendorModel> list = response.body();
                    if (list != null) {
                        Log.e(TAG, "onResponse: "+ list.size() );
                        mLocationViewModel.insetStoreList(level, list);
                        if (level < 4)
                            getLocationData(level + 1);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BrandVendorModel>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;//super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e(TAG, "onOptionsItemSelected: calling filter" );
        Intent intent = new Intent(LoginActivity.this, Filter.class);
        intent.putExtra(FilterConstant.FILTER_ARRAY, new ArrayList<String>(Arrays.asList(FilterConstant.mLocationHierarchyList)));
        intent.putExtra(FilterConstant.CHECK_FROM, "Dashboard");
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    private void callMemberDetails(String offset) {
        final Map<String, String> data = new HashMap<>();
        data.put("view", "LD");//ED-SF&lobId=1
        data.put("geoLevel2Code", "ED-SF");
        data.put("lobId", "1");
        data.put("limit", "50");
        data.put("offset", offset);

        if (apiService != null) {
            memberCall = apiService.getMemberDetails(bearerToken, data);
            Request request = memberCall.request();
            Log.d(TAG, "callMemberDetails() returned: URL: " + request.url());
            Log.d(TAG, "callMemberDetails() returned: header : " + request.header("Authorization"));
        }
        if (memberCall != null) {
            memberCall.enqueue(new Callback<List<MemberDetails>>() {
                @Override
                public void onResponse(Call<List<MemberDetails>> call, Response<List<MemberDetails>> response) {
                    List<MemberDetails> memberDetails = response.body();
                    if (memberDetails != null) {
                        Log.d(TAG, "onResponse() returned: " + memberDetails.size());
                        mMemberViewModel.insetMembers(memberDetails);
//                        mMemberViewModel.getArrayListMutableLiveData().setValue(memberDetails);
                    }

                }

                @Override
                public void onFailure(Call<List<MemberDetails>> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, "onFailure: ", t);
                }
            });
        }
    }
}
