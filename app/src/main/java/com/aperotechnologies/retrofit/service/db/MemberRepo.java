package com.aperotechnologies.retrofit.service.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.aperotechnologies.retrofit.service.model.MemberDetails;

import java.util.List;

public class MemberRepo {
    private MemberDetailsDao detailsDao;
    private LiveData<List<MemberDetails>> mMembersDetails = new MutableLiveData<>();

    public MemberRepo(Context context) {
        AppDatabase appDatabase = AppDatabase.getDatabase(context);
        this.detailsDao = appDatabase.memberDetailsDao();
    }

    public LiveData<List<MemberDetails>> getmMembersDetails() {
        if (mMembersDetails == null){
            mMembersDetails = new MutableLiveData<>();
        }
        return mMembersDetails;
    }

    public void inset(List<MemberDetails> memberDetails) {
        new insertAsyncTask(detailsDao).execute(memberDetails);
    }

    public LiveData<List<MemberDetails>> getUpdatedMemberList(){
        return detailsDao.getAllMembers();

    }

    private static class insertAsyncTask extends AsyncTask<List<MemberDetails>, Void, Void> {

        private MemberDetailsDao memberDetailsDao;

        insertAsyncTask(MemberDetailsDao dao) {
            memberDetailsDao = dao;
        }

        @Override
        protected Void doInBackground(List<MemberDetails>... params) {
            memberDetailsDao.insetMembers(params[0]);
            return null;
        }
    }
}
