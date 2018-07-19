package com.aperotechnologies.retrofit.service.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.aperotechnologies.retrofit.service.model.Login_StoreList;

@Dao
public interface LoginDao {
    @Query("SELECT * FROM Login_StoreList")
    LiveData<Login_StoreList> getuserDetails();

    @Insert
    void insetLogin(Login_StoreList... login_storeLists);

}
