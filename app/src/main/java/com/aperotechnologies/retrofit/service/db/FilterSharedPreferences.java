package com.aperotechnologies.retrofit.service.db;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * Created by kshivale on 22/02/18.
 */
// Share preference class for filter api's
@SuppressWarnings("ALL")
public class FilterSharedPreferences {

    private static final String SETTINGS_NAME = "FilterSharedPreferences";
    private final android.content.SharedPreferences mPreferences;
    private android.content.SharedPreferences.Editor mEditor;
    private boolean mBulkUpdate = false;
    private static FilterSharedPreferences singleSharedPreferencesInstance;

    // call atleast once
    public static FilterSharedPreferences getInstance(Context context) {
        if (singleSharedPreferencesInstance == null) {
            singleSharedPreferencesInstance = new FilterSharedPreferences(context.getApplicationContext());
        }
        return singleSharedPreferencesInstance;
    }
    //get obect of sheare preference
    public static FilterSharedPreferences getInstance() {
        return singleSharedPreferencesInstance;
    }

    //private constructore
    private FilterSharedPreferences(Context context) {
        mPreferences = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
    }

    // preferece keys todo: add keys here if you want add more data with key
    public static class Key
    {
        // Keys for Easy day filter
        public static final String ED_DEPT = "ed_dept";
        public static final String ED_SUB_DEPT = "ed_sub_dept";
        public static final String ED_CLASS = "ed_class";
        public static final String ED_SUB_CLASS = "ed_sub_class";
        public static final String ED_MC = "ed_mc";
        public static final String ED_MARKER= "market";
        public static final String ED_VENDOR = "vendor";
        public static final String ED_BRAND_TYPE = "brand_type";
        public static final String ED_BRAND = "brand";
        public static final String ED_DISTRICT = "district";
        // keys for central , big bazzar , fbb , hometown , ezone and brand factory concept filter
        public static final String DEPT = "dept";
        public static final String SUB_DEPT = "sub_dept";
        public static final String CLASS = "class";
        public static final String SUB_CLASS = "sub_class";
        public static final String MC = "mc";
        // Keys for ezone filter
        public static final String EZ_REGION = "ez_region";
        public static final String EZ_STORE = "ez_store";

        public static final String LAST_UPDATE= "last_update";
        public static final String IS_UPDATE= "is_update";
        public static final String PVA_PRODUCT = "pva_product";
        // fot Manager View Acitity filter, easy day;
        public static final String ZONE = "zone";
        public static final String CITY = "city";
        public static final String STORE = "store";
        public static final String MARKET = "market";
        public static final String DISTRICT = "district";
        public static final String ED_STORE = "ed_store";

    }

    public void put(String key, String val)
    {
        doEdit();
        mEditor.putString(key, val);
        doCommit();
    }

    public void put(String key, boolean val)
    {
        doEdit();
        mEditor.putBoolean(key, val);
        doCommit();
    }

    public void put(String key, float val)
    {
        doEdit();
        mEditor.putFloat(key, val);
        doCommit();
    }

    public void put(String key, double val)
    {
        doEdit();
        mEditor.putString(key, String.valueOf(val));
        doCommit();
    }

    public void put(String key, long val)
    {
        doEdit();
        mEditor.putLong(key, val);
        doCommit();
    }

    public void put(String key, int val) {
        doEdit();
        mEditor.putInt(key, val);
        doCommit();
    }

    public String getString(String key, String defaultValue) {
        return mPreferences.getString(key, defaultValue);
    }

    public String getString(String key)
    {
        return mPreferences.getString(key, null);
    }

    public int getInt(String key)
    {
        return mPreferences.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue)
    {
        return mPreferences.getInt(key, defaultValue);
    }

    public long getLong(String key)
    {
        return mPreferences.getLong(key, 0);
    }

    public long getLong(String key, long defaultValue)
    {
        return mPreferences.getLong(key, defaultValue);
    }

    public float getFloat(String key)
    {
        return mPreferences.getFloat(key, 0);
    }

    public float getFloat(String key, float defaultValue)
    {
        return mPreferences.getFloat(key, defaultValue);
    }

    public double getDouble(String key)
    {
        return getDouble(key, 0);
    }

    private double getDouble(String key, double defaultValue)
    {
        try
        {
            return Double.valueOf(mPreferences.getString(key, String.valueOf(defaultValue)));
        }
        catch (NumberFormatException nfe)
        {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mPreferences.getBoolean(key, defaultValue);
    }

    public boolean getBoolean(String key) {
        return mPreferences.getBoolean(key, false);
    }

    public void remove(String... keys) {
        doEdit();
        for (String key : keys) {
            mEditor.remove(key);
        }
        doCommit();
    }
    public void clear() {
        doEdit();
        mEditor.clear();
        doCommit();
    }

    public void commit() {
        mBulkUpdate = false;
        mEditor.commit();
        mEditor = null;
    }

    @SuppressLint("CommitPrefEdits")
    public void edit() {
        mBulkUpdate = true;
        mEditor = mPreferences.edit();
    }

    @SuppressLint("CommitPrefEdits")
    private void doEdit() {
        if (!mBulkUpdate && mEditor == null) {
            mEditor = mPreferences.edit();
        }
    }

    private void doCommit() {
        if (!mBulkUpdate && mEditor != null) {
            mEditor.commit();
            mEditor = null;
        }
    }
}
