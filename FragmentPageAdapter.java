package com.example.tuananh.manhinhchinh;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by SidaFPT on 8/18/2016.
 */
public class FragmentPageAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[] { "CHỌN MÓN", "GỢI Ý", "THỐNG KÊ" };

    public FragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // Top Rated fragment activity
                return new Food();
            case 1:
                // Games fragment activity
                return new Recommend();
            case 2:
                // Movies fragment activity
                return new Thongke();
        }
//        return PageFragment.newInstance(position + 1);
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
