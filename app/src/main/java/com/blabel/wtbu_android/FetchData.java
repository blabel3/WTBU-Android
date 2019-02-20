package com.blabel.wtbu_android;

import android.os.AsyncTask;
import android.util.Log;

import com.blabel.wtbu_android.ui.streaming.ArchiveFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class FetchData extends AsyncTask<Void, Void, ArrayList<LinkedList<Show>>> {

    private ArchiveFragment mArchiveFragment;
    private static String url = "http://headphones.bu.edu/";

    public FetchData(ArchiveFragment caller){
        mArchiveFragment = caller;
    }

    protected void onPreExcecute(){
        super.onPreExecute();
        //Progress message
    }

    @Override
    protected ArrayList<LinkedList<Show>> doInBackground(Void... param) {

        ArrayList<LinkedList<Show>> archiveData = new ArrayList<>(7);

        LinkedList<Show> sun = new LinkedList<>();
        LinkedList<Show> mon = new LinkedList<>();
        LinkedList<Show> tue = new LinkedList<>();
        LinkedList<Show> wed = new LinkedList<>();
        LinkedList<Show> thr = new LinkedList<>();
        LinkedList<Show> fri = new LinkedList<>();
        LinkedList<Show> sat = new LinkedList<>();

        archiveData.add(sun);
        archiveData.add(mon);
        archiveData.add(tue);
        archiveData.add(wed);
        archiveData.add(thr);
        archiveData.add(fri);
        archiveData.add(sat);

        try {
            //Connect to the site
            Document mShowList = Jsoup.connect(url).get();
            //Get its data
            Elements mElementDataSize = mShowList.select("table[id=bydate]");
            // Locate the content attribute
            int mElementSize = mElementDataSize.size();

            Log.d("WTBU-A", "tables "+ mElementSize);

            Elements headers = mElementDataSize.select("strong");

            List<String> linksDump = mElementDataSize.select("a").eachAttr("href");
            ArrayList<String> links = new ArrayList<>(linksDump.size());
            links.addAll(linksDump);

            //Example LoadArchive.php?mp3file=WTBU-2019-02-03_0000_to_0200_Aircheck.mp3&realname=Aircheck
            for(String link : links){
                //figure out what day of the week it belongs to
                String day = link.substring(37,39);
                String month = link.substring(34, 36);
                String year = link.substring(29, 33);

                int weekday = dayOfWeek(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year)); //sunday 0 through 6 saturday

                String nameHTML = link.substring(link.indexOf("=", 25) + 1);
                String url =  "http://headphones.bu.edu/Archives/" + link.substring(link.indexOf("=") +1, link.indexOf("&"));

                Show radioShow = new Show(URLDecoder.decode(nameHTML, "UTF-8"), url, "");

                Log.d("WTBU-A", "" + weekday + " " + radioShow.getName());

                archiveData.get(weekday).add(radioShow);

            }

            for(LinkedList<Show> weekday : archiveData){
                sortShows(weekday);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return archiveData;

    }

    @Override
    protected void onPostExecute(ArrayList<LinkedList<Show>> archiveData) {
        mArchiveFragment.updateArchiveData(archiveData);

        Log.d("WTBU-A", "Success!");

    }

    private void sortShows(LinkedList<Show> shows){
        //sorts
        Collections.sort(shows);

        //combines duplicates
        for(int i = 1; i < shows.size(); i++){
            if(shows.get(i -1).isSameShow(shows.get(i))){
                shows.get(i - 1).setUrl2(shows.get(i).getUrl1());
                shows.remove(i);
                i--;
            }
        }
    }


    public static int dayOfWeek(int d, int m, int y)
    {
        int t[] = { 0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4 };
        y -= (m < 3) ? 1 : 0;
        return ( y + y/4 - y/100 + y/400 + t[m-1] + d) % 7;
    }
}
