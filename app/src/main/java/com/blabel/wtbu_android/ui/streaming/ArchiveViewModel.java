package com.blabel.wtbu_android.ui.streaming;

import android.app.Application;

import com.blabel.wtbu_android.Show;
import com.blabel.wtbu_android.ShowLiveData;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArchiveViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private ShowLiveData showData;

    public ArchiveViewModel(){
        super();
        Calendar c = new GregorianCalendar();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        showData = new ShowLiveData(dayOfWeek);
    }

    public ShowLiveData getShows(){
        return showData;
    }
}
