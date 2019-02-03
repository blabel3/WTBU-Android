package com.emission.abnc.wtbu_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.emission.abnc.wtbu_android.ui.streaming.ScheduleFragment;
import com.emission.abnc.wtbu_android.ui.streaming.StreamingFragment;
import com.emission.abnc.wtbu_android.ui.streaming.WTBUFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 3;

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    private BottomNavigationView bottomNavigationView;

    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";

    private boolean darkThemeEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Dark Theme
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        darkThemeEnabled = preferences.getBoolean(PREF_DARK_THEME, false);

        Log.d("WTBU-A", ((Boolean) darkThemeEnabled).toString());

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
                case 0: return ScheduleFragment.newInstance();
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
}
