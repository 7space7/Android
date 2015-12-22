package com.ua.viktor.spotify.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ua.viktor.spotify.R;
import com.ua.viktor.spotify.activity.MainActivity;
import com.ua.viktor.spotify.adapter.TrackAdapter;
import com.ua.viktor.spotify.model.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {
    public static final String TAG =DetailFragment.class.getName() ;
    TextView textView;
    View view;
    private String name="";
    private Track []mTracks;
    TrackAdapter mTrackAdapter;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private final String nameKey=DetailFragment.class.getName();
    Bundle savedInstanceState;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        Bundle arguments = getArguments();

        String position="";



        view=inflater.inflate(R.layout.fragment_detail, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view1);
        mRecyclerView.setHasFixedSize(true);


        mLayoutManager = new GridLayoutManager(view.getContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);


        textView=(TextView) view.findViewById(R.id.textView2);
        if (arguments != null) {
           position = arguments.getString(MainActivity.TAG);
            name=arguments.getString(MainActivity.TAG_NAME);
          //  Toast.makeText(getActivity(), "lol"+position, Toast.LENGTH_SHORT).show();
         //   getActivity().setTitle(name);
        }


       // textView.setText("" + position);
        String movieUrl="https://api.spotify.com/v1/artists/"+position+"/top-tracks?country=US";
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
                    final String json = response.body().string();
                   mTracks  = getTrack(json);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
         // Log.v("json",json);
                           mTrackAdapter= new TrackAdapter(mTracks);
                            mRecyclerView.setAdapter(mTrackAdapter);
                            mTrackAdapter.SetOnClickListener(new TrackAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    //   Toast.makeText(getActivity(),music[position].getImgUrl(),Toast.LENGTH_SHORT).show();
                                    PlayerFragment newFragment = new PlayerFragment();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                    if (!MainActivity.ismTwoPane()) {



                                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                        transaction.replace(R.id.movie_detail_container, newFragment);
                                        transaction.addToBackStack(null);
// Commit the transaction
                                        transaction.commit();
                                    } else {

                                        newFragment.show(fragmentManager, "dialog");

                                }
                                    Bundle args = new Bundle();
                                    args.putInt("lol", position);
                                    args.putParcelableArray(TAG, mTracks);
                                    newFragment.setArguments(args);
                                }
                            });


                        }
                    });

                } catch (IOException e) {
                    Log.e(MainActivity.TAG, "EROR", e);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        // Set title


            getActivity()
                    .setTitle(name);

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        //outState.putString(nameKey, name);
    }

    private Track[] getTrack(String json) throws JSONException {
        JSONObject jsonObject=new JSONObject(json);
        JSONArray data=jsonObject.getJSONArray("tracks");
        Track[]track=new Track[data.length()];


        for (int i=0;i<data.length();i++){
            JSONObject jsonMusic=data.getJSONObject(i);
            Track music=new Track();
            music.setAlbum(jsonMusic.getString("name"));
            music.setPreviewUrl(jsonMusic.getString("preview_url"));
            JSONObject album=jsonMusic.getJSONObject("album");
            music.setNameSong(album.getString("name"));
            track[i]=music;
            Log.v(TAG, track[i].getAlbum());
            Log.v(TAG, track[i].getNameSong());
            JSONArray imageData =album.getJSONArray("images");

            for(int j=0;j<imageData.length();j++){
                JSONObject jsonImage=imageData.getJSONObject(j);
                int height = jsonImage.getInt("width");
                if(height>=300) {
                    music.setImageSmall(jsonImage.getString("url"));
                    Log.v(TAG, track[i].getImageSmall());

                }
                else if(height>=600){
                    music.setImageBig(jsonImage.getString("url"));
                    Log.v(TAG, track[i].getImageBig());
                }

                track[i]=music;
            }

        }
        return track;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}
