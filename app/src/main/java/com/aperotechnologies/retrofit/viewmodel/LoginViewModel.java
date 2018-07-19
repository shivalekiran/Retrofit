package com.aperotechnologies.retrofit.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;

import com.aperotechnologies.retrofit.service.db.LoginRepo;
import com.aperotechnologies.retrofit.service.model.Login_StoreList;

public class LoginViewModel extends AndroidViewModel {

    LoginRepo mLoginRepo;
    LiveData<Login_StoreList> loginMutableData = new MutableLiveData<>();
    ObservableBoolean isLoading;

    public LoginViewModel(Application application) {
        super(application);
        mLoginRepo = new LoginRepo(application);
        loginMutableData = new MutableLiveData<>();
        loginMutableData = mLoginRepo.getmLoginData();
    }

    public void insert(Login_StoreList storeList){
        mLoginRepo.inset(storeList);
    }
    public ObservableBoolean getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(ObservableBoolean isLoading) {
        this.isLoading = isLoading;
    }

    public LiveData<Login_StoreList> getLoginMutableData() {
        if (loginMutableData == null) {
            loginMutableData = new MutableLiveData<>();
        }
        return loginMutableData;
    }

    public void setLoginMutableData(MutableLiveData<Login_StoreList> loginMutableData) {
        this.loginMutableData = loginMutableData;
    }
}
