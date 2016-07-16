package com.taxiapp.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taxiapp.vendor.app.R;

/**
 * Created by Amit S on 16/11/15.
 */
public class VendorHomeFragment extends Fragment {

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home_vendor, null);
        return rootView;
    }
}
