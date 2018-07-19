package com.aperotechnologies.retrofit.service;


import com.aperotechnologies.retrofit.service.model.BrandVendorModel;

import java.util.ArrayList;

/**
 * Created by kshivale on 23/05/18.
 */

@SuppressWarnings("ALL")
public interface FilterFragmentInteraction {
    void onItemClick(String level, ArrayList<BrandVendorModel> selectedValues);
}
