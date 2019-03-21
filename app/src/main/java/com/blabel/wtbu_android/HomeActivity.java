package com.blabel.wtbu_android;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.blabel.wtbu_android.ui.streaming.ArchiveFragment;
import com.blabel.wtbu_android.ui.streaming.StreamingFragment;
import com.blabel.wtbu_android.ui.streaming.WTBUFragment;
import com.google.android.exoplayer2.ui.PlayerView;
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

    private CardView playerCard;
    public static PlayerView playerView;

    private PlayerService mService;
    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Dark Theme
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        darkThemeEnabled = preferences.getBoolean(PREF_DARK_THEME, false);

        Log.d("WTBU-A", "Dark theme enabled: " + ((Boolean) darkThemeEnabled).toString());

        if(darkThemeEnabled){
            setTheme(R.style.AppThemeDark);
        }

        //Create activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        createNotificationChannel();

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
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
        mBound = false;
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

    public void startMedia(String audioUrl){
        //Create and start service
        Intent playerIntent = new Intent(this, PlayerService.class);
        playerIntent.putExtra("audioURL", audioUrl);
        Log.v("WTBU-A", "Trying to start service");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(playerIntent);
        } else {
            startService(playerIntent);
        }

        //bind to service
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, serviceConnection, 0);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PlayerService.PlayerBinder binder = (PlayerService.PlayerBinder) iBinder;
            mService = binder.getService();
            showCard();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
            hideCard();
        }
    };

    public void showCard(){
        playerCard.setVisibility(View.VISIBLE);
        Log.v("WTBU-A", "Showing controls??");
    }

    public void hideCard(){
        mService.releasePlayer();
        unbindService(serviceConnection);
        playerCard.setVisibility(View.GONE);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(PlayerService.getChannelID(), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
