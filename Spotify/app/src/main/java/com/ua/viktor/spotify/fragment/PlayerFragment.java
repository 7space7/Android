package com.ua.viktor.spotify.fragment;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ua.viktor.spotify.R;
import com.ua.viktor.spotify.model.Track;
import com.ua.viktor.spotify.service.MusicPlayService;

import java.util.concurrent.TimeUnit;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PlayerFragment extends DialogFragment implements ServiceConnection {

    private Context context;

    LocalBroadcastManager localBroadcastManager;
    private ImageView mPlayerControl;

    private SeekBar seekbar;

    private TextView beginTime;
    private TextView endTime;

    private MusicPlayService musicPlayService;

    Track [] tracks;
    Track track;
    static Track playing;
    static int position;
    static Track[] playingTracks;

   private TextView nameSong;
   private TextView nameAlbum;
   private ImageView imageAlbum;
    public PlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player, container, false);
        nameSong = (TextView) view.findViewById(R.id.nameAlbum);
        nameAlbum = (TextView) view.findViewById(R.id.nameSong);
        imageAlbum = (ImageView) view.findViewById(R.id.imageAlbum);
        mPlayerControl = (ImageButton) view.findViewById(R.id.media_play);
        ImageButton next=(ImageButton)view.findViewById(R.id.media_next);
        ImageButton prev=(ImageButton)view.findViewById(R.id.media_prev);
        beginTime = (TextView) view.findViewById(R.id.startTime);
        endTime = (TextView) view.findViewById(R.id.endTime);
        seekbar = (SeekBar) view.findViewById(R.id.scrubBar);
        seekbar.setClickable(false);

        context = getActivity().getApplicationContext();



            Intent bindIntent = new Intent(context, MusicPlayService.class);
            context.startService(bindIntent);
            context.bindService(bindIntent, this, Context.BIND_AUTO_CREATE);

            localBroadcastManager = LocalBroadcastManager.getInstance(context);



        updateTrack();
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPrev();
            }
        });
       next.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        selectNext();
         }
       });
        mPlayerControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTracks();
            }
        });


        return view;
    }
    @Override
    public void onPause() {
        localBroadcastManager.unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (musicPlayService != null) {
            context.unbindService(this);
        }

        super.onDestroy();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        Bundle arguments = getArguments();
        if (arguments != null) {
            tracks = (Track[]) arguments.getParcelableArray(DetailFragment.TAG);
            position = arguments.getInt("lol");
          //  Log.v("lolka", position + "");
            if (tracks != null)
                track = tracks[position];

          //  Log.v("lolka", track.getNameSong() + "");


           /* nameAlbum.setText(mTracks[position].getAlbum());
            nameSong.setText(mTracks[position].getNameSong());
            Glide.with(view.getContext()).load(mTracks[position].getImageSmall()).into(imageAlbum);
            String url = mTracks[position].getPreviewUrl(); // your URL here*/

        }
        setRetainInstance(true);

        Intent bindIntent = new Intent(context, MusicPlayService.class);
        context.startService(bindIntent);
        context.bindService(bindIntent, this, Context.BIND_AUTO_CREATE);

        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    private void updateTrack() {
        if (track != null) {
            //  artistName.setText(" " + track.getArtistName());
            nameAlbum.setText(" " + track.getAlbum());
            nameSong.setText(" " + track.getNameSong());
            if(track.getImageSmall()!=null) {
                Glide.with(getActivity()).load(track.getImageSmall()).into(imageAlbum);
            }else {
                Glide.with(getActivity()).load(R.drawable.ic_music).into(imageAlbum);
            }

            mPlayerControl.setImageResource(android.R.drawable.ic_media_play);
            beginTime.setText(String.format("%02d:%02d", 0, 0));
            endTime.setText(String.format("%02d:%02d", 0, 0));
            seekbar.setProgress(0);
        }
    }

    public void playTracks() {
        if (musicPlayService != null) {
            // musicPlayService.setTracks(tracks);
            // musicPlayService.setPosition(position);
            playTrack(position);
        }
    }
    @Override
    public void onResume() {

        localBroadcastManager
                .registerReceiver(receiver, new IntentFilter(MusicPlayService.MEDIA_PLAYER_STATUS));
        localBroadcastManager
                .registerReceiver(receiver, new IntentFilter(MusicPlayService.MEDIA_PLAYER_NEW_TRACK));
        super.onResume();
    }
    public void playTrack(int position) {
        if (musicPlayService != null) {
            musicPlayService.setTracks(tracks);
            musicPlayService.setPosition(position);
            musicPlayService.playTrack(position);
        }
    }

    public void selectPrev() {
        if (musicPlayService != null)
            musicPlayService.playPrev();
    }

    public void selectNext() {
        if (musicPlayService != null)
            musicPlayService.playNext();
    }

    // http://stackoverflow.com/questions/12433397/android-dialogfragment-disappears-after-orientation-change
    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        musicPlayService = ((MusicPlayService.LocalBinder) service).getService();
        musicPlayService.setTracks(tracks);
        musicPlayService.setPosition(position);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicPlayService = null;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MusicPlayService.MEDIA_PLAYER_STATUS)) {
                playing = intent.getParcelableExtra(MusicPlayService.TRACK_INFO);
             //   ((ReadyToShare) getActivity()).onReadyToShare();
                if (playing.getPreviewUrl().equals(track.getPreviewUrl())) {
                    double progress = intent.getDoubleExtra(MusicPlayService.TRACK_PROGRESS, 0.0);
                    double duration = intent.getDoubleExtra(MusicPlayService.TRACK_DURATION, 0.0);
                    beginTime.setText(String.format("%02d:%02d",
                                    TimeUnit.MILLISECONDS.toMinutes((long) progress),
                                    TimeUnit.MILLISECONDS.toSeconds((long) progress) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                    toMinutes((long) progress)))
                    );
                    endTime.setText(String.format("%02d:%02d",
                                    TimeUnit.MILLISECONDS.toMinutes((long) duration),
                                    TimeUnit.MILLISECONDS.toSeconds((long) duration) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                                    toMinutes((long) duration)))
                    );
                    seekbar.setProgress((int) (progress * 100 / duration));

                    boolean isPlaying = intent.getBooleanExtra(MusicPlayService.TRACK_STATUS, false);
                    if (isPlaying) {
                        mPlayerControl.setImageResource(android.R.drawable.ic_media_pause);
                    }
                    else if(!isPlaying) {
                        mPlayerControl.setImageResource(android.R.drawable.ic_media_play);
                    }
                }
            } else if (intent.getAction().equals(MusicPlayService.MEDIA_PLAYER_NEW_TRACK)) {
                track = playing = intent.getParcelableExtra(MusicPlayService.TRACK_INFO);
                playingTracks = (Track[]) intent.getParcelableArrayExtra(MusicPlayService.TOP_TRACK_LIST);
                position = intent.getIntExtra(MusicPlayService.TRACK_POSITION, 0);
                updateTrack();
                //Log.i(LOG_TAG, "NEW TRACK recorded");
            }
        }
    };
    public interface ReadyToShare {
        void onReadyToShare();
    }
}
