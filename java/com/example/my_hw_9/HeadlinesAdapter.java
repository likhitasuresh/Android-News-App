package com.example.my_hw_9;

import android.content.Context;

import com.example.my_hw_9.R;
import com.example.my_hw_9.ui.headlines.BusinessFragment;
import com.example.my_hw_9.ui.headlines.PoliticsFragment;
import com.example.my_hw_9.ui.headlines.ScienceFragment;
import com.example.my_hw_9.ui.headlines.SportsFragment;
import com.example.my_hw_9.ui.headlines.TechnologyFragment;
import com.example.my_hw_9.ui.headlines.WorldFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HeadlinesAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public HeadlinesAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new WorldFragment();
        } else if (position == 1){
            return new BusinessFragment();
        } else if (position == 2){
            return new PoliticsFragment();
        } else if (position == 3){
            return new SportsFragment();
        }
        else if (position == 4){
            return new TechnologyFragment();
        }
        else {
            return new ScienceFragment();
        }

    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 6;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "World";
            case 1:
                return "Business";
            case 2:
                return "Politics";
            case 3:
                return "Sports";
            case 4:
                return "Technology";
            case 5:
                return "Science";
            default:
                return null;
        }
    }

}
