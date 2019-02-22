package com.blabel.wtbu_android;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.blabel.wtbu_android.ui.streaming.ArchiveFragment;
import com.blabel.wtbu_android.ui.streaming.StreamingFragment;
import com.blabel.wtbu_android.ui.streaming.WTBUFragment;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

//import com.google.android.exoplayer2.ui.PlayerNotificationManager;

public class HomeActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 3;

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    private BottomNavigationView bottomNavigationView;

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";

    private boolean darkThemeEnabled;

    private PlayerView playerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    //PlayerNotificationManager playerNotificationManager;

    private String audioUrl;

    private CardView playerCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Dark Theme
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        darkThemeEnabled = preferences.getBoolean(PREF_DARK_THEME, false);

        Log.d("WTBU-A", "Dark theme enabled: " + ((Boolean) darkThemeEnabled).toString());

        audioUrl = "";

        if(darkThemeEnabled){
            setTheme(R.style.AppThemeDark);
        }

        //Create activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.fragment_slider);
        viewPager.setAdapter(pagerAdapter);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        playerCard = findViewById(R.id.media_player_card);
        playerCard.setVisibility(View.GONE);

        //Set up media player in the card
        playerView = findViewById(R.id.video_view);
        playerView.setControllerShowTimeoutMs(0);

        ((Button)findViewById(R.id.dismiss_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideCard();
            }
        });

        //hideCard();

        //playerNotificationManager

        //Linking the Bottom Navigation to the ViewPager
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch(menuItem.getItemId()){
                            case R.id.schedule: viewPager.setCurrentItem(0);
                                break;
                            case R.id.radio: viewPager.setCurrentItem(1);
                                break;
                            case R.id.wtbu: viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                }
        );

        //Linking the ViewPager to the Bottom Navigation
        viewPager.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        bottomNavigationView.getMenu().getItem(position).setChecked(true);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }
        );

    }

    public static class MyPagerAdapter extends FragmentPagerAdapter{
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            //return ArrayListFragment.newInstance(position);
            switch(position){
                case 0: return ArchiveFragment.newInstance();
                case 1: return StreamingFragment.newInstance();
                default: return WTBUFragment.newInstance();
                //default: Log.e("WTBU-A", "Pager out of bounds");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.dark_theme_menu).setChecked(darkThemeEnabled);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.dark_theme_menu:
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                item.setChecked(!item.isChecked());
                editor.putBoolean(PREF_DARK_THEME, item.isChecked());
                editor.apply();
                recreate();
                return true;
            case R.id.about_menu:
                //showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initializePlayer(String urlSource) {
        player = ExoPlayerFactory.newSimpleInstance(this);

        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(urlSource);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("WTBU-Android")).
                createMediaSource(uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(audioUrl);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer(audioUrl);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    public void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public void showCard(){
        playerCard.setVisibility(View.VISIBLE);
    }

    public void hideCard(){
        releasePlayer();
        playerCard.setVisibility(View.GONE);
    }


}
