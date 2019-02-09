package com.blabel.wtbu_android.ui.streaming;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blabel.wtbu_android.HomeActivity;
import com.blabel.wtbu_android.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class StreamingFragment extends Fragment {

    private StreamingViewModel mViewModel;

    public static StreamingFragment newInstance() {
        return new StreamingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.streaming_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.play_stream_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HomeActivity act = (HomeActivity) getActivity();

                act.showCard();

                act.setAudioUrl("http://wtbu.bu.edu:1800/");
                act.releasePlayer();
                act.initializePlayer("http://wtbu.bu.edu:1800/");
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StreamingViewModel.class);
        // TODO: Use the ViewModel
    }

}
