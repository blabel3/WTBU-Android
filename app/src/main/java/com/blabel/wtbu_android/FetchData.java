package com.blabel.wtbu_android;

import android.os.AsyncTask;
import android.util.Log;

import com.blabel.wtbu_android.ui.streaming.ScheduleFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class FetchData extends AsyncTask<Void, Void, Void> {

    private static String url = "http://headphones.bu.edu/";
    LinkedList<LinkedList<String>> dayOfTheWeekData = ScheduleFragment.dayOfTheWeekData;


    protected void onPreExcecute(){
        super.onPreExecute();
        //Progress message
    }

    @Override
    protected Void doInBackground(Void... params) {

        LinkedList<String> sun = new LinkedList<>();
        LinkedList<String> mon = new LinkedList<>();
        LinkedList<String> tue = new LinkedList<>();
        LinkedList<String> wed = new LinkedList<>();
        LinkedList<String> thr = new LinkedList<>();
        LinkedList<String> fri = new LinkedList<>();
        LinkedList<String> sat = new LinkedList<>();

        dayOfTheWeekData.add(sun);
        dayOfTheWeekData.add(mon);
        dayOfTheWeekData.add(tue);
        dayOfTheWeekData.add(wed);
        dayOfTheWeekData.add(thr);
        dayOfTheWeekData.add(fri);
        dayOfTheWeekData.add(sat);
        dayOfTheWeekData.add(mon);

        try {
            //Connect to the site
            Document mShowList = Jsoup.connect(url).get();
            //Get its data
            Elements mElementDataSize = mShowList.select("table[id=bydate]");
            // Locate the content attribute
            int mElementSize = mElementDataSize.size();

            Log.d("WTBU-A", "tables "+ mElementSize);

            Elements headers = mElementDataSize.select("strong");

                /*Log.d("WTBU-A", "headers " + headers.size());

                Log.d("WTBU-A", "" + mElementDataSize.select("td").size());
                Log.d("WTBU-A", "" + mElementDataSize.select("td").next().size());
                Log.d("WTBU-A", "" + mElementDataSize.select("a").size());

                Log.d("WTBU-A", mElementDataSize.select("a").eachAttr("href"));

                List<String> niceHeaders = headers.eachText();

                niceHeaders.remove(niceHeaders.size() -1);
                for(String header : niceHeaders){
                    Log.d("WTBU-A", header);
                }*/

            List<String> links = mElementDataSize.select("a").eachAttr("href");

            //Example LoadArchive.php?mp3file=WTBU-2019-02-03_0000_to_0200_Aircheck.mp3&realname=Aircheck
            for(String link : links){
                //figure out what day of the week it belongs to
                String day = link.substring(37,39);
                String month = link.substring(34, 36);
                String year = link.substring(29, 33);

                int weekday = dayOfWeek(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year)); //sunday 0 through 6 saturday
                Log.d("WTBU-A", "" + weekday);

                String name = link.substring(link.indexOf("=", 25) + 1);
                String data = java.net.URLDecoder.decode(name, "UTF-8") + " " + "http://headphones.bu.edu/Archives/" + link.substring(link.indexOf("=") +1, link.indexOf("&"));

                Log.d("WTBU-A", data);

                dayOfTheWeekData.get(weekday).add(data);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void result) {
        Log.d("WTBU-A", "Success!");

        //sorts
        for(LinkedList<String> weekdayData : dayOfTheWeekData){
            Collections.sort(weekdayData, new Comparator<String>() {
                @Override
                public int compare(String s, String t1) {
                    return s.toLowerCase().compareTo(t1.toLowerCase());
                }
            });
        }

        for(LinkedList<String> weekdayData : dayOfTheWeekData){
            for(int i = 1; i < weekdayData.size(); i++){
                if(weekdayData.get(i).substring(0, 30).equals(weekdayData.get(i-1).substring(0, 30))){
                    String first = weekdayData.get(i-1);
                    String[] linksMore = weekdayData.get(i).split(" ");
                    weekdayData.set(i-1, first + " " + linksMore[linksMore.length -1]);
                    weekdayData.remove(i);
                }
            }
        }


    }


    private static int dayOfWeek(int d, int m, int y)
    {
        int t[] = { 0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4 };
        y -= (m < 3) ? 1 : 0;
        return ( y + y/4 - y/100 + y/400 + t[m-1] + d) % 7;
    }
}
