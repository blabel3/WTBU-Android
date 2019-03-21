package com.blabel.wtbu_android;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
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
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
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

public class HomeActivity extends AppCompatActivity implements PlayerCallbacks {

    private static final int NUM_PAGES = 3;

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    private BottomNavigationView bottomNavigationView;

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";

    private boolean darkThemeEnabled;

    private CardView playerCard;
    private PlayerView playerView;


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

    // Don't attempt to unbind from the service unless the client has received some
    // information about the service's state.
    private boolean mShouldUnbind;

    // To invoke the bound service, first make sure that this value
    // is not null.
    private PlayerService mBoundService;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mBoundService = ((PlayerService.LocalBinder)service).getService();

            // Tell the user about this for our demo.
            Log.v("WTBU-A", "Service connected to activity");
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mBoundService = null;
            Log.v("WTBU-A", "AAAAAA SERVICE COME BACK");
        }
    };

    void doBindService() {
        // Attempts to establish a connection with the service.  We use an
        // explicit class name because we want a specific service
        // implementation that we know will be running in our own process
        // (and thus won't be supporting component replacement by other
        // applications).
        if (bindService(new Intent(HomeActivity.this, PlayerService.class),
                mConnection, Context.BIND_AUTO_CREATE)) {
            mShouldUnbind = true;
        } else {
            Log.e("WTBU-A", "Error: The requested service doesn't " +
                    "exist, or this client isn't allowed access to it.");
        }
    }

    void doUnbindService() {
        if (mShouldUnbind) {
            // Release information about the service's state.
            unbindService(mConnection);
            mShouldUnbind = false;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        doBindService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from service
        if (mShouldUnbind) {
            mBoundService.setCallbacks(null); // unregister
            unbindService(mConnection);
            mShouldUnbind = false;
        }
    }

    public void showCard(){
        playerCard.setVisibility(View.VISIBLE);
    }

    public void hideCard(){
        //unbindService(//TODO);
        playerCard.setVisibility(View.GONE);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(PlayerService.getChannelID(), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void makeControls(SimpleExoPlayer player) {
        playerView.setPlayer(player);
    }

    @Override
    public void makeNotification(PlayerNotificationManager playerNotificationManager, SimpleExoPlayer player) {
        playerNotificationManager.setPlayer(player);
    }
}
