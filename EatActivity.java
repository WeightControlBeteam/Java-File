package com.example.tuananh.manhinhchinh;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;

/**
 * Created by SidaFPT on 8/18/2016.
 */
public class EatActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private FragmentPageAdapter fragAdapter;
    static Context mainContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat);
        mainContext = getApplication().getApplicationContext();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        fragAdapter = new FragmentPageAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewpager1);
        viewPager.setAdapter(fragAdapter);

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
//        tabsStrip.setDividerColor(getResources().getColor(R.color.white));
//        tabsStrip.setBackgroundColor(getResources().getColor(R.color.green));
 //       tabsStrip.setTextColor(getResources().getColor(R.color.white));
//        tabsStrip.setIndicatorColor(getResources().getColor(R.color.white));
//        tabsStrip.setTextSize(40);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
    }

}
