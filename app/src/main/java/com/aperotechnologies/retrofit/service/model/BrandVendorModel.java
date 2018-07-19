package com.aperotechnologies.retrofit.service.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by pamrutkar on 21/05/18.
 */

@SuppressWarnings("ALL")
@Entity
public class BrandVendorModel extends LocationHierarchyModel {
    @PrimaryKey(autoGenerate = true)
    private int brandId;
    private String brandLevel3Code;
    private String brandLevel3Desc;
    private String brandLevel1Code;
    private String vendorCode;
    private String vendorDesc;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandLevel3Code() {
        return this.brandLevel3Code;
    }

    public void setBrandLevel3Code(String brandLevel3Code) {
        this.brandLevel3Code = brandLevel3Code;
    }

    public String getBrandLevel3Desc() {
        return this.brandLevel3Desc;
    }

    public void setBrandLevel3Desc(String brandLevel3Desc) {
        this.brandLevel3Desc = brandLevel3Desc;
    }

    public String getBrandLevel1Code() {
        return this.brandLevel1Code;
    }

    public void setBrandLevel1Code(String brandLevel1Code) {
        this.brandLevel1Code = brandLevel1Code;
    }

    public String getVendorCode() {
        return this.vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getVendorDesc() {
        return this.vendorDesc;
    }

    public void setVendorDesc(String vendorDesc) {
        this.vendorDesc = vendorDesc;
    }

}


