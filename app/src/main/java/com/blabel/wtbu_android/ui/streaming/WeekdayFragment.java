package com.blabel.wtbu_android.ui.streaming;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blabel.wtbu_android.R;
import com.blabel.wtbu_android.Show;

import java.util.LinkedList;

public class WeekdayFragment extends Fragment {

    private WeekdayViewModel mViewModel;

    private LinearLayout mLinearLayout;

    public static WeekdayFragment newInstance() {
        return new WeekdayFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View self = inflater.inflate(R.layout.weekday_fragment, container, false);

        mLinearLayout = self.findViewById(R.id.cardholder);

        Bundle args = getArguments();

        LinkedList<Show> shows = new LinkedList<>();

        if(args != null) {
            for (int i = 0; i < args.size(); i++) {
                Parcel p = Parcel.obtain();
                Parcelable pa = args.getParcelable("" + i);
                if (pa != null) {
                    pa.writeToParcel(p, 0);
                }
                new Show(p);
            }
        }


        for(Show show : shows){
            View fullView = getLayoutInflater().inflate(R.layout.archive_show_card, mLinearLayout);
            ((TextView)fullView.findViewById(R.id.card_show_name)).setText(show.getName());
        }

        return self;
    }

    public static WeekdayFragment newInstance(LinkedList<Show> shows){
        WeekdayFragment f = new WeekdayFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        if(shows != null) {
            for (int i = 0; i < shows.size(); i++) {
                args.putParcelable("" + i, shows.get(i));
                f.setArguments(args);
            }
        }
        return f;
    }

    public LinearLayout getmLinearLayout() {
        return mLinearLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(WeekdayViewModel.class);
        // TODO: Use the ViewModel
    }

}
