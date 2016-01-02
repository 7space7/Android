package com.ua.viktor.game2048.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ua.viktor.game2048.R;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private TextView mScoreTextView;
    public static MainActivity mainActivity;
    private SoundPool mSoundPool;
    private AssetManager assets;
    private int mSound;
    private boolean music = true;
    private Button mRefreshButton;
    private Button mPauseButton;
    private Button mVolumeButton;

    public MainActivity() {
        mainActivity = this;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mRefreshButton= (Button) findViewById(R.id.refrehB);
        mPauseButton=(Button)findViewById(R.id.pauseB);
        mVolumeButton=(Button)findViewById(R.id.soundB);
        mScoreTextView = (TextView) findViewById(R.id.scoreText);


        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        assets = getAssets();
        mSound = loadSound("sound_slide.wav");

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),StartActivity.class);
                startActivity(intent);
            }
        });
        mVolumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (music) {
                    mVolumeButton.setBackgroundResource(R.drawable.volume_offbutton_selector);
                    music = false;
                }
                else {
                    mVolumeButton.setBackgroundResource(R.drawable.volume_upbutton_selector);
                    music = true;
                }
            }
        });
    }

    public void showScore(int score) {
        mScoreTextView.setText(score + "");
    }

    public void clearScore() {
        mScoreTextView.setText(0 + "");
    }

    private int loadSound(String fileName) {
        AssetFileDescriptor assetFileDescriptor;
        try {
            assetFileDescriptor = assets.openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        return mSoundPool.load(assetFileDescriptor, 1);
    }

    public void playSound() {
        if (music) {
            if (mSound > 0)
                mSoundPool.play(mSound, 1, 1, 1, 0, 1);
        }
        else if (!music) {
            if (mSound > 0)
                mSoundPool.stop(mSound);
        }
    }
}
