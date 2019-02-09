package com.blabel.wtbu_android;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.LinkedList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ArchiveRecyclerAdapter extends RecyclerView.Adapter<ArchiveRecyclerAdapter.MyViewHolder>{
    private LinkedList<Show> mDataset;
    private HomeActivity mActivity;

    private SimpleExoPlayer archivePlayer;
    private boolean playWhenReady = true;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //correspondes to the views on the cards we will be making
        public CardView holderCard;

        public TextView showName;
        public ImageButton oneWeek;
        public ImageButton twoWeeks;

        public MyViewHolder(View view){
            super(view);

            Log.d("WTBU-A", "ViewHolder created");

            holderCard = view.findViewById(R.id.show_card);

            showName = view.findViewById(R.id.card_show_name);
            oneWeek = view.findViewById(R.id.archive_recent_button);
            twoWeeks = view.findViewById(R.id.archive_later_button);

        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ArchiveRecyclerAdapter(HomeActivity fragmentDad, LinkedList<Show> myDataset) {
        mDataset = myDataset;
        mActivity = fragmentDad;
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
                    mActivity.showCard();

                    mActivity.setAudioUrl(show.getUrl1());
                    mActivity.releasePlayer();
                    mActivity.initializePlayer(show.getUrl1());
                }
            });
        } else {
            holder.oneWeek.setVisibility(View.GONE);
        }

        if(show.getUrl2()!=null) {
            holder.twoWeeks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActivity.showCard();

                    mActivity.setAudioUrl(show.getUrl2());
                    mActivity.releasePlayer();
                    mActivity.initializePlayer(show.getUrl2());

                }
            });
        } else {
            holder.twoWeeks.setVisibility(View.GONE);
        }

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
