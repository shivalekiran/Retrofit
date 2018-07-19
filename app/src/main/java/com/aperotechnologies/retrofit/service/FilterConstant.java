package com.aperotechnologies.retrofit.service;

import android.util.Log;

import com.aperotechnologies.retrofit.service.db.FilterSharedPreferences;
import com.aperotechnologies.retrofit.service.model.BrandVendorModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by pamrutkar on 21/05/18.
 */

@SuppressWarnings("ALL")
public class FilterConstant {

    private static final String TAG = FilterConstant.class.getSimpleName();

    public static final String Location = "Location";
    public static final String Product = "Product";
    public static final String Brand = "Brand ";  // added space to distinguish b/w @Brand label and @BRAND list
    public static final String Vendor = "Vendor "; // added space to distinguish b/w vendor label and vendor list


    // Easy Day Filter Names
    public static final String DEPT = "Dept";
    public static final String SUBDEPT = "Subdept";
    public static final String CLASS = "Class";
    public static final String SUBCLASS = "Sub Class";
    public static final String MC = "MC";
    public static final String ZONE = "Zone";
    public static final String MARKET = "Market";
    public static final String DISTRICT = "District";
    public static final String STORE = "Store";
    public static final String BRAND_TYPE = "Brand Type";
    public static final String BRAND = "Brand";
    public static final String VENDOR = "Vendor";

    // Fashion Filter Product Hierarachy Names
    public static final String FASHION_DEPT = "Dept"; //
    public static final String FASHION_SUBDEPT = "Subdept";
    public static final String FASHION_CLASS = "Class";
    public static final String FASHION_SUBCLASS = "Sub Class";
    public static final String FASHION_MC = "MC";
    public static final String FASHION_STORE = "Store";


    //  NON Fashion Filter Product Hierarachy Names
    public static final String NON_FASHION_DEPT = "Dept"; //
    public static final String NON_FASHION_SUBDEPT = "Subdept";
    public static final String NON_FASHION_CLASS = "Class";
    public static final String NON_FASHION_MC = "MC";
    public static final String NON_FASHION_ZONE = "Zone";
    public static final String NON_FASHION_STORE = "Store";


    // Ezone Product Hierarachy Names
    public static final String EZ_DEPT = "Dept"; //
    public static final String EZ_SUBDEPT = "Subdept";
    public static final String EZ_CLASS = "Class";
    public static final String EZ_SUBCLASS = "Sub Class";
    public static final String EZ_MC = "MC";
    public static final String EZ_REGION = "Region";
    public static final String EZ_STORE = "Store";

    // Key Product pvA Filter List
    public static final String PVA_STORE = "Store";
    public static final String PVA_PRODUCT = "Product";

    // this used for only store filter
    public static final String ALL_STORE = "Store";


    public static final String FILTER_ARRAY = "array_list";
    public static final String CHECK_FROM = "checkFrom";

    public static final String ADAPTER_LIST = "ADAPTER_LIST";
    public static final String SELECTED_LIST = "SELECTED_LIST";
    public static final String FILTER_SELECTED = "FILTER_SELECTED_VALUES";
    public static final String HAS_LAZY_LOADING = "HAS_LAZY_LOADING";
    public static final String FILTER_DATA = "FILTER_DATA";
    public static final String FILTER_APPLIED = "FILTER_APPLIED";
    public static final String HEADER_VALUES = "HEADER_VALUES";
    public static final String FILTER_CLEARED = "FILTER_CLEARED";
    public static final String FILTER_LEVEL = "FILTER_LEVEL";
    public static final String FILTERED_APPLIED = "FILTERED_APPLIED";
    public static final String FILTER_HEADER_DATA_CHANGE = "FILTER_HEADER_DATA_CHANGE";
    public static final String Filter_View = "view";
    // level int constants

    public static final int NON_DEPT_LEVEL = 1;
    public static final int NON_SUBDEPT_LEVEL = 2;
    public static final int NON_CLASS_LEVEL = 3;
    public static final int NON_MC_LEVEL = 4;

    public static final int DEPT_LEVEL = 1;
    public static final int SUBDEPT_LEVEL = 2;
    public static final int CLASS_LEVEL = 3;

    public static final int SUBCLASS_LEVEL = 4;
    public static final int MC_LEVEL = 5;
    public static final int ZONE_LEVEL = 1;
    public static final int MARKET_LEVEL = 1;
    public static final int DISTRICT_LEVEL = 1;
    public static final int STORE_LEVEL = 1;


    public static boolean FILTER_VALUES_CHANGE = false;
    public static final int REQUEST_TYPE_PRODUCT = 1;
    public static final String[] mProductHierarchyList = {
            Product, DEPT, SUBDEPT, CLASS, SUBCLASS, MC,
            Location, ZONE, MARKET, DISTRICT, STORE,
            Brand, BRAND_TYPE, BRAND,
            Vendor, VENDOR
    };

