package com.example.rshum.instaclone.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rshum on 13/09/2017.
 */

public class SectionsStatePagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final HashMap<Fragment , Integer> mFragments = new HashMap<>();
    private final HashMap<String , Integer> mFragmentNumbers = new HashMap<>();
    private final HashMap<Integer , String > mFragmentNames = new HashMap<>();

    public SectionsStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String fragmentName){
        mFragmentList.add(fragment);
        mFragments.put(fragment , mFragmentList.size() - 1);
        mFragmentNumbers.put(fragmentName , mFragmentList.size() - 1);
        mFragmentNames.put(mFragmentList.size() - 1 , fragmentName);
    }

    //returns the fragment with the name
    public Integer getFragmentNumber(String fragmentName){
        if (mFragmentNumbers.containsKey(fragmentName)){
            return mFragmentNumbers.get(fragmentName);
        }
        else
            return null;
    }

    //returns the fragment with the name
    public Integer getFragmentNumber(Fragment fragment){
        if (mFragmentNumbers.containsKey(fragment)){
            return mFragmentNumbers.get(fragment);
        }
        else
            return null;
    }

    //returns the fragment with the name
    public Integer getFragmentName(Integer fragmentNumber){
        if (mFragmentNumbers.containsKey(fragmentNumber)){
            return mFragmentNumbers.get(fragmentNumber);
        }
        else
            return null;
    }


}
