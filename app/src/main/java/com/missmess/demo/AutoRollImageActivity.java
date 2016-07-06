package com.missmess.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class AutoRollImageActivity extends AppCompatActivity {

    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_roll_image);

        init();
        initData();
    }

    private void init() {
        vp = (ViewPager) findViewById(R.id.vp);
    }

    private void initData() {
        vp.setAdapter(new MyAdapter(getSupportFragmentManager()));
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new AutoRollImageFragment();
            } else {
                return new BlankFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
