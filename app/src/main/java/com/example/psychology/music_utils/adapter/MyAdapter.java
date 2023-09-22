package com.example.psychology.music_utils.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by 52926 on 2017/4/28.
 */

public class MyAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> list;
    public MyAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list=list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
