package com.blabel.wtbu_android;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

public interface PlayerCallbacks {
    public void makeNotification(PlayerNotificationManager playerNotificationManager, SimpleExoPlayer player);
    public void makeControls(SimpleExoPlayer player);
}
