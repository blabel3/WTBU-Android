package com.blabel.wtbu_android;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import androidx.annotation.Nullable;

public class PlayerService extends IntentService {
    private PlayerNotificationManager playerNotificationManager;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    public static final String CHANNEL_ID = "wtbu-android";
    private static final int NOTIFICATION_ID = 5;

    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        PlayerService getService() {
            return PlayerService.this;
        }
    }

    public PlayerService(){
        super("WTBU Media Player");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        releasePlayer();
        String url = intent.getExtras().getString("audioURL");
        Log.v("WTBU-A", "Intent Sent " + url);
        initializePlayer(url);

    }

    @Override
    public void onCreate() {
        Log.v("WTBU-A", "Service created");

        playerNotificationManager = new PlayerNotificationManager(
                getApplicationContext(),
                CHANNEL_ID,
                NOTIFICATION_ID,
                new DescriptionAdapter());

        playerNotificationManager.setStopAction(null);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        playerNotificationManager.setPlayer(null);

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    public static String getChannelID(){
        return CHANNEL_ID;
    }

    public void initializePlayer(String audioUrl) {
        player = ExoPlayerFactory.newSimpleInstance(getApplicationContext());
        //playerNotificationManager.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(audioUrl);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("WTBU-Android")).
                createMediaSource(uri);
    }

    public void releasePlayer() {
        if (player != null) {
            playerNotificationManager.setPlayer(null);
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

}
