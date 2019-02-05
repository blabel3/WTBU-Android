package com.blabel.wtbu_android;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

public class Show implements Comparable<Show>, Parcelable {

    String name;
    String url1;
    String url2;
    //djs?
    //custom show color?
    //custom show image link?
    //air time?

    public static final Parcelable.Creator<Show> CREATOR = new Parcelable.Creator<Show>() {
        public Show createFromParcel(Parcel in) {
            return new Show(in);
        }

        public Show[] newArray(int size) {
            return new Show[size];
        }
    };

    public Show(){

    }

    public Show(Parcel in){
        name = in.readString();
        url1 = in.readString();
        url2 = in.readString();
    }

    public Show(String name, String url1, String url2){
        this.name = name;
        this.url1 = url1;
        this.url2 = url2;
    }

    public String getName() {
        return name;
    }

    public String getUrl1() {
        return url1;
    }

    public String getUrl2() {
        return url2;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    @Override
    public int compareTo(Show s2) {
        return getName().compareToIgnoreCase(s2.getName());
    }

    public boolean isSameShow(Show s2){
        return getName().equals(s2.getName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.url1);
        dest.writeString(this.url2);
    }

}
