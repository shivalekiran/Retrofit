package com.aperotechnologies.retrofit.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.aperotechnologies.retrofit.service.db.CommonRepository;
import com.aperotechnologies.retrofit.service.model.BrandVendorModel;

import java.util.ArrayList;
import java.util.List;

public class LocationViewModel extends AndroidViewModel {

    private MutableLiveData<List<BrandVendorModel>> mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<BrandVendorModel>> mStoreList = new MutableLiveData<>();
    private MutableLiveData<List<BrandVendorModel>> mDistrictList = new MutableLiveData<>();
    private MutableLiveData<List<BrandVendorModel>> mZoneList = new MutableLiveData<>();
    private MutableLiveData<List<BrandVendorModel>> mMarketList = new MutableLiveData<>();
    private CommonRepository commonRepository;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        commonRepository = new CommonRepository(application);
        List<BrandVendorModel> updatedStoreList = commonRepository.getUpdatedStoreList().getValue();
        List<BrandVendorModel> updatedMarketList = commonRepository.getUpdatedMarketList().getValue();
        List<BrandVendorModel> updatedZoneList= commonRepository.getUpdatedZone().getValue();
        List<BrandVendorModel> updatedDistrict = commonRepository.getUpdatedDistrictList().getValue();

        if (updatedStoreList == null) {
            mutableLiveData.setValue(new ArrayList<BrandVendorModel>());
            mStoreList.setValue(new ArrayList<BrandVendorModel>());
            mMarketList.setValue(new ArrayList<BrandVendorModel>());
            mDistrictList.setValue(new ArrayList<BrandVendorModel>());
            mZoneList.setValue(new ArrayList<BrandVendorModel>());
        } else {
            mutableLiveData.setValue(updatedStoreList);
            mStoreList.setValue(updatedStoreList);
            mZoneList.setValue(updatedZoneList);
            mDistrictList.setValue(updatedDistrict);
            mMarketList.setValue(updatedMarketList);
        }
    }

    public MutableLiveData<List<BrandVendorModel>> getmStoreList() {
        if (mStoreList.getValue() == null) mStoreList.setValue(commonRepository.getStoreList());
        return mStoreList;
    }

    public MutableLiveData<List<BrandVendorModel>> getmDistrictList() {
        return mDistrictList;
    }

    public MutableLiveData<List<BrandVendorModel>> getmZoneList() {
        return mZoneList;
    }

    public MutableLiveData<List<BrandVendorModel>> getmMarketList() {
        return mMarketList;
    }

    public MutableLiveData<List<BrandVendorModel>> getMutableLiveData() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
            mutableLiveData.setValue(new ArrayList<BrandVendorModel>());
        }
        return mutableLiveData;
    }

    public void insetStoreList(int level, List<BrandVendorModel> models) {
        commonRepository.insetStoreList(level, models);
    }

    public LiveData<List<BrandVendorModel>> getUpdatedLocationStoreList() {
        return commonRepository.getUpdatedStoreList();

    }

    public LiveData<List<BrandVendorModel>> getUpdatedDistrictList() {
        return commonRepository.getUpdatedDistrictList();
    }

    public LiveData<List<BrandVendorModel>> getUpdatedZoneList() {
        return commonRepository.getUpdatedZone();

    }

    public LiveData<List<BrandVendorModel>> getUpdatedMarketList() {
        return commonRepository.getUpdatedMarketList();
    }
}
