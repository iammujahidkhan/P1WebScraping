package com.justclack.legends_quotes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by Mujahid on 4/15/2018.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    public SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PopularTopicsFragment textFragment = new PopularTopicsFragment();
                return textFragment;
            case 1:
                PopularAuthorsFragment categoriesFragment = new PopularAuthorsFragment();
                return categoriesFragment;
            case 2:
                FavFragment favFragment = new FavFragment();
                return favFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            case 0:
                return "Popular Topics";
            case 1:
                return "Popular Authors";
            case 2:
                return "My Favourites";
            default:
                return null;
        }
    }
}