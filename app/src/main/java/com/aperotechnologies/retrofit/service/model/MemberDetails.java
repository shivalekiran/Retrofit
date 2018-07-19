package com.aperotechnologies.retrofit.service.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by csuthar on 12/12/17.
 */

@Entity
public class MemberDetails implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int Id;

    private double totMbr;
    private double trxMbrPct;
    private double mbrSaleNetVal;
    private double mbrSalePct;
    private double totBills;
    private double mbrBills;
    private double mbrBillPct;
    private double mbrUnitsPct;
    private double mbrSaleTotQty;
    private double avgBasketVal;
    private double avgBasketValMbr;
    private double avgBasketSize;
    private double avgBasketSizeMbr;
    private double yoyNetSalesGrowthPct;
    private double yoyMarginGrowthPct;
    private double marginPct;
    private double invVal;
    private double instockPercent;
    private double daysSupply;
    private double billsPerMbr;
    private double salePerMbr;
    private double unitsPerMbr;
    private int trxMbrCount;
    private int newMbrCount;
    private int expiringMbrCount;
    private int renewedMbrCount;
    private int expiredMbrCount;
    private String dayType;
    private int planNewMbrCount;
    private int maxMbrCnt;
    private double mbrAchPct;
    // customer details response
    private String customerCode;
    private String fullName;
    private String mobileNumber;
    private String lastVisitDate;
    private String mbrVisitCount;
    private double avgBasketQty;
    private String last90dayVisit;
    private String emailAddress;
    private String brandLevel1Code;
    private String vendorDesc;
    //for ed fileter add check box file here
    private int childPosition;

    // Api response for basket size screen
    private String level;
    private int custCnt;
    private double custContri;
    String storeCode;
    String storeCodeParam;
    String geoLevel2Code;
    String geoLevel2Desc;
    String vendorCode;
    // Api response for product hierarchy (easy day sales filter)
    String prodLevel2Code;
    String prodLevel2Desc;
    String prodLevel3Code;
    String prodLevel5Code;
    String prodLevel5Desc;
    String prodLevel6Code;
    String prodLevel6Desc;
    String brandLevel3Code;
    String brandLevel3Desc;
    private String levelCode;
    String prodLevel3Desc;

    String prodLevel4Code;
    String prodLevel4Desc;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public double getTotMbr() {
        return totMbr;
    }

    public void setTotMbr(double totMbr) {
        this.totMbr = totMbr;
    }

    public double getTrxMbrPct() {
        return trxMbrPct;
    }

    public void setTrxMbrPct(double trxMbrPct) {
        this.trxMbrPct = trxMbrPct;
    }

    public double getMbrSaleNetVal() {
        return mbrSaleNetVal;
    }

    public void setMbrSaleNetVal(double mbrSaleNetVal) {
        this.mbrSaleNetVal = mbrSaleNetVal;
    }

    public double getMbrSalePct() {
        return mbrSalePct;
    }

    public void setMbrSalePct(double mbrSalePct) {
        this.mbrSalePct = mbrSalePct;
    }

    public double getTotBills() {
        return totBills;
    }

    public void setTotBills(double totBills) {
        this.totBills = totBills;
    }

    public double getMbrBills() {
        return mbrBills;
    }

    public void setMbrBills(double mbrBills) {
        this.mbrBills = mbrBills;
    }

    public double getMbrBillPct() {
        return mbrBillPct;
    }

    public void setMbrBillPct(double mbrBillPct) {
        this.mbrBillPct = mbrBillPct;
    }

    public double getMbrUnitsPct() {
        return mbrUnitsPct;
    }

    public void setMbrUnitsPct(double mbrUnitsPct) {
        this.mbrUnitsPct = mbrUnitsPct;
    }

    public double getMbrSaleTotQty() {
        return mbrSaleTotQty;
    }

    public void setMbrSaleTotQty(double mbrSaleTotQty) {
        this.mbrSaleTotQty = mbrSaleTotQty;
    }

    public double getAvgBasketVal() {
        return avgBasketVal;
    }

    public void setAvgBasketVal(double avgBasketVal) {
        this.avgBasketVal = avgBasketVal;
    }

    public double getAvgBasketValMbr() {
        return avgBasketValMbr;
    }

    public void setAvgBasketValMbr(double avgBasketValMbr) {
        this.avgBasketValMbr = avgBasketValMbr;
    }

    public double getAvgBasketSize() {
        return avgBasketSize;
    }

    public void setAvgBasketSize(double avgBasketSize) {
        this.avgBasketSize = avgBasketSize;
    }

    public double getAvgBasketSizeMbr() {
        return avgBasketSizeMbr;
    }

    public void setAvgBasketSizeMbr(double avgBasketSizeMbr) {
        this.avgBasketSizeMbr = avgBasketSizeMbr;
    }

    public double getYoyNetSalesGrowthPct() {
        return yoyNetSalesGrowthPct;
    }

    public void setYoyNetSalesGrowthPct(double yoyNetSalesGrowthPct) {
        this.yoyNetSalesGrowthPct = yoyNetSalesGrowthPct;
    }

    public double getYoyMarginGrowthPct() {
        return yoyMarginGrowthPct;
    }

    public void setYoyMarginGrowthPct(double yoyMarginGrowthPct) {
        this.yoyMarginGrowthPct = yoyMarginGrowthPct;
    }

    public double getMarginPct() {
        return marginPct;
    }

    public void setMarginPct(double marginPct) {
        this.marginPct = marginPct;
    }

    public double getInvVal() {
        return invVal;
    }

    public void setInvVal(double invVal) {
        this.invVal = invVal;
    }

    public double getInstockPercent() {
        return instockPercent;
    }

    public void setInstockPercent(double instockPercent) {
        this.instockPercent = instockPercent;
    }

    public double getDaysSupply() {
        return daysSupply;
    }

    public void setDaysSupply(double daysSupply) {
        this.daysSupply = daysSupply;
    }

    public double getBillsPerMbr() {
        return billsPerMbr;
    }

    public void setBillsPerMbr(double billsPerMbr) {
        this.billsPerMbr = billsPerMbr;
    }

    public double getSalePerMbr() {
        return salePerMbr;
    }

    public void setSalePerMbr(double salePerMbr) {
        this.salePerMbr = salePerMbr;
    }

    public double getUnitsPerMbr() {
        return unitsPerMbr;
    }

    public void setUnitsPerMbr(double unitsPerMbr) {
        this.unitsPerMbr = unitsPerMbr;
    }

    public int getTrxMbrCount() {
        return trxMbrCount;
    }

    public void setTrxMbrCount(int trxMbrCount) {
        this.trxMbrCount = trxMbrCount;
    }

    public int getNewMbrCount() {
        return newMbrCount;
    }

    public void setNewMbrCount(int newMbrCount) {
        this.newMbrCount = newMbrCount;
    }

    public int getExpiringMbrCount() {
        return expiringMbrCount;
    }

    public void setExpiringMbrCount(int expiringMbrCount) {
        this.expiringMbrCount = expiringMbrCount;
    }

    public int getRenewedMbrCount() {
        return renewedMbrCount;
    }

    public void setRenewedMbrCount(int renewedMbrCount) {
        this.renewedMbrCount = renewedMbrCount;
    }

    public int getExpiredMbrCount() {
        return expiredMbrCount;
    }

    public void setExpiredMbrCount(int expiredMbrCount) {
        this.expiredMbrCount = expiredMbrCount;
    }

    public String getDayType() {
        return dayType;
    }

    public void setDayType(String dayType) {
        this.dayType = dayType;
    }

    public int getPlanNewMbrCount() {
        return planNewMbrCount;
    }

    public void setPlanNewMbrCount(int planNewMbrCount) {
        this.planNewMbrCount = planNewMbrCount;
    }

    public int getMaxMbrCnt() {
        return maxMbrCnt;
    }

    public void setMaxMbrCnt(int maxMbrCnt) {
        this.maxMbrCnt = maxMbrCnt;
    }

    public double getMbrAchPct() {
        return mbrAchPct;
    }

    public void setMbrAchPct(double mbrAchPct) {
        this.mbrAchPct = mbrAchPct;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getLastVisitDate() {
        return lastVisitDate;
    }

    public void setLastVisitDate(String lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

    public String getMbrVisitCount() {
        return mbrVisitCount;
    }

    public void setMbrVisitCount(String mbrVisitCount) {
        this.mbrVisitCount = mbrVisitCount;
    }

    public double getAvgBasketQty() {
        return avgBasketQty;
    }

    public void setAvgBasketQty(double avgBasketQty) {
        this.avgBasketQty = avgBasketQty;
    }

    public String getLast90dayVisit() {
        return last90dayVisit;
    }

    public void setLast90dayVisit(String last90dayVisit) {
        this.last90dayVisit = last90dayVisit;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getBrandLevel1Code() {
        return brandLevel1Code;
    }

    public void setBrandLevel1Code(String brandLevel1Code) {
        this.brandLevel1Code = brandLevel1Code;
    }

    public String getVendorDesc() {
        return vendorDesc;
    }

    public void setVendorDesc(String vendorDesc) {
        this.vendorDesc = vendorDesc;
    }

    public int getChildPosition() {
        return childPosition;
    }

    public void setChildPosition(int childPosition) {
        this.childPosition = childPosition;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getCustCnt() {
        return custCnt;
    }

    public void setCustCnt(int custCnt) {
        this.custCnt = custCnt;
    }

    public double getCustContri() {
        return custContri;
    }

    public void setCustContri(double custContri) {
        this.custContri = custContri;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreCodeParam() {
        return storeCodeParam;
    }

    public void setStoreCodeParam(String storeCodeParam) {
        this.storeCodeParam = storeCodeParam;
    }

    public String getGeoLevel2Code() {
        return geoLevel2Code;
    }

    public void setGeoLevel2Code(String geoLevel2Code) {
        this.geoLevel2Code = geoLevel2Code;
    }

    public String getGeoLevel2Desc() {
        return geoLevel2Desc;
    }

    public void setGeoLevel2Desc(String geoLevel2Desc) {
        this.geoLevel2Desc = geoLevel2Desc;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getProdLevel2Code() {
        return prodLevel2Code;
    }

    public void setProdLevel2Code(String prodLevel2Code) {
        this.prodLevel2Code = prodLevel2Code;
    }

    public String getProdLevel2Desc() {
        return prodLevel2Desc;
    }

    public void setProdLevel2Desc(String prodLevel2Desc) {
        this.prodLevel2Desc = prodLevel2Desc;
    }

    public String getProdLevel3Code() {
        return prodLevel3Code;
    }

    public void setProdLevel3Code(String prodLevel3Code) {
        this.prodLevel3Code = prodLevel3Code;
    }

    public String getProdLevel5Code() {
        return prodLevel5Code;
    }

    public void setProdLevel5Code(String prodLevel5Code) {
        this.prodLevel5Code = prodLevel5Code;
    }

    public String getProdLevel5Desc() {
        return prodLevel5Desc;
    }

    public void setProdLevel5Desc(String prodLevel5Desc) {
        this.prodLevel5Desc = prodLevel5Desc;
    }

    public String getProdLevel6Code() {
        return prodLevel6Code;
    }

    public void setProdLevel6Code(String prodLevel6Code) {
        this.prodLevel6Code = prodLevel6Code;
    }

    public String getProdLevel6Desc() {
        return prodLevel6Desc;
    }

    public void setProdLevel6Desc(String prodLevel6Desc) {
        this.prodLevel6Desc = prodLevel6Desc;
    }

    public String getBrandLevel3Code() {
        return brandLevel3Code;
    }

    public void setBrandLevel3Code(String brandLevel3Code) {
        this.brandLevel3Code = brandLevel3Code;
    }

    public String getBrandLevel3Desc() {
        return brandLevel3Desc;
    }

    public void setBrandLevel3Desc(String brandLevel3Desc) {
        this.brandLevel3Desc = brandLevel3Desc;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getProdLevel3Desc() {
        return prodLevel3Desc;
    }

    public void setProdLevel3Desc(String prodLevel3Desc) {
        this.prodLevel3Desc = prodLevel3Desc;
    }

    public String getProdLevel4Code() {
        return prodLevel4Code;
    }

    public void setProdLevel4Code(String prodLevel4Code) {
        this.prodLevel4Code = prodLevel4Code;
    }

    public String getProdLevel4Desc() {
        return prodLevel4Desc;
    }

    public void setProdLevel4Desc(String prodLevel4Desc) {
        this.prodLevel4Desc = prodLevel4Desc;
    }
}
