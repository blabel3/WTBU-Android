package com.blabel.wtbu_android.ui.streaming;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blabel.wtbu_android.ArchiveRecyclerAdapter;
import com.blabel.wtbu_android.HomeActivity;
import com.blabel.wtbu_android.R;
import com.blabel.wtbu_android.Show;
import com.google.android.material.tabs.TabLayout;

import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ArchiveFragment extends Fragment {

    private ArchiveViewModel model;

    private RecyclerView mRecycler;
    private ArchiveRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private LinkedList<Show> currentShows;

    private TabLayout tabs;


    public static ArchiveFragment newInstance() {
        return new ArchiveFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View self = inflater.inflate(R.layout.schedule_fragment, container, false);

        tabs = (TabLayout) self.findViewById(R.id.tabs);

        mRecycler = self.findViewById(R.id.archive_recycler);
        mRecycler.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecycler.setLayoutManager(mLayoutManager);

        currentShows = new LinkedList<>();
        mAdapter = new ArchiveRecyclerAdapter((HomeActivity)getActivity(), currentShows);
        mRecycler.setAdapter(mAdapter);

        tabs.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        model.getShows().changeDay(tab.getPosition());
                    }

        //Refreshes data */

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        //model.getShows().loadShows();
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                }
        );

        return self;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = ViewModelProviders.of(this).get(ArchiveViewModel.class);
        // TODO: Use the ViewModel

        model.getShows().observe(this, new Observer<LinkedList<Show>>() {
            @Override
            public void onChanged(LinkedList<Show> shows) {
                //update UI with new shows.
                tabs.getTabAt(model.getShows().dataDay()).select();
                mAdapter.clear();
                currentShows.clear();
                currentShows.addAll(shows);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

}
