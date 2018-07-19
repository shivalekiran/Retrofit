package com.aperotechnologies.retrofit.service.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.aperotechnologies.retrofit.service.model.BrandVendorModel;
import com.aperotechnologies.retrofit.service.model.Login_StoreList;
import com.aperotechnologies.retrofit.service.model.MemberDetails;

@Database(entities = {Login_StoreList.class
        , MemberDetails.class
        , BrandVendorModel.class}, version = 1, exportSchema = false)
public abstract class AppDatabase  extends RoomDatabase{

    private final static String DATABASE_NAME = "SMDM";
    private static AppDatabase INSTANCE;
    abstract LoginDao loginDao();
    abstract MemberDetailsDao memberDetailsDao();
    abstract CommonDao dao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
