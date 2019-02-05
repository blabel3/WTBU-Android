package com.blabel.wtbu_android.ui.streaming;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blabel.wtbu_android.ArchiveRecyclerAdapter;
import com.blabel.wtbu_android.FetchData;
import com.blabel.wtbu_android.R;
import com.blabel.wtbu_android.Show;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ScheduleFragment extends Fragment {

    private ScheduleViewModel mViewModel;

    private static final int NUM_PAGES = 7;

    private ViewPager viewPager;
    private WeekdayPagerAdapter pagerAdapter;

    private static ArrayList<LinkedList<Show>> archiveData;
    private LinkedList<Show> currentShows;


    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View self = inflater.inflate(R.layout.schedule_fragment, container, false);

        archiveData = new ArrayList<>(7);
        currentShows = new LinkedList<>();

        TabLayout tabs = (TabLayout) self.findViewById(R.id.tabs);

        viewPager = (ViewPager) self.findViewById(R.id.weekday_pager);
        pagerAdapter = new WeekdayPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);


        /*Date d = new Date();
        Calendar c = new GregorianCalendar();
        TabLayout.Tab currentDay = tabs.getTabAt(c.get(Calendar.DAY_OF_WEEK) - 1);
        //currentDay.select();

        //Refreshes data */

        new FetchData(this).execute();

        return self;
    }

    private class WeekdayPagerAdapter extends FragmentStatePagerAdapter {
        private SparseArray<WeekdayFragment> registeredFragments = new SparseArray<WeekdayFragment>();

        public WeekdayPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //return WeekdayFragment.newInstance(archiveData.get(position));
            return WeekdayFragment.newInstance();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            WeekdayFragment fragment = (WeekdayFragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // CHANGE STARTS HERE

            String tabTitle = "";

            switch(position){
                case 0: tabTitle = "SUN"; break;
                case 1: tabTitle = "MON"; break;
                case 2: tabTitle = "TUE"; break;
                case 3: tabTitle = "WED"; break;
                case 4: tabTitle = "THR"; break;
                case 5: tabTitle = "FRI"; break;
                case 6: tabTitle = "SAT"; break;
            }
            // CHANGE ENDS HERE
            return tabTitle;
        }
    }


    public void updateArchiveData(ArrayList<LinkedList<Show>> result){
        archiveData = result;
        //reload pages
        for(int i = 0; i < NUM_PAGES; i++){
            if(pagerAdapter.getRegisteredFragment(i)!=null){
                pagerAdapter.destroyItem(viewPager, i, pagerAdapter.getRegisteredFragment(i));
                pagerAdapter.instantiateItem(viewPager, i);
            }
        }
        //set first load
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);
        // TODO: Use the ViewModel
    }

}
