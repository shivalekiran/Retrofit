package com.aperotechnologies.retrofit.service.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.aperotechnologies.retrofit.service.model.BrandVendorModel;

import java.util.List;

public class CommonRepository {
    private CommonDao dao;

    public CommonRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        dao = appDatabase.dao();
    }

    public void insetStoreList(int level, List<BrandVendorModel> list) {
        new insertAsyncTask(dao, level).execute(list);
    }

    public LiveData<List<BrandVendorModel>> getUpdatedStoreList(){
        return dao.getAllStore();
    }

    public LiveData<List<BrandVendorModel>> getUpdatedDistrictList() {
        return dao.getAllDistrict();
    }

    public LiveData<List<BrandVendorModel>> getUpdatedMarketList() {
        return dao.getAllMarket();
    }

    public List<BrandVendorModel> getMarketList() {
        return dao.getMarket();
    }

    public List<BrandVendorModel> getZoneList(){
        return dao.getZone();
    }

//
    public List<BrandVendorModel> getStoreList(){
        return dao.getStore();
    }

    public List<BrandVendorModel> getDistrictList() {
        return dao.getDistricts();
    }
    public LiveData<List<BrandVendorModel>> getUpdatedZone(){
        return dao.getAllZone();
    }


    private static class insertAsyncTask extends AsyncTask<List<BrandVendorModel>, Void, Void> {

        private CommonDao dao;
        private int level;
        insertAsyncTask(CommonDao dao, int level) {
            this.dao = dao;
            this.level = level;
        }

        @Override
        protected Void doInBackground(List<BrandVendorModel>... params) {
            dao.insertLocationData(params[0]);
            return null;
        }
    }
}
