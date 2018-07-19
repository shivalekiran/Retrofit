package com.aperotechnologies.retrofit.service.model;

/**
 * Created by kshivale on 04/06/18.
 */

@SuppressWarnings("ALL")
public class PvAModel {
    private String productName;
    private boolean isSelected;

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }
}
