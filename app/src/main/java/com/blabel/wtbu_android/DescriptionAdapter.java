package com.blabel.wtbu_android;

import android.app.PendingIntent;
import android.graphics.Bitmap;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

import androidx.annotation.Nullable;

public class DescriptionAdapter implements PlayerNotificationManager.MediaDescriptionAdapter{
    @Override
    public String getCurrentContentTitle(Player player) {
        return "WTBU Radio";
    }

    @Nullable
    @Override
    public String getCurrentContentText(Player player) {
        return "Fresh jams from your BU friends " +
                "\uD83D\uDE04";
    }

    @Nullable
    @Override
    public Bitmap getCurrentLargeIcon(Player player,
                                      PlayerNotificationManager.BitmapCallback callback) {
            /*int window = player.getCurrentWindowIndex();
            Bitmap largeIcon = getLargeIcon(window);
            if (largeIcon == null && getLargeIconUri(window) != null) {
                // load bitmap async
                loadBitmap(getLargeIconUri(window), callback);
                return getPlaceholderBitmap();
            }
            return largeIcon; */
        return null;
    }

    @Nullable
    @Override
    public PendingIntent createCurrentContentIntent(Player player) {
            /*int window = player.getCurrentWindowIndex();
            return createPendingIntent(window); */
        return null;
    }

}