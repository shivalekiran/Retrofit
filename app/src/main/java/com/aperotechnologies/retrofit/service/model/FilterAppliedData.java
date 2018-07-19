package com.aperotechnologies.retrofit.service.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kshivale on 22/05/18.
 */

@SuppressWarnings("ALL")
public class FilterAppliedData implements Serializable {
    private ArrayList<BrandVendorModel> DepartmentSelectedList, SubDeptSelectedList, ClassSelectedList
            , SubClassSelectedList, MCSelectedList, ZoneSelectedList, MarketSelectedList, DistrictSelectedList
            , StoreSelectedList, BrandTypeSelectedList, BrandSelectedList, VendorSelectedList, PvAProductList;

    public FilterAppliedData() {
        DepartmentSelectedList = new ArrayList<>();
        SubDeptSelectedList = new ArrayList<>();
        ClassSelectedList = new ArrayList<>();
        SubClassSelectedList = new ArrayList<>();
        MCSelectedList = new ArrayList<>();
        ZoneSelectedList = new ArrayList<>();
        MarketSelectedList = new ArrayList<>();
        DistrictSelectedList = new ArrayList<>();
        StoreSelectedList = new ArrayList<>();
        BrandTypeSelectedList = new ArrayList<>();
        BrandSelectedList = new ArrayList<>();
        VendorSelectedList = new ArrayList<>();
        PvAProductList = new ArrayList<>();
    }

    public ArrayList<BrandVendorModel> getPvAProductList() {
        return this.PvAProductList;
    }

    public void setPvAProductList(ArrayList<BrandVendorModel> pvAProductList) {
        this.PvAProductList = pvAProductList;
    }

    public ArrayList<BrandVendorModel> getDepartmentSelectedList() {
        return this.DepartmentSelectedList;
    }

    public void setDepartmentSelectedList(ArrayList<BrandVendorModel> departmentSelectedList) {
        this.DepartmentSelectedList = departmentSelectedList;
    }

    public ArrayList<BrandVendorModel> getSubDeptSelectedList() {
        return this.SubDeptSelectedList;
    }

    public void setSubDeptSelectedList(ArrayList<BrandVendorModel> subDeptSelectedList) {
        this.SubDeptSelectedList = subDeptSelectedList;
    }

    public ArrayList<BrandVendorModel> getClassSelectedList() {
        return this.ClassSelectedList;
    }

    public void setClassSelectedList(ArrayList<BrandVendorModel> classSelectedList) {
        this.ClassSelectedList = classSelectedList;
    }

    public ArrayList<BrandVendorModel> getSubClassSelectedList() {
        return this.SubClassSelectedList;
    }

    public void setSubClassSelectedList(ArrayList<BrandVendorModel> subClassSelectedList) {
        this.SubClassSelectedList = subClassSelectedList;
    }

    public ArrayList<BrandVendorModel> getMCSelectedList() {
        return this.MCSelectedList;
    }

    public void setMCSelectedList(ArrayList<BrandVendorModel> MCSelectedList) {
        this.MCSelectedList = MCSelectedList;
    }

    public ArrayList<BrandVendorModel> getZoneSelectedList() {
        return this.ZoneSelectedList;
    }

    public void setZoneSelectedList(ArrayList<BrandVendorModel> zoneSelectedList) {
        this.ZoneSelectedList = zoneSelectedList;
    }

    public ArrayList<BrandVendorModel> getMarketSelectedList() {
        return this.MarketSelectedList;
    }

    public void setMarketSelectedList(ArrayList<BrandVendorModel> marketSelectedList) {
        this.MarketSelectedList = marketSelectedList;
    }

    public ArrayList<BrandVendorModel> getDistrictSelectedList() {
        return this.DistrictSelectedList;
    }

    public void setDistrictSelectedList(ArrayList<BrandVendorModel> districtSelectedList) {
        this.DistrictSelectedList = districtSelectedList;
    }

    public ArrayList<BrandVendorModel> getStoreSelectedList() {
        return this.StoreSelectedList;
    }

    public void setStoreSelectedList(ArrayList<BrandVendorModel> storeSelectedList) {
        this.StoreSelectedList = storeSelectedList;
    }

    public ArrayList<BrandVendorModel> getBrandTypeSelectedList() {
        return this.BrandTypeSelectedList;
    }

    public void setBrandTypeSelectedList(ArrayList<BrandVendorModel> brandTypeSelectedList) {
        this.BrandTypeSelectedList = brandTypeSelectedList;
    }

    public ArrayList<BrandVendorModel> getBrandSelectedList() {
        return this.BrandSelectedList;
    }

    public void setBrandSelectedList(ArrayList<BrandVendorModel> brandSelectedList) {
        this.BrandSelectedList = brandSelectedList;
    }

    public ArrayList<BrandVendorModel> getVendorSelectedList() {
        return this.VendorSelectedList;
    }

    public void setVendorSelectedList(ArrayList<BrandVendorModel> vendorSelectedList) {
        this.VendorSelectedList = vendorSelectedList;
    }
}
