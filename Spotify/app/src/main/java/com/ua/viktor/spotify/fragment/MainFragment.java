package com.ua.viktor.spotify.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ua.viktor.spotify.R;
import com.ua.viktor.spotify.activity.MainActivity;
import com.ua.viktor.spotify.adapter.MusicAdapter;
import com.ua.viktor.spotify.model.Music;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by viktor on 24.07.15.
 */
public class MainFragment extends Fragment   {
    private static final String TAG =MainActivity.class.getSimpleName() ;
   private Music[]music;
   private RecyclerView mRecyclerView;
   private RecyclerView.LayoutManager mLayoutManager;
   private MusicAdapter mMusicAdapter;
   private View view;
   private ImageView emptyView;
    private String str;
    Context context;
    private static String musicKey="music";
    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        emptyView = (ImageView) view.findViewById(R.id.empty_view);
        mRecyclerView.setHasFixedSize(true);


        // The number of Columns
        mLayoutManager = new GridLayoutManager(view.getContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        musicUI();
        if (savedInstanceState != null) {
            music = (Music[]) savedInstanceState.getParcelableArray(musicKey);
           // if (music != null) {
                musicUI();
           // }
        }
      /*  final EditText searchText = (EditText) view.findViewById(R.id.searchText);

        view.findViewById(R.id.searchButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        str=searchText.getText().toString();
                        if(!str.isEmpty()) {

                            getMusicData(str);
                        }
                    }
                }
        );*/


  return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getActivity();

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArray(musicKey, music);
        super.onSaveInstanceState(outState);
    }
   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String queryString) {
                        // Get the query string from searchView
                         str=queryString;
                        if(!str.isEmpty()) {

                            getMusicData(str);
                        }
                        searchView.clearFocus();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        return false;
                    }
                });

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void getMusicData(String search){
        if (!isNetworkAvailable(context)) {
            //Toast.makeText(context, "No Internet, please check your network connection", Toast.LENGTH_SHORT).show();
            //Log.v("No Internet, please check your network connection","lol");
        }else {
            String movieUrl = "https://api.spotify.com/v1/search?q=" + search + "&type=artist";
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
                        music = getMusic(json);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                musicUI();
                            }
                        });

                    } catch (IOException e) {
                        Log.e(TAG, "EROR", e);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

public void musicUI(){
    mMusicAdapter = new MusicAdapter(music);
    mRecyclerView.setAdapter(mMusicAdapter);
    if(music==null) {
        mRecyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }
    else {
        mRecyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }
    mMusicAdapter.SetOnClickListener(new MusicAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            //   Toast.makeText(getActivity(),music[position].getImgUrl(),Toast.LENGTH_SHORT).show();
            ((Commute) getActivity()).myMusic(music[position].getId(),music[position].getName());

        }
    });
}
    public  interface Commute

{
    public void myMusic(String id, String name);
}





    public Music[]getMusic(String json) throws JSONException {
        JSONObject jsonObject=new JSONObject(json);
        JSONObject jsonObject1=jsonObject.getJSONObject("artists");
        JSONArray data=jsonObject1.getJSONArray("items");
        Music[]musics=new Music[data.length()];


        for (int i=0;i<data.length();i++){
            JSONObject jsonMusic=data.getJSONObject(i);
            Music music=new Music();
            music.setId(jsonMusic.getString("id"));
            music.setName(jsonMusic.getString("name"));

            JSONArray imageData =jsonMusic.getJSONArray("images");
            musics[i]=music;
           // Log.v(TAG, musics[i].getName());
           // Log.v(TAG, musics[i].getId());
            for(int j=0;j<imageData.length();j++){
                JSONObject jsonImage=imageData.getJSONObject(j);
                int height = jsonImage.getInt("width");
               if(height>=300) {
                    music.setImgUrl(jsonImage.getString("url"));
                    musics[i]=music;
                   // Log.v(TAG, musics[i].getImgUrl());
                }
            }

        }
        return musics;
    }



}

