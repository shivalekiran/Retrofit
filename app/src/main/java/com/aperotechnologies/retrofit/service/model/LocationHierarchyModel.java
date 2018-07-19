package com.aperotechnologies.retrofit.service.model;

import android.arch.persistence.room.Entity;

/**
 * Created by pamrutkar on 21/05/18.
 */

@SuppressWarnings("ALL")
@Entity
public class LocationHierarchyModel extends ProductHierarchyModel
{
    private int locationId;
    private String zoneED;
    private String marketED;
    private String districtED;
    private String storeCodeParam;
    private String geoLevel3Code;
    private String geoLevel3Desc;

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getGeoLevel3Code() {
        return this.geoLevel3Code;
    }

    public void setGeoLevel3Code(String geoLevel3Code) {
        this.geoLevel3Code = geoLevel3Code;
    }

    public String getGeoLevel3Desc() {
        return this.geoLevel3Desc;
    }

    public void setGeoLevel3Desc(String geoLevel3Desc) {
        this.geoLevel3Desc = geoLevel3Desc;
    }

    public String getDescEz() {
        return this.descEz;
    }

    public void setDescEz(String descEz) {
        this.descEz = descEz;
    }

    private String descEz;

    public String getZoneED() {
        return this.zoneED;
    }

    public void setZoneED(String zoneED) {
        this.zoneED = zoneED;
    }

    public String getMarketED() {
        return this.marketED;
    }

    public void setMarketED(String marketED) {
        this.marketED = marketED;
    }

    public String getDistrictED() {
        return this.districtED;
    }

    public void setDistrictED(String districtED) {
        this.districtED = districtED;
    }

    public String getStoreCode() {
        return this.storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    private String storeCode;

    public String getStoreCodeParam() {
        return this.storeCodeParam;
    }

    public void setStoreCodeParam(String storeCodeParam) {
        this.storeCodeParam = storeCodeParam;
    }


}