    public static final String[] mLocationHierarchyList = {
            ZONE, MARKET, DISTRICT, STORE
    };

    public static final String[] mFashionFilterList = {
            DEPT, SUBDEPT, CLASS, SUBCLASS, MC, STORE
    };

    public static final String[] mEzoneSalesFilterList = {
            EZ_REGION, EZ_STORE, EZ_DEPT, EZ_SUBDEPT, EZ_CLASS, EZ_SUBCLASS
    };
    public static final String[] mEzoneInventoryFilterList = {
            EZ_REGION, EZ_STORE, EZ_DEPT, EZ_SUBDEPT, EZ_CLASS, EZ_SUBCLASS, EZ_MC
    };
    public static final String[] mNonFashionFilterList = {
            NON_FASHION_DEPT, NON_FASHION_SUBDEPT, NON_FASHION_CLASS, NON_FASHION_MC, NON_FASHION_ZONE, NON_FASHION_STORE
    };

    public static final String[] mKeyProdPvAFilterList = {
            PVA_STORE, PVA_PRODUCT
    };
    public static final String[] mAllStore = {
            ALL_STORE
    };

    public static String checkStoreAttribute(String lobId, String geoLevel2Code) {
        try {
            if ((geoLevel2Code.contains("BB") || geoLevel2Code.contains("FBB"))
                    && (lobId.contains("3") || lobId.contains("7") || lobId.contains("1"))) {
                return "&storeAttribute1=BB,HC";
            } else if (((geoLevel2Code.contains("BB") || geoLevel2Code.contains("FBB")))){
                return "&storeAttribute1=All";
            }
        } catch (Exception e) {
            Log.e(TAG, "checkStoreAttribute: ", e);
        }
        return "";
    }

    public static ArrayList<BrandVendorModel> parseResponse(String response, Gson gson, int level) {
        ArrayList<BrandVendorModel> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(response);
            if (array != null || array.length() > 0) {
                JSONArray levelArray = array.getJSONArray(level - 1);
                if (levelArray != null && levelArray.length() > 0) {
                    for (int i = 0; i < levelArray.length(); i++) {
                        BrandVendorModel model = gson.fromJson(levelArray.get(i).toString(), BrandVendorModel.class);
                        list.add(model);
                    }
                }
            }
            Log.e(TAG, "parseResponse: Size: " + list.size());
        } catch (JSONException e) {
            Log.e(TAG, "parseResponse: ", e);
        }
        return list;
    }

    public static ArrayList<BrandVendorModel> parseResponse(String response, Gson gson) {
        ArrayList<BrandVendorModel> list = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(response);
            if (array != null || array.length() > 0) {
                for (int i = 0; i < array.length(); i++) {
                    BrandVendorModel model = gson.fromJson(array.get(i).toString(), BrandVendorModel.class);
                    list.add(model);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "parseResponse: ", e);
        }
        return list;
    }

    // return index of model if o
