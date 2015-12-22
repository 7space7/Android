package com.ua.viktor.spotify.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;
import com.ua.viktor.spotify.R;
import com.ua.viktor.spotify.model.Track;

import java.io.IOException;

/**
 * Created by viktor on 14.09.15.
 */
public class MusicPlayService extends Service {
    static MediaPlayer mediaPlayer;
    static Track mTrack;
    static Track[] mTracks;
    static int mPosition;
   private  Notification notification;

    private static Handler handler = new Handler();
    private final IBinder mBinder = new LocalBinder();
    LocalBroadcastManager broadcastManager;
    Context context;

    // Track play status for each current playing track
    // Intent filter
    public static final String MEDIA_PLAYER_STATUS = "MEDIA_PLAYER_STATUS";
    public static final String TRACK_PROGRESS = "TRACK_PROGRESS";
    public static final String TRACK_DURATION = "TRACK_DURATION";
    public static final String TRACK_STATUS = "TRACK_STATUS";
    public static final String TRACK_INFO = "TRACK_INFO";

    // Track info <list of top tracks and position of current playing track>
    // Intent filter
    public static final String MEDIA_PLAYER_NEW_TRACK = "MEDIA_PLAYER_NEW_TRACK";
    public static final String TRACK_POSITION = "TRACK_POSITION";
    public static final String TOP_TRACK_LIST = "TOP_TRACK_LIST";

    public static String PREV_ACTION = "com.udacity.android.spotify.action.prev";
    public static String PLAY_ACTION = "com.udacity.android.spotify.action.play";
    public static String NEXT_ACTION = "com.udacity.android.spotify.action.next";

    public static final int NOTIFICATION_ID = 101;

    private final String LOG_TAG = MusicPlayService.class.getSimpleName();
    boolean finished = false;

