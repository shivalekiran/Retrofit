package com.aperotechnologies.retrofit.view.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.aperotechnologies.retrofit.service.ApiHandlerAsyncTask;
import com.aperotechnologies.retrofit.service.ConstsCore;
import com.aperotechnologies.retrofit.service.OnFilterDataFetchComplete;
import com.aperotechnologies.retrofit.service.Reusable_Functions;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rkanawade on 06/06/18.
 */

@SuppressWarnings("ALL")
public abstract class FilterDataClass extends AppCompatActivity {
    private static final int PRODUCT_LEVEL = 1;
    private static final int ED_LOCATION_LEVEL = 1;
    private static final int ED_BRAND_LEVEL = 1;
    private final int REQUEST_TYPE_PRODUCT = 1;
    private final int REQUEST_TYPE_LOCATION = 2;
    private final int REQUEST_TYPE_VENDOR = 3;
    private final int REQUEST_TYPE_BRAND = 4;
//    private FilterSharedPreferences filterSharedPreferences;
    private int offset = 0;
    private final int limit = 500;
    private final int offsetED = 0;
    protected static boolean product;
    protected static boolean location;
    protected static boolean brand = false;
    protected static boolean vendor = false;
    protected ApiHandlerAsyncTask venderApiTask;
    protected ApiHandlerAsyncTask productAsync;
    protected ApiHandlerAsyncTask lovationAsync;
    protected ApiHandlerAsyncTask brandAsync;
    private OnFilterDataFetchComplete onFilterDataFetchComplete;
    private String bearertoken, userId,geoLevel2Code,lobId;
    private final String TAG = FilterDataClass.class.getSimpleName();
    public boolean locationUpdate = false;
    public boolean productUpdate = false;
    private RequestQueue queue;

    protected void updateEasyDayFilterData(Context context, String bearToken, String userId, RequestQueue queue, String geoLevel2Code, String lobId) {
        try {
            if (context instanceof OnFilterDataFetchComplete)
                onFilterDataFetchComplete = (OnFilterDataFetchComplete) context;
            this.bearertoken = bearToken;
            this.userId = userId;
            this.geoLevel2Code = geoLevel2Code;
            this.queue = queue;
            this.lobId = lobId;

            updateProductHierarchy(PRODUCT_LEVEL);
            updateEDLocationFilterData(ED_LOCATION_LEVEL);
            updateEDBrandFilterData(ED_BRAND_LEVEL);
            updateEDVenderFilterData();
            updateStoreDetails();
        } catch (Exception e) {
            Log.e("", "updateEasyDayFilterData: Error while updating filter data", e);
        }
    }

    private void updateStoreDetails() {
        if (Reusable_Functions.chkStatus(this))
        {
            String storeUrl = ConstsCore.web_url + "/v1/display/storeselection/" + userId + "?geoLevel2Code=" + geoLevel2Code
                    + "&lobId=" + lobId;
            Log.e("StoreAPI: ", "" + storeUrl);
            final JsonArrayRequest postRequest = new JsonArrayRequest(Request.Method.GET, storeUrl,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray array) {
                            if (array != null && array.length() > 0)
                            {

//                                fillSharePreference(0, array.toString()); /// default for store - 0
                                updateEDSharePreference(0,array.toString());
                            }


//                            requestBrandType();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    params.put("Authorization", "Bearer " + bearertoken);
                    return params;

                }
            };
            postRequest.setRetryPolicy(Reusable_Functions.getPolicy());
            queue.add(postRequest);

        }
    }

    private void updateProductHierarchy(int level) {
        String[] networkParam = new String[2];
        networkParam[0] = getHierarchyUrl(level);
        networkParam[1] = bearertoken;
        productAsync = new ApiHandlerAsyncTask(new ApiResponseHandler(), level, REQUEST_TYPE_PRODUCT);
        productAsync.execute(networkParam);
    }

    private String getHierarchyUrl(int level) {
        return ConstsCore.web_url + "/v1/display/producthierarchyDisplayEDSF/"
                + userId
                + "?offset=" + offsetED
                + "&limit=" + limit
                + "&level=" + level;

    }

    @SuppressLint("HandlerLeak")
    private class ApiResponseHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String response = (String) msg.obj;
            int level = msg.arg1;
            int requestType = msg.arg2;
