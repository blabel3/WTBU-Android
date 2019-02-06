package com.blabel.wtbu_android.ui.streaming;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blabel.wtbu_android.ArchiveRecyclerAdapter;
import com.blabel.wtbu_android.R;
import com.blabel.wtbu_android.Show;

import java.util.LinkedList;

public class WeekdayFragment extends Fragment {

    private WeekdayViewModel mViewModel;

    private RecyclerView mRecyclerView;
    private ArchiveRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public static WeekdayFragment newInstance() {
        return new WeekdayFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View self = inflater.inflate(R.layout.weekday_fragment, container, false);

        Bundle args = getArguments();

        LinkedList<Show> shows = new LinkedList<>();

        if(args != null) {
            for (int i = 0; i < args.size(); i++) {
                Show showParceled = args.getParcelable(""+i);
                shows.add(showParceled);
                Log.d("WTBU-A", "Showname " + showParceled.getName());
            }
        }

        Log.d("TIMING", "start");
        mRecyclerView = (RecyclerView) self.findViewById(R.id.weekday_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ArchiveRecyclerAdapter(getContext(), shows);
        mRecyclerView.setAdapter(mAdapter);
        Log.d("TIMING", "finish");

        return self;
    }

    public static WeekdayFragment newInstance(LinkedList<Show> shows){
        WeekdayFragment f = new WeekdayFragment();
        // Supply index input as an argument.
        Log.d("WTBU-A", shows.getFirst().getName());
        Bundle args = new Bundle();
        for (int i = 0; i < shows.size(); i++) {
            args.putParcelable("" + i, shows.get(i));
        }
        f.setArguments(args);
        return f;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(WeekdayViewModel.class);
        // TODO: Use the ViewModel
    }

}