    public MusicPlayService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        context = getApplicationContext();
        broadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        mTrack = null;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public MusicPlayService getService() {
            return MusicPlayService.this;
        }
    }

    public void setTracks(Track []tracks) {
        mTracks = tracks;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public void loadAndPlay(final Track track) {
        String url = track.getPreviewUrl();

        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer player) {
                player.start();
                mTrack = track;
                handler.postDelayed(updateTrack, 100);

                // Put service at Foreground and send out notification
                startForeground(NOTIFICATION_ID, buildNotification());

                // Broadcast current playing list and current track position
                // This will let other activity/fragment to popup current playing track control
                Intent intent = new Intent(MEDIA_PLAYER_NEW_TRACK);
                intent.putExtra(TRACK_INFO, mTrack);
              //  intent.putExtra(TRACK_ARTIST_ID, mTrack.getArtistId());
                intent.putExtra(TOP_TRACK_LIST, mTracks);
                intent.putExtra(TRACK_POSITION, mPosition);
                broadcastManager.sendBroadcast(intent);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                handler.removeCallbacks(updateTrack);
                double duration = mediaPlayer.getDuration();

                // Broadcast current playing status (COMPLETED)
                Intent intent = new Intent(MEDIA_PLAYER_STATUS);
                intent.putExtra(TRACK_INFO, mTrack);
                intent.putExtra(TRACK_STATUS, mediaPlayer.isPlaying());
                intent.putExtra(TRACK_PROGRESS, 0.0);
                intent.putExtra(TRACK_DURATION, duration);
                broadcastManager.sendBroadcast(intent);
                buildNotification();
                finished = true;
            }
        });
    }

    public void playTrack(Track track) {

        if (mediaPlayer.isPlaying()) {
            // Pause the track

            mediaPlayer.pause();

            // Switch to a new track? Load and play
            if (track != mTrack) {
                handler.removeCallbacks(updateTrack);
                mediaPlayer.reset();
                loadAndPlay(track);
            }

        } else {
            if (track == mTrack) {
                // If track is completed, and user wanted to play again, reset to update progress again
                if (finished) {
                    handler.postDelayed(updateTrack, 100);
                    finished = false;
                }
                // Resume the same track after pause, no need to load new track
                mediaPlayer.start();
            } else {
                // Starting a new track, load and play
                mediaPlayer.reset();
                loadAndPlay(track);
            }
        }
    }

    public void playTrack(int position) {
        if (mTracks != null) {
            mPosition = position;
            playTrack(mTracks[position]);
        }
    }

    private Runnable updateTrack = new Runnable() {
        public void run() {
            if (mediaPlayer != null) {
                double progress = mediaPlayer.getCurrentPosition();
                double duration = mediaPlayer.getDuration();

                // Broadcast current playing status (STARTED)
                // Will let control UI to update progress and time
                Intent intent = new Intent(MEDIA_PLAYER_STATUS);
                intent.putExtra(TRACK_INFO, mTrack);
                intent.putExtra(TRACK_STATUS, mediaPlayer.isPlaying());
                intent.putExtra(TRACK_PROGRESS, progress);
                intent.putExtra(TRACK_DURATION, duration);
                broadcastManager.sendBroadcast(intent);
                handler.postDelayed(this, 100);
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null
                    && (action.equals(PLAY_ACTION)
                    || action.equals(PREV_ACTION)
                    || action.equals(NEXT_ACTION))) {

                mPosition = intent.getIntExtra(TRACK_POSITION, 0);
                playTrack(mPosition);
                buildNotification();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public int selectPrev() {
        int position = mPosition;
        return (position > 0) ? position - 1 : mTracks.length - 1;
    }

    public int selectNext() {
        int position = mPosition;
        return (position < mTracks.length - 1) ? position + 1 : 0;
    }

    public void playNext() {
        mPosition = selectNext();
        playTrack();
    }

    public void playPrev() {
        mPosition = selectPrev();
        playTrack();
    }

    public void playTrack() {
        playTrack(mPosition);
    }

    private Notification buildNotification() {

  notification = new NotificationCompat.Builder(getApplicationContext())

                .setAutoCancel(true)
                .setVisibility(getNotificationVisibility())
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle(mTrack.getAlbum())
                .setContentText(mTrack.getNameSong())
                        // http://stackoverflow.com/questions/16032031/android-remove-notificationcreated-by-mnotifybuilder-on-click
                .setContentIntent(PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        new Intent(),
                        PendingIntent.FLAG_CANCEL_CURRENT))
                .build();
        //notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            notification.bigContentView = getExpandedView();

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);

        return notification;
    }

    // https://github.com/PaulTR/AndroidDemoProjects/tree/master/NotificationsCustomLayout
    private RemoteViews getExpandedView() {
        final RemoteViews customView = new RemoteViews(getPackageName(), R.layout.notification);
        customView.setTextViewText(R.id.notification_title, mTrack.getAlbum());
        customView.setTextViewText(R.id.notification_text, mTrack.getNameSong());
        if(mTrack.getImageSmall()!=null) {
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {

                    Picasso
                            .with(getApplicationContext())
                            .load(mTrack.getImageSmall())
                            .into(customView, R.id.notification_image, NOTIFICATION_ID, notification);

                }
            });
        }
        customView.setImageViewResource(
                R.id.btnPrevious, android.R.drawable.ic_media_previous);


        if (mediaPlayer.isPlaying())
            customView.setImageViewResource(R.id.btnPlay, android.R.drawable.ic_media_pause);
        else
            customView.setImageViewResource(R.id.btnPlay, android.R.drawable.ic_media_play);

        customView.setImageViewResource(R.id.btnNext, android.R.drawable.ic_media_next);


        Intent playIntent = new Intent(context, MusicPlayService.class);
        playIntent.putExtra(TRACK_POSITION, mPosition);
        playIntent.setAction(PLAY_ACTION);
        PendingIntent pausePendingIntent =
                PendingIntent.getService(
                        this,
                        0,
                        playIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        customView.setOnClickPendingIntent(R.id.btnPlay, pausePendingIntent);

        Intent prevIntent = new Intent(this, MusicPlayService.class);
        prevIntent.putExtra(TRACK_POSITION, selectPrev());
        prevIntent.setAction(PREV_ACTION);
        PendingIntent prevPendingIntent =
                PendingIntent.getService(
                        this,
                        0,
                        prevIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        customView.setOnClickPendingIntent(R.id.btnPrevious, prevPendingIntent);

        Intent nextIntent = new Intent(this, MusicPlayService.class);
        nextIntent.putExtra(TRACK_POSITION, selectNext());
        nextIntent.setAction(NEXT_ACTION);
        PendingIntent nextPendingIntent =
                PendingIntent.getService(
                        this,
                        0,
                        nextIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        customView.setOnClickPendingIntent(R.id.btnNext, nextPendingIntent);


        return customView;
    }

    int getNotificationVisibility() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String value = pref.getString(getString(
                R.string.pref_notifications_visibilty_key), "0");

        int visibility = 0;
        switch (value) {

            case "PUBLIC":
                visibility = Notification.VISIBILITY_PUBLIC;
                break;

            case "PRIVATE":
                visibility = Notification.VISIBILITY_PRIVATE;
                break;

            case "SECRET":
                visibility = Notification.VISIBILITY_SECRET;
                break;
        }

        Log.i(LOG_TAG, "Current visibility is set to: " + value);

        return visibility;
    }
}
