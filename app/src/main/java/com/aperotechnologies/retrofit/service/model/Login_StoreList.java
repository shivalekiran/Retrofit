package com.aperotechnologies.retrofit.service.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by pamrutkar on 15/09/17.
 */

@Entity
public class Login_StoreList
{
    @PrimaryKey(autoGenerate = true)
    private int ID;

    String userId;
    String loginName;
    String password;
    String fullName;
    String bearerToken;
    String geoLeveLDesc;
    String geoLevel2Code;
    String storeCode;
    String geoLevel2Desc;
    String isMultiStore;
    String isMultiConcept;
    String isConceptSwitch;
    String kpiId;
    String roleId;
    String roleName;
    String lobId;
    String lobName;
    String hierarchyLevels;



/*    public Login_StoreList(String geoLevel2Code ,String kpiId, String lobId, String lobName ) {
        this.geoLevel2Code = geoLevel2Code;
        this.kpiId = kpiId;
        this.lobId = lobId;
        this.lobName = lobName;
    }*/

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Login_StoreList() {
        // empty constructor for set params.
    }
    public String getHierarchyLevels() {
        return hierarchyLevels;
    }
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGeoLeveLDesc() {
        return geoLeveLDesc;
    }

    public void setGeoLeveLDesc(String geoLeveLDesc) {
        this.geoLeveLDesc = geoLeveLDesc;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    public String getGeoLevel2Code() {
        return geoLevel2Code;
    }

    public void setGeoLevel2Code(String geoLevel2Code) {
        this.geoLevel2Code = geoLevel2Code;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getGeoLevel2Desc() {
        return geoLevel2Desc;
    }

    public void setGeoLevel2Desc(String geoLevel2Desc) {
        this.geoLevel2Desc = geoLevel2Desc;
    }

    public String getIsMultiStore() {
        return isMultiStore;
    }

    public void setIsMultiStore(String isMultiStore) {
        this.isMultiStore = isMultiStore;
    }

    public String getIsMultiConcept() {
        return isMultiConcept;
    }

    public void setIsMultiConcept(String isMultiConcept) {
        this.isMultiConcept = isMultiConcept;
    }

    public String getIsConceptSwitch() {
        return isConceptSwitch;
    }

    public void setIsConceptSwitch(String isConceptSwitch) {
        this.isConceptSwitch = isConceptSwitch;
    }

    public String getKpiId() {
        return kpiId;
    }

    public void setKpiId(String kpiId) {
        this.kpiId = kpiId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getLobName() {
        return lobName;
    }

    public void setLobName(String lobName) {
        this.lobName = lobName;
    }

    public String getLobId() {
        return lobId;
    }

    public void setHierarchyLevels(String hierarchyLevels) {
        this.hierarchyLevels = hierarchyLevels;
    }
    public void setLobId(String lobId) {
        this.lobId = lobId;
    }


}