//            if (response.length() == limit)   todo: enable layzy loading for this level list

            if (requestType == REQUEST_TYPE_PRODUCT) {
                Log.e(TAG, "handleMessage:ED Product level: " + level + " Respo:" + response);
                updateEDSharePreference(level, response);
                if (level < 5) {
                    updateProductHierarchy(level + 1);
                } else {
                    product = true;
                }
            }
            if (requestType == REQUEST_TYPE_LOCATION) {
                Log.e(TAG, "handleMessage:ED Location level: " + level + " Respo:" + response);
                updateEDSharePreference(-level, response);
                if (level < 4) {
                    updateEDLocationFilterData(level + 1);
                } else {
                    location = true;
                }
            }

            if (requestType == REQUEST_TYPE_BRAND) {
                Log.e(TAG, "handleMessage:ED Brand level: " + level + " Respo:" + response);
                updateEDSharePreference(level == 1 ? 6 : 7, response);
                if (level < 2) {
                    updateEDBrandFilterData(level + 1);
                } else {
                    brand = true;
                }
            }
            if (requestType == REQUEST_TYPE_VENDOR) {
                Log.e(TAG, "handleMessage:ED Vendor level: " + level + " Respo:" + response);
                updateEDSharePreference(8, response);
                vendor = true;
            }
            if (onFilterDataFetchComplete != null) {
                onFilterDataFetchComplete.onDataFetchComplete();

            }

        }
    }

    private void updateEDVenderFilterData() {
        final String[] networkParam = new String[2];
        networkParam[0] = this.getVenderUrl();
        networkParam[1] = bearertoken;
        venderApiTask = new ApiHandlerAsyncTask(new ApiResponseHandler(), 0, REQUEST_TYPE_VENDOR);
        venderApiTask.execute(networkParam);
    }

    private String getVenderUrl() {
        int offset = 0;
        return ConstsCore.web_url + "/v1/display/VendorEDSF/" + userId + "?offset=" + offset + "&limit=" + limit;
    }

    private void updateEDBrandFilterData(final int edBrandLevel) {
        final String[] networkParam = new String[2];
        networkParam[0] = this.getBrandUrl(edBrandLevel);
        networkParam[1] = bearertoken;
        brandAsync = new ApiHandlerAsyncTask(new ApiResponseHandler(), edBrandLevel, REQUEST_TYPE_BRAND);
        brandAsync.execute(networkParam);
    }

    private String getBrandUrl(int edBrandLevel) {
        int offset = 0;
        return ConstsCore.web_url + "/v1/display/brandEDSF/" + userId + "?level=" + edBrandLevel + "&offset=" + offset + "&limit=" + limit;
    }

    private void updateEDLocationFilterData(int level) {
        String[] networkParam = new String[2];
        networkParam[0] = getStoreUrl(level);
        networkParam[1] = bearertoken;
        lovationAsync = new ApiHandlerAsyncTask(new ApiResponseHandler(), level, REQUEST_TYPE_LOCATION);
        lovationAsync.execute(networkParam);
    }

    private String getStoreUrl(int level) {
        return ConstsCore.web_url + "/v1/display/storehierarchyEDSF/"
                + userId
                + "?level=" + level;
    }

    private void updateEDSharePreference(int level, String list) {
//        switch (level) {
//            case 1:
//                filterSharedPreferences.put(FilterSharedPreferences.Key.ED_DEPT, list);
//                break;
//            case 2:
//                filterSharedPreferences.put(FilterSharedPreferences.Key.ED_SUB_DEPT, list);
//                break;
//            case 3:
//                filterSharedPreferences.put(FilterSharedPreferences.Key.ED_CLASS, list);
//                break;
//            case 4:
//                filterSharedPreferences.put(FilterSharedPreferences.Key.ED_SUB_CLASS, list);
//                break;
//            case 5:
//                filterSharedPreferences.put(FilterSharedPreferences.Key.ED_MC, list);
//                break;
//
//            case -1:
//                filterSharedPreferences.put(FilterSharedPreferences.Key.ZONE, list);
//                break;
//            case -2:
//                filterSharedPreferences.put(FilterSharedPreferences.Key.ED_MARKER, list);
//                break;
//            case -3:
//                filterSharedPreferences.put(FilterSharedPreferences.Key.ED_DISTRICT, list);
//                break;
//            case -4:
//                filterSharedPreferences.put(FilterSharedPreferences.Key.STORE, list);
//                break;
//
//            case 6:
//                filterSharedPreferences.put(FilterSharedPreferences.Key.ED_BRAND_TYPE, list);
//                break;
//            case 7:
//                filterSharedPreferences.put(FilterSharedPreferences.Key.ED_BRAND, list);
//                break;
//            case 8:
//                filterSharedPreferences.put(FilterSharedPreferences.Key.ED_VENDOR, list);
//                break;
//
//            case 0:
//                filterSharedPreferences.put(FilterSharedPreferences.Key.ED_STORE,list);
//                break;
//        }
//        filterSharedPreferences.put(FilterSharedPreferences.Key.LAST_UPDATE, System.currentTimeMillis());
    }


}
