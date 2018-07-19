package com.aperotechnologies.retrofit.service.di;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.aperotechnologies.retrofit.viewmodel.LoginViewModel;

public  class LoginFactory implements ViewModelProvider.Factory{
    Application application;
    public LoginFactory(Application context) {
        application = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)){
            return (T) new LoginViewModel(application);
        }
        throw new IllegalArgumentException("cant cant to Login model");
    }
}