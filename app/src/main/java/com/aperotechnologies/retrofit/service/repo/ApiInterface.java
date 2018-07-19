package com.aperotechnologies.retrofit.service.repo;

import com.aperotechnologies.retrofit.service.model.BrandVendorModel;
import com.aperotechnologies.retrofit.service.model.Login_StoreList;
import com.aperotechnologies.retrofit.service.model.MemberDetails;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;


public interface ApiInterface {
    @Headers("Content-Type: application/json")
    @GET("/v1/display/customerdetailEDSF/ARUNTEST")
    Call<List<MemberDetails>> getMemberDetails(@Header("Authorization")  String Authorization, @QueryMap Map<String, String> options);
//            , @Query("view") String view
//            , @Query("geoLevel2Code") String geoLevel2Code
//            , @Query("lobId") String lobId);

    @GET("/v1/login")
    Call<Login_StoreList> login(@Header("Authorization") String token);

    @GET("/v1/display/storehierarchyEDSF/ARUNTEST")
    Call<List<BrandVendorModel>> getStoreDetails(@Header("Authorization")  String Authorization, @QueryMap Map<String, String> options);
}