//    public static int addSelectedModel(BrandVendorModel model, ArrayList<BrandVendorModel> list, String level) {
//        int index = -1;
//        switch (level) {
//            case FilterSharedPreferences.Key.ED_DEPT:
//            case FilterConstant.DEPT:
//                for (BrandVendorModel m : list) {
//                    if (m.getProdLevel2Code().equals(model.getProdLevel2Code()) || m.getProdLevel2Desc().equals(model.getProdLevel2Desc())) {
//                        m.setSelected(true);
//                        index = list.indexOf(m);
//                        break;
//                    }
//                }
//                break;
//            case FilterSharedPreferences.Key.ED_SUB_DEPT:
//            case FilterConstant.SUBDEPT:
//                for (BrandVendorModel m : list) {
//                    if (m.getProdLevel3Code().equals(model.getProdLevel3Code()) || m.getProdLevel3Desc().equals(model.getProdLevel3Desc())) {
//                        m.setSelected(true);
//                        index = list.indexOf(m);
//                        break;
//                    }
//                }
//                break;
//            case FilterSharedPreferences.Key.ED_CLASS:
//            case FilterConstant.CLASS:
//                for (BrandVendorModel m : list) {
//                    if (m.getProdLevel4Code().equals(model.getProdLevel4Code()) || m.getProdLevel4Desc().equals(model.getProdLevel4Desc())) {
//                        m.setSelected(true);
//                        index = list.indexOf(m);
//                        break;
//                    }
//                }
//                break;
//            case FilterSharedPreferences.Key.ED_SUB_CLASS:
//            case FilterConstant.SUBCLASS:
//                for (BrandVendorModel m : list) {
//                    if (m.getProdLevel5Code().equals(model.getProdLevel5Code()) || m.getProdLevel5Desc().equals(model.getProdLevel5Desc())) {
//                        m.setSelected(true);
//                        index = list.indexOf(m);
//                        break;
//                    }
//                }
//                break;
//            case FilterSharedPreferences.Key.ED_MC:
//            case FilterConstant.MC:
//                for (BrandVendorModel m : list) {
//                    if (m.getProdLevel6Code().equals(model.getProdLevel6Code()) || m.getProdLevel6Desc().equals(model.getProdLevel6Desc())) {
//                        m.setSelected(true);
//                        index = list.indexOf(m);
//                        break;
//                    }
//                }
//                break;
//
//            case FilterSharedPreferences.Key.ZONE:
//            case FilterConstant.ZONE:
//                for (BrandVendorModel m : list) {
//                    if (m.getZoneED().equals(model.getZoneED())) {
//                        m.setSelected(true);
//                        index = list.indexOf(m);
//                        break;
//                    }
//                }
//                break;
//            case FilterSharedPreferences.Key.ED_MARKER:
//            case FilterConstant.MARKET:
//                for (BrandVendorModel m : list) {
//                    if (m.getMarketED().equals(model.getMarketED())) {
//                        m.setSelected(true);
//                        index = list.indexOf(m);
//                        break;
//                    }
//                }
//                break;
//            case FilterSharedPreferences.Key.ED_DISTRICT:
//            case FilterConstant.DISTRICT:
//                for (BrandVendorModel m : list) {
//                    if (m.getDistrictED().equals(model.getDistrictED())) {
//                        m.setSelected(true);
//                        index = list.indexOf(m);
//                        break;
//                    }
//                }
//                break;
//            case FilterSharedPreferences.Key.STORE:
//            case FilterConstant.STORE:
//                for (BrandVendorModel m : list) {
//                    if (m.getStoreCode().equals(model.getStoreCode()) || m.getStoreCodeParam().equals(model.getStoreCodeParam())) {
//                        m.setSelected(true);
//                        index = list.indexOf(m);
//                        break;
//                    }
//                }
//                break;
//            case FilterSharedPreferences.Key.ED_BRAND_TYPE:
//            case FilterConstant.BRAND_TYPE:
//                for (BrandVendorModel m : list) {
//                    if (m.getBrandLevel1Code().equals(model.getBrandLevel1Code())) {
//                        m.setSelected(true);
//                        index = list.indexOf(m);
//                        break;
//                    }
//                }
//                break;
//            case FilterSharedPreferences.Key.ED_BRAND:
//            case FilterConstant.BRAND:
//                for (BrandVendorModel m : list) {
//                    if (m.getBrandLevel3Code().equals(model.getBrandLevel3Code()) || m.getBrandLevel3Desc().equals(model.getBrandLevel3Desc())) {
//                        m.setSelected(true);
//                        index = list.indexOf(m);
//                        break;
//                    }
//                }
//                break;
//            case FilterSharedPreferences.Key.ED_VENDOR:
//            case FilterConstant.VENDOR:
//                for (BrandVendorModel m : list) {
//                    if (m.getVendorCode().equals(model.getVendorCode()) || m.getVendorDesc().equals(model.getVendorDesc())) {
//                        m.setSelected(true);
//                        index = list.indexOf(m);
//                        break;
//                    }
//                }
//                break;
//        }
//        return index;
//    }

    public static int addFashionSelectedModel(BrandVendorModel model, ArrayList<BrandVendorModel> list, String level) {
        int index = -1;
        switch (level) {
            case FilterSharedPreferences.Key.DEPT:
            case FilterConstant.DEPT:
                for (BrandVendorModel m : list) {
                    if (m.getPlanDept().equals(model.getPlanDept())) {
                        m.setSelected(true);
                        index = list.indexOf(m);
                        break;
                    }
                }
                break;
            case FilterSharedPreferences.Key.SUB_DEPT:
            case FilterConstant.SUBDEPT:
                for (BrandVendorModel m : list) {
                    if (m.getPlanCategory().equals(model.getPlanCategory())) {
                        m.setSelected(true);
                        index = list.indexOf(m);
                        break;
                    }
                }
                break;
            case FilterSharedPreferences.Key.CLASS:
            case FilterConstant.CLASS:
                for (BrandVendorModel m : list) {
                    if (m.getPlanClass().equals(model.getPlanClass())) {
                        m.setSelected(true);
                        index = list.indexOf(m);
                        break;
                    }
                }
                break;
            case FilterSharedPreferences.Key.SUB_CLASS:
            case FilterConstant.SUBCLASS:
                for (BrandVendorModel m : list) {
                    if (m.getBrandName().equals(model.getBrandName())) {
                        m.setSelected(true);
                        index = list.indexOf(m);
                        break;
                    }
                }
                break;
            case FilterSharedPreferences.Key.MC:
            case FilterConstant.MC:
                for (BrandVendorModel m : list) {
                    if (m.getBrandPlanClass().equals(model.getBrandPlanClass())) {
                        m.setSelected(true);
                        index = list.indexOf(m);
                        break;
                    }
                }
                break;

        }
        return index;
    }

}
