package com.ua.viktor.movies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ua.viktor.movies.R;
import com.ua.viktor.movies.model.Movie;
import com.ua.viktor.movies.ui.MovieDetail;

/**
 * Created by viktor on 19.07.15.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    public static final String MOVIE ="MOVIE" ;
    public static final String POSITION ="POSITION" ;
    private Movie[]mMovies;

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v,viewGroup.getContext());
        return viewHolder;
    }
    public MovieAdapter(Movie[]movies){
        mMovies=movies;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String movieUrl="http://image.tmdb.org/t/p/w342/";
        Movie movie=mMovies[i];
        viewHolder.tvspecies.setText(movie.getTitle());
        Context context=viewHolder.imgThumbnail.getContext();
        Picasso.with(context).load(movieUrl+movie.getImageUrl())
                .fit().centerInside()
                .into(viewHolder.imgThumbnail);

    }

    @Override
    public int getItemCount() {
        return mMovies.length;
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imgThumbnail;
        public TextView tvspecies;
        Context contxt;
        public ViewHolder(View itemView,Context context) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
             contxt = context;
            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            tvspecies = (TextView)itemView.findViewById(R.id.tv_species);
        }
        @Override
        public void onClick(View v) {

            Intent intent=new Intent(contxt,MovieDetail.class);
            intent.putExtra(POSITION,getPosition());
            intent.putExtra(MOVIE,mMovies);
            contxt.startActivity(intent);

        }
    }
}
