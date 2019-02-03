package com.emission.abnc.wtbu_android;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ArchiveRecyclerAdapter extends RecyclerView.Adapter<ArchiveRecyclerAdapter.MyViewHolder>{
    private String[] mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //correspondes to the views on the cards we will be making
        public CardView holderCard;

        public TextView showName;
        public ImageButton oneWeek;
        public ImageButton twoWeeks;

        public ImageButton control;
        public SeekBar elapsedTime;

        public MyViewHolder(View view){
            super(view);

            Log.d("WTBU-A", "ViewHolder created");

            holderCard = view.findViewById(R.id.show_card);

            showName = view.findViewById(R.id.card_show_name);
            oneWeek = view.findViewById(R.id.archive_recent_button);
            twoWeeks = view.findViewById(R.id.archive_later_button);

            control = view.findViewById(R.id.card_control_button);
            elapsedTime = view.findViewById(R.id.card_seek_bar);

        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ArchiveRecyclerAdapter(String[] myDataset) {
        mDataset = myDataset;
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.showName.setText(mDataset[position]);
        Log.d("WTBU-A", "Replacing");


        //replace the rest later

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Log.d("WTBU-A", "" + mDataset.length);
        return mDataset.length;
    }

}
