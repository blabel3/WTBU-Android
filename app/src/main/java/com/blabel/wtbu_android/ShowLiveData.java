package com.blabel.wtbu_android;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.LinkedList;

import androidx.lifecycle.LiveData;

public class ShowLiveData extends LiveData<LinkedList<Show>> {
    private ArrayList<LinkedList<Show>> allShows;
    private int currentDay;

    public ShowLiveData(int currentDay){
        this.currentDay = currentDay;
        if(allShows == null){
            loadShows();
        }
    }

    public void loadShows(){
        //TODO: Data Leak?
        AsyncTask dataGetter = new FetchData() {
            @Override
            protected void onPostExecute(ArrayList<LinkedList<Show>> linkedLists) {
                super.onPostExecute(linkedLists);
                allShows = linkedLists;
                setValue(allShows.get(currentDay));
            }
        }.execute();
    }

    public void changeDay(int currentDay){
        this.currentDay = currentDay;
        setValue(allShows.get(currentDay));
    }

    public int dataDay(){
        return currentDay;
    }


}
