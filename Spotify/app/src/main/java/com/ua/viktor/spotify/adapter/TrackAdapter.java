package com.ua.viktor.spotify.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ua.viktor.spotify.R;
import com.ua.viktor.spotify.activity.MainActivity;
import com.ua.viktor.spotify.model.Track;

/**
 * Created by viktor on 02.09.15.
 */
public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {
    private Track[]mTracks;
    OnItemClickListener mOnClickListener;
    private MainActivity main;

    @Override
    public TrackAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v,viewGroup.getContext());
        return viewHolder;
    }
    public TrackAdapter(Track[]tracks){

        this.mTracks=tracks;
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        Track track=mTracks[i];
        viewHolder.tvspecies.setText(track.getAlbum());
        viewHolder.nameAlbum.setText(track.getNameSong());
        Context context=viewHolder.imgThumbnail.getContext();

        if(track.getImageSmall()!=null) {
            Glide.with(context).load(track.getImageSmall()).
                    into(viewHolder.imgThumbnail);
        }else {
            Glide.with(context).load(R.drawable.ic_music).into(viewHolder.imgThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return mTracks.length;
    }


    public  interface OnItemClickListener{
        public void onItemClick(View view,int position);
    }
    public void SetOnClickListener(final OnItemClickListener onItemClickListener){
        this.mOnClickListener=onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        public ImageView imgThumbnail;
        public TextView tvspecies;
        public TextView nameAlbum;
        Context contxt;
        public ViewHolder(View itemView,Context context) {
            super(itemView);
            contxt = context;
            nameAlbum=(TextView)itemView.findViewById(R.id.textView3);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.imageView);
            tvspecies = (TextView)itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if(mOnClickListener!=null){
                mOnClickListener.onItemClick(v,getLayoutPosition());
            }
        }
    }
}
