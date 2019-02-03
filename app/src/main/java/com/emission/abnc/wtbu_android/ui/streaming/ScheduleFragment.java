package com.emission.abnc.wtbu_android.ui.streaming;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.emission.abnc.wtbu_android.ArchiveRecyclerAdapter;
import com.emission.abnc.wtbu_android.FetchData;
import com.emission.abnc.wtbu_android.R;
import com.google.android.material.tabs.TabLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ScheduleFragment extends Fragment {

    private ScheduleViewModel mViewModel;

    private RecyclerView mRecycler;
    public ArchiveRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static LinkedList<LinkedList<String>> dayOfTheWeekData;


    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View self = inflater.inflate(R.layout.schedule_fragment, container, false);

        dayOfTheWeekData = new LinkedList<>();

        TabLayout tabs = (TabLayout) self.findViewById(R.id.tabs);
        mRecycler = (RecyclerView) self.findViewById(R.id.archive_recycler);

        mRecycler.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecycler.setLayoutManager(mLayoutManager);

        mAdapter = new ArchiveRecyclerAdapter(dayOfTheWeekData);
        mRecycler.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

        Log.d("WTBU-A", "Got here");

        tabs.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        mAdapter.clear();
                        dayOfTheWeekData.add(dayOfTheWeekData.get(tab.getPosition()));
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                }
        );

        //Refreshes data
        new FetchData().execute();

        return self;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);
        // TODO: Use the ViewModel
    }

}
