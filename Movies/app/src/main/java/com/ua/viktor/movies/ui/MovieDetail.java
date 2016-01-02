package com.ua.viktor.movies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.ua.viktor.movies.R;
import com.ua.viktor.movies.adapter.MovieAdapter;
import com.ua.viktor.movies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;


public class MovieDetail extends ActionBarActivity {
    private static final String TAG =MovieDetail.class.getSimpleName();
    private Movie[] mMovies;
    private TextView mTextView;
    private TextView yearText;
    private TextView ratingText;
    private ImageView mImageView;
    private TextView overviewText;
    private ImageButton mButton;
    private ShareActionProvider mShareActionProvider;
    private RatingBar mRatingBar;
    private int mPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);



        mRatingBar=(RatingBar)findViewById(R.id.ratingBar);
        mButton=(ImageButton)findViewById(R.id.imageButton);
        ratingText=(TextView)findViewById(R.id.ratingText);
        yearText=(TextView)findViewById(R.id.yearText);
        mImageView=(ImageView)findViewById(R.id.imageView);
        mTextView=(TextView)findViewById(R.id.etTodo);
        overviewText=(TextView)findViewById(R.id.descView);




        Intent intent=getIntent();
        mPosition=intent.getIntExtra(MovieAdapter.POSITION, 0);
       // Toast.makeText(this, "The Item Clicked is: " + mPosition, Toast.LENGTH_SHORT).show();
        Parcelable[]parceble=intent.getParcelableArrayExtra(MovieAdapter.MOVIE);
        mMovies= Arrays.copyOf(parceble, parceble.length, Movie[].class);

        setTitle(mMovies[mPosition].getTitle());

        ratingText.setText(mMovies[mPosition].getRating() + "/10");
        mTextView.setText(mMovies[mPosition].getTitle());
        yearText.setText(mMovies[mPosition].getDate());
        overviewText.setText(mMovies[mPosition].getOverview());
        String movieUrl="http://image.tmdb.org/t/p/w342/";
        mRatingBar.setNumStars(5);

        mRatingBar.setStepSize((float) 0.1);
        mRatingBar.setRating((int) Math.round(mMovies[mPosition].getRating() / 2));
        mRatingBar.setFocusable(false);

       Picasso.with(this)
                .load(movieUrl+mMovies[mPosition].getImageUrl())
                .into(mImageView);
        getMovieTrailer(mMovies[mPosition].getId());

    }


   public void getMovieTrailer(int movieId){
    String api_key="0046de4ee266fbe5d82529513745db70";
    String movieUrl="http://api.themoviedb.org/3/movie/"+movieId+"/videos?api_key="+api_key;
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
                String json = response.body().string();
                JSONObject jsonObject=new JSONObject(json);
                JSONArray data=jsonObject.getJSONArray("results");
                final String []key=new String[data.length()];
                for(int i=0;i<data.length();i++) {
             JSONObject jsonTrailer = data.getJSONObject(i);

             key[i] = jsonTrailer.getString("key");
               }
      if(key.length!=0) {
      mButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + key[0]));
            intent.putExtra("VIDEO_ID", key[0]);
            startActivity(intent);
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

   }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menu.findItem(R.id.action_settings).setVisible(false);
        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        mShareActionProvider.setShareIntent(createShareForecastIntent());
        return  true;
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        String url="https://www.themoviedb.org/movie/"+mMovies[mPosition].getId();
        shareIntent.putExtra(Intent.EXTRA_TEXT,url);
        return shareIntent;
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
