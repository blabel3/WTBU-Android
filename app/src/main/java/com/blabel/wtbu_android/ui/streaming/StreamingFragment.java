package com.blabel.wtbu_android.ui.streaming;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blabel.wtbu_android.HomeActivity;
import com.blabel.wtbu_android.PlayerService;
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

                String audioUrl = "http://wtbu.bu.edu:1800/";
                Intent playerIntent = new Intent(act, PlayerService.class);
                playerIntent.putExtra("audioURL", "http://wtbu.bu.edu:1800/");
                Log.v("WTBU-A", "Trying to start service");
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    act.startService(playerIntent);
                } else {
                    act.startService(playerIntent);
                }
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
