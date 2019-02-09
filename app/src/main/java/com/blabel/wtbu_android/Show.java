package com.blabel.wtbu_android;

public class Show implements Comparable<Show> {

    private String name;
    private String url1;
    private String url2;
    //djs?
    //custom show color?
    //custom show image link?
    //air time?

    public Show(){

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

}
