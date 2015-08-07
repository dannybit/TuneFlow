package com.dannybit.tuneflow.fragments.search;

import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.dannybit.tuneflow.R;
import com.dannybit.tuneflow.activities.SearchSongActivity;
import com.dannybit.tuneflow.fragments.NowPlayingFragment;


public class SearchLocalFragment extends Fragment {

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private Activity activity;

    public SearchLocalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_local, container, false);
        pager = (ViewPager) view.findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        pager.setAdapter(pagerAdapter);
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        return view;
    }



    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{

        private final String[] TITLES = { "Songs", "Artists", "Albums"};

        public ScreenSlidePagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SearchLocalSongsListFragment();
                case 1:
                    return new SearchLocalArtistsListFragment();
                case 2:
                    return new SearchLocalSongsListFragment();
                default:
                    return null;


            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }



}
