package com.aperotechnologies.retrofit.service.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.aperotechnologies.retrofit.service.model.Login_StoreList;

public class LoginRepo {
    private LoginDao loginDao;
    LiveData<Login_StoreList> mLoginData= new MutableLiveData<>();

    public LoginRepo(Context context) {
        AppDatabase appDatabase = AppDatabase.getDatabase(context);
        this.loginDao = appDatabase.loginDao();
        this.mLoginData = loginDao.getuserDetails();
    }

    public LiveData<Login_StoreList> getmLoginData() {
        return mLoginData;
    }

    public void inset(Login_StoreList mLoginData) {
         new insertAsyncTask(loginDao).execute(mLoginData);
    }
    private static class insertAsyncTask extends AsyncTask<Login_StoreList, Void, Void> {

        private LoginDao mAsyncTaskDao;

        insertAsyncTask(LoginDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Login_StoreList... params) {
            mAsyncTaskDao.insetLogin(params[0]);
            return null;
        }
    }
}
