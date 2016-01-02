package com.ua.viktor.movies.ui;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ua.viktor.movies.R;
import com.ua.viktor.movies.adapter.MovieAdapter;
import com.ua.viktor.movies.data.MovieContract;
import com.ua.viktor.movies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends AppCompatActivity {
public static String TAG=MainActivity.class.getSimpleName();
    RecyclerView mRecyclerView;

    RecyclerView.Adapter mAdapter;

    Movie[]movie;
   // private MovieCursorAdapter mFlavorAdapter;


    private static String movieKey="movie";

    private final String api_key = "0046de4ee266fbe5d82529513745db70";
    private static String mSort="popularity.desc";
    private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      /*  Cursor c =
                getApplication().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                        new String[]{MovieContract.MovieEntry._ID},
                        null,
                        null,
                        null);
        if (c.getCount() == 0){
            insertData();
        }
        mCallbacks = this;
        // initialize loader
        LoaderManager lm = getLoaderManager();
        lm.initLoader(CURSOR_LOADER_ID, null,mCallbacks);

        mFlavorAdapter = new MovieCursorAdapter(getApplication(), null, 0, CURSOR_LOADER_ID);
        // initialize mGridView to the GridView in fragment_main.xml
        mGridView = (GridView) findViewById(R.id.flavors_grid);
        // set mGridView adapter to our CursorAdapter
        mGridView.setAdapter(mFlavorAdapter);
      /*  android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);*/

        //emptyView=(TextView)findViewById(R.id.empty_view);




        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);



    if (savedInstanceState == null || !savedInstanceState.containsKey(movieKey)) {

             getMovieData(mSort);

    } else {
        movie = (Movie[]) savedInstanceState.getParcelableArray(movieKey);
        if(movie!=null) {
            movieUI();
        }
    }
}

public void getMovieData(String sort_by){
    String movieUrl = "http://api.themoviedb.org/3/discover/movie?sort_by="+sort_by+"&api_key=" + api_key;
    if(isNetworkAvaible()) {
    OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(movieUrl)
                .addHeader("Accept", "application/json")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                    String json = response.body().string();

                    movie = getMovieDB(json);
                       // insertData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                movieUI();
                            }
                        });
                    }
                } catch (IOException e) {
                    Log.e(TAG, "EROR", e);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }else{
        Toast.makeText(this, "Network isn't available", Toast.LENGTH_SHORT).show();
    }
}
    private boolean isNetworkAvaible() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isAvailble=false;
        if(networkInfo!=null&&networkInfo.isConnected()){
            isAvailble=true;
        }
        return isAvailble;
    }
public void movieUI()
    {


            mAdapter = new MovieAdapter(movie);
            mRecyclerView.setAdapter(mAdapter);
        if(getApplication().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
           mRecyclerView.setLayoutManager(new GridLayoutManager(getApplication(), 2));
        }
        else{
           mRecyclerView.setLayoutManager(new GridLayoutManager(getApplication(), 3));
        }
           // mRecyclerView.setLayoutManager(new GridLayoutManager(getApplication(), 2));
            mRecyclerView.setHasFixedSize(true);

    }

public Movie[] getMovieDB(String jsonData) throws JSONException {
    JSONObject jsonObject=new JSONObject(jsonData);
    JSONArray data=jsonObject.getJSONArray("results");
    Movie[]movies=new Movie[data.length()];
    for (int i=0;i<data.length();i++){
        JSONObject jsonMovie=data.getJSONObject(i);
        Movie movie=new Movie();
        movie.setId(jsonMovie.getInt("id"));
        movie.setTitle(jsonMovie.getString("title"));
        movie.setOverview(jsonMovie.getString("overview"));
        movie.setDate(jsonMovie.getString("release_date"));
        movie.setRating(jsonMovie.getDouble("vote_average"));
        movie.setImageUrl(jsonMovie.getString("poster_path"));
        movies[i]=movie;
    }
    return movies;
}


    public void insertData(){
        ContentValues[] movieValuesArr = new ContentValues[movie.length];
        // Loop through static array of Flavors, add each to an instance of ContentValues
        // in the array of ContentValues
        for(int i = 0; i < movie.length; i++){
            movieValuesArr[i] = new ContentValues();
            movieValuesArr[i].put(MovieContract.MovieEntry.COLUMN_ICON, movie[i].getImageUrl());
            movieValuesArr[i].put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                    movie[i].getId());
            movieValuesArr[i].put(MovieContract.MovieEntry.COLUMN_DESCRIPTION,
                    movie[i].getOverview());
            movieValuesArr[i].put(MovieContract.MovieEntry.COLUMN_TITLE,
                    movie[i].getTitle());
            movieValuesArr[i].put(MovieContract.MovieEntry.COLUMN_RATING,
                    movie[i].getRating());
            movieValuesArr[i].put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
                    movie[i].getDate());
        }

        // bulkInsert our ContentValues array
        getApplication().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI,
                movieValuesArr);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArray(movieKey, movie);
        super.onSaveInstanceState(outState);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
          if(mSort=="popularity.desc") {
            menu.findItem(R.id.action_popular).setChecked(true);
        }else if(mSort=="vote_count.desc"){
            menu.findItem(R.id.action_rating).setChecked(true);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case  R.id.action_settings:
                return true;
            case R.id.action_popular:
                if(isNetworkAvaible()) {
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                mSort="popularity.desc";
                getMovieData(mSort);
                  } else{
                    Toast.makeText(this, "Network isn't available", Toast.LENGTH_SHORT).show();      return true;
                      }
                 return true;
            case R.id.action_rating:
                if(isNetworkAvaible()) {
                    if (item.isChecked())
                        item.setChecked(false);
                    else item.setChecked(true);
                    mSort = "vote_count.desc";
                    getMovieData(mSort);
                } else{
                    Toast.makeText(this, "Network isn't available", Toast.LENGTH_SHORT).show();
                    }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
