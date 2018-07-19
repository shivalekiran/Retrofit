package com.aperotechnologies.retrofit.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.aperotechnologies.retrofit.service.db.MemberRepo;
import com.aperotechnologies.retrofit.service.model.MemberDetails;

import java.util.ArrayList;
import java.util.List;

public class MemberViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<MemberDetails>> arrayListMutableLiveData = new MutableLiveData<>();
    private MemberRepo mMemberRepo;

    public MemberViewModel(Application application) {
        super(application);
        mMemberRepo = new MemberRepo(application);
        ArrayList<MemberDetails>  arrayList = (ArrayList<MemberDetails>) mMemberRepo.getmMembersDetails().getValue();
        if (arrayList != null) {
            arrayListMutableLiveData.setValue(arrayList);
        }else {
            arrayListMutableLiveData.setValue(new ArrayList<MemberDetails>());

        }
    }

    public void insetMembers(List<MemberDetails> list){
        mMemberRepo.inset(list);
    }
    public LiveData<List<MemberDetails>> getUpdatedMemberList(){
        return  mMemberRepo.getUpdatedMemberList();
    }

    public MutableLiveData<ArrayList<MemberDetails>> getArrayListMutableLiveData() {
        if (arrayListMutableLiveData.getValue() == null) {
            arrayListMutableLiveData.setValue(new ArrayList<MemberDetails>());
        }
        return arrayListMutableLiveData;
    }

    public void setArrayListMutableLiveData(MutableLiveData<ArrayList<MemberDetails>> arrayListMutableLiveData) {
        this.arrayListMutableLiveData = arrayListMutableLiveData;
    }
}
