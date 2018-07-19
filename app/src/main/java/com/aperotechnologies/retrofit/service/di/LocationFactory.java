package com.aperotechnologies.retrofit.service.di;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.aperotechnologies.retrofit.viewmodel.LocationViewModel;

public class LocationFactory implements ViewModelProvider.Factory {
    Application application;
    public LocationFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LocationViewModel.class))
            return (T) new LocationViewModel(application);
        throw new IllegalArgumentException("cant cast to member view model");
    }
}