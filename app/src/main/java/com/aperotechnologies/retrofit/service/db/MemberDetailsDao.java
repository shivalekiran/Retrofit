package com.aperotechnologies.retrofit.service.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.aperotechnologies.retrofit.service.model.MemberDetails;

import java.util.List;

@Dao
public interface MemberDetailsDao {

    @Query("SELECT * FROM MemberDetails")
    LiveData<List<MemberDetails>> getAllMembers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insetMembers(List<MemberDetails> details);

    @Delete
    void deleteMemers(MemberDetails... details);

}
