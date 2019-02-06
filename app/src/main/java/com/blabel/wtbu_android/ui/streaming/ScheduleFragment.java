package com.blabel.wtbu_android.ui.streaming;

import android.os.Bundle;
import android.util.Log;
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

    private RecyclerView mRecycler;
    private ArchiveRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


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
        archiveData.add(new LinkedList<Show>());
        archiveData.add(new LinkedList<Show>());
        archiveData.add(new LinkedList<Show>());
        archiveData.add(new LinkedList<Show>());
        archiveData.add(new LinkedList<Show>());
        archiveData.add(new LinkedList<Show>());
        archiveData.add(new LinkedList<Show>());

        TabLayout tabs = (TabLayout) self.findViewById(R.id.tabs);

        Date d = new Date();
        Calendar c = new GregorianCalendar();
        TabLayout.Tab currentDay = tabs.getTabAt(c.get(Calendar.DAY_OF_WEEK) - 1);

        currentShows = archiveData.get(c.get(Calendar.DAY_OF_WEEK) - 1);

        mRecycler = (RecyclerView) self.findViewById(R.id.archive_recycler);

        mRecycler.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecycler.setLayoutManager(mLayoutManager);

        mAdapter = new ArchiveRecyclerAdapter(getContext(), currentShows);
        mRecycler.setAdapter(mAdapter);


        tabs.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        mAdapter.clear();
                        currentShows.addAll(archiveData.get(tab.getPosition()));
                        mAdapter.notifyDataSetChanged();
                    }

        //Refreshes data */

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                }
        );

        //Refreshes data
        new FetchData(this).execute();

        return self;
    }

    public void updateArchiveData(ArrayList<LinkedList<Show>> result){
        archiveData.clear();
        archiveData.addAll(result);
        mAdapter.notifyDataSetChanged();
        //set first load
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);
        // TODO: Use the ViewModel
    }

}
