package com.blabel.wtbu_android;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ArchiveRecyclerAdapter extends RecyclerView.Adapter<ArchiveRecyclerAdapter.MyViewHolder>{
    private LinkedList<Show> mDataset;
    private Context mContext;

    private SimpleExoPlayer archivePlayer;
    private boolean playWhenReady = true;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //correspondes to the views on the cards we will be making
        public CardView holderCard;

        public TextView showName;
        public ImageButton oneWeek;
        public ImageButton twoWeeks;

        public ImageButton control;
        public SeekBar elapsedTime;

        public PlayerView playerViewEarly;
        public PlayerView playerViewLate;

        public MyViewHolder(View view){
            super(view);

            Log.d("WTBU-A", "ViewHolder created");

            holderCard = view.findViewById(R.id.show_card);

            showName = view.findViewById(R.id.card_show_name);
            oneWeek = view.findViewById(R.id.archive_recent_button);
            twoWeeks = view.findViewById(R.id.archive_later_button);

            control = view.findViewById(R.id.card_control_button);
            elapsedTime = view.findViewById(R.id.card_seek_bar);

            playerViewEarly = view.findViewById(R.id.video_early_view);
            playerViewLate = view.findViewById(R.id.video_late_view);

            playerViewEarly.setControllerShowTimeoutMs(0);
            playerViewLate.setControllerShowTimeoutMs(0);

        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ArchiveRecyclerAdapter(Context fragmentDad, LinkedList<Show> myDataset) {
        mDataset = myDataset;
        mContext = fragmentDad;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ArchiveRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.archive_show_card, parent, false);

        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Show show = mDataset.get(position);

        holder.showName.setText(show.getName());

        if(show.getUrl1()!=null) {
            holder.oneWeek.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setVisibility(View.INVISIBLE);
                    initializePlayer(holder.playerViewEarly, show.getUrl1());
                    holder.playerViewEarly.setVisibility(View.VISIBLE);
                }
            });
        } else {
            holder.oneWeek.setVisibility(View.GONE);
        }

        if(show.getUrl2()!=null) {
            holder.twoWeeks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setVisibility(View.INVISIBLE);
                    initializePlayer(holder.playerViewLate, show.getUrl2());
                    holder.playerViewLate.setVisibility(View.VISIBLE);
                }
            });
        } else {
            holder.twoWeeks.setVisibility(View.GONE);
        }

    }

    private void initializePlayer(PlayerView playerView, String link) {
        archivePlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(mContext),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(archivePlayer);

        archivePlayer.setPlayWhenReady(playWhenReady);
        archivePlayer.seekTo(0, 0);

        Uri uri = Uri.parse(link);
        MediaSource mediaSource = buildMediaSource(uri);
        archivePlayer.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("WTBU-Android")).
                createMediaSource(uri);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void clear() {
        int size = mDataset.size();
        mDataset.clear();
        notifyItemRangeRemoved(0, size);
    }


}
