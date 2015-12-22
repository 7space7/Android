package com.ua.viktor.spotify.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ua.viktor.spotify.R;
import com.ua.viktor.spotify.fragment.DetailFragment;
import com.ua.viktor.spotify.fragment.MainFragment;


public class MainActivity extends AppCompatActivity implements MainFragment.Commute {
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String TAG_NAME ="NAME";
    public static String LOG_TAG = "my_log";
    private static boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (findViewById(R.id.movie_detail_container) != null) {

            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }

        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

    }



    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    public static boolean ismTwoPane() {
        return mTwoPane;
    }


    @Override
    public void myMusic(String position,String name) {
 //       Log.v("Name",name);


        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction

            Bundle args = new Bundle();
            args.putString(TAG, position);
            args.putString(TAG_NAME, name);
            DetailFragment detailFragment =new DetailFragment();
                detailFragment.setArguments(args);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, detailFragment, DETAILFRAGMENT_TAG)
                        .commit();
            } else {
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra(MainActivity.TAG,position);
                 intent.putExtra(MainActivity.TAG_NAME,name);
                startActivity(intent);
            }
        }
    }
