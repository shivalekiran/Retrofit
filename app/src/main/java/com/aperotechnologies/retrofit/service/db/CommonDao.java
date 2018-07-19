package com.aperotechnologies.retrofit.service.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.aperotechnologies.retrofit.service.model.BrandVendorModel;

import java.util.List;

@Dao
public interface CommonDao {
    @Query("SELECT * FROM BrandVendorModel WHERE storeCodeParam != '' AND storeCode != ''")
    LiveData<List<BrandVendorModel>> getAllStore();

    @Query("SELECT * FROM BrandVendorModel WHERE districtED != ''")
    LiveData<List<BrandVendorModel>> getAllDistrict();

    @Query("SELECT * FROM BrandVendorModel WHERE zoneED!= ''")
    LiveData<List<BrandVendorModel>> getAllZone();

    @Query("SELECT * FROM BrandVendorModel WHERE marketED != ''")
    LiveData<List<BrandVendorModel>> getAllMarket();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocationData(List<BrandVendorModel> models);

    @Query("SELECT * FROM BrandVendorModel WHERE storeCodeParam != '' AND storeCode != ''")
    List<BrandVendorModel> getStore();


    @Query("SELECT * FROM BrandVendorModel WHERE districtED != ''")
    List<BrandVendorModel> getDistricts();

    @Query("SELECT * FROM BrandVendorModel WHERE zoneED!= ''")
    List<BrandVendorModel> getZone();

    @Query("SELECT * FROM BrandVendorModel WHERE marketED != ''")
    List<BrandVendorModel> getMarket();


}
