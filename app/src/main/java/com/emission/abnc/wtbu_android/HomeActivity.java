package com.emission.abnc.wtbu_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.emission.abnc.wtbu_android.ui.streaming.StreamingFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, StreamingFragment.newInstance())
                    .commitNow();
        }
    }
}
