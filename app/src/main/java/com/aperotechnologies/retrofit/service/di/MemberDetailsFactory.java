package com.aperotechnologies.retrofit.service.di;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.aperotechnologies.retrofit.viewmodel.MemberViewModel;

public class MemberDetailsFactory implements ViewModelProvider.Factory {
    Application application;
    public MemberDetailsFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MemberViewModel.class))
            return (T) new MemberViewModel(application);
        throw new IllegalArgumentException("cant cast to member view model");
    }
}
