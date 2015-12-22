package com.ua.viktor.spotify.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ua.viktor.spotify.R;
import com.ua.viktor.spotify.fragment.DetailFragment;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG =DetailActivity.class.getName() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent=getIntent();
        String position=intent.getStringExtra(MainActivity.TAG);
        String name=intent.getStringExtra(MainActivity.TAG_NAME);

        if (savedInstanceState == null) {
            DetailFragment fragment = new DetailFragment();
            Bundle arguments = new Bundle();
            arguments.putString(MainActivity.TAG, position);
            arguments.putString(MainActivity.TAG_NAME, name);
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
