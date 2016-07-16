package com.taxiapp.app.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.taxiapp.app.fragments.BookingDetailsFragment;
import com.taxiapp.model.business.Booking;
import com.taxiapp.utils.GlobalData;
import com.taxiapp.vendor.app.R;

import java.util.List;

/**
 * Created by Amit S on 12/01/16.
 */
public class NewTripsActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager mPager;
    private List<Booking> bookings;

    private View left, right;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_newtrip);
        MyPagerAdapter adapter = new MyPagerAdapter(getFragmentManager(), GlobalData.get().getBookingList());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(adapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {





            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getSupportActionBar().setTitle("New Trips");
        left = findViewById(R.id.swipeLeftButton);
        left.setOnClickListener(this);
        right = findViewById(R.id.swipeRightButton);
        right.setOnClickListener(this);


        bookings = GlobalData.get().getBookingList();
        if (bookings.size() == 1) {
            left.setVisibility(View.GONE);
            right.setVisibility(View.GONE);
        } else {
            left.setVisibility(View.GONE);
            right.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        int position = mPager.getCurrentItem();
        switch (v.getId()) {
            case R.id.swipeLeftButton:

                mPager.setCurrentItem(position - 1);
                if (position - 1 <= 0) {
                    right.setVisibility(View.VISIBLE);
                    left.setVisibility(View.GONE);
                }

                break;
            case R.id.swipeRightButton:

                mPager.setCurrentItem(position + 1);
                if (position == bookings.size() - 2) {
                    right.setVisibility(View.GONE);
                    left.setVisibility(View.VISIBLE);
                }

                break;
        }
    }


    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        private FragmentManager fragmentManager;
        private List<Booking> dataList;

        public MyPagerAdapter(FragmentManager fragmentManager, List<Booking> dataList) {
            super(fragmentManager);
            this.fragmentManager = fragmentManager;
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            if (dataList != null) {
                return dataList.size();
            }
            return 0;
        }

        @Override
        public Fragment getItem(int position) {
            if (dataList != null && dataList.size() > 0 && position < dataList.size()) {
                Booking str = dataList.get(position);
                return BookingDetailsFragment.newInstance(str);
            }

            return null;
        }
    }
}
