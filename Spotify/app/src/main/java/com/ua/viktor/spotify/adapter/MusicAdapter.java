package com.ua.viktor.spotify.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ua.viktor.spotify.model.Music;
import com.ua.viktor.spotify.R;
import com.ua.viktor.spotify.activity.MainActivity;

/**
 * Created by viktor on 24.07.15.
 */
public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder>{
    public static final String MOVIE ="MOVIE" ;
    public static final String POSITION ="POSITION" ;
    private Music[]mMusic;
    OnItemClickListener mOnClickListener;
    private MainActivity main;

    @Override
    public MusicAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v,viewGroup.getContext());
        return viewHolder;
    }
    public MusicAdapter(Music[]musics){

        this.mMusic=musics;
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        Music music=mMusic[i];
        viewHolder.tvspecies.setText(music.getName());
        Context context=viewHolder.imgThumbnail.getContext();

if(music.getImgUrl()!=null) {
    Glide.with(context).load(music.getImgUrl()).
            into(viewHolder.imgThumbnail);
}else {
    Glide.with(context).load(R.drawable.ic_music).into(viewHolder.imgThumbnail);
}
    }

    @Override
    public int getItemCount() {
        return mMusic.length;
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
        Context contxt;
        public ViewHolder(View itemView,Context context) {
            super(itemView);
            contxt = context;
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


