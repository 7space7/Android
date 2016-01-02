package com.ua.viktor.movies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ua.viktor.movies.R;

/**
 * Created by viktor on 14.10.15.
 */
public class MovieCursorAdapter extends CursorRecyclerViewAdapter<MovieCursorAdapter.ViewHolder>
       {
        Context mContext;
        ViewHolder mVh;
public MovieCursorAdapter(Context context, Cursor cursor){
        super(context, cursor);
        mContext = context;
        }

public static class ViewHolder extends RecyclerView.ViewHolder
       {
    public TextView mTextView;
   // public CircleImageView mImageview;
    public ViewHolder(View view){
        super(view);
     //   mTextView = (TextView) view.findViewById(R.id.planet_name);
       // mImageview = (CircleImageView) view.findViewById(R.id.planet_image);
    }
}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        mVh = vh;
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor){
        DatabaseUtils.dumpCursor(cursor);
       // viewHolder.mTextView.setText(cursor.getString(
        //        cursor.getColumnIndex(PlanetColumns.NAME)));
       /// viewHolder.mImageview.setImageResource(cursor.getInt(cursor.getColumnIndex(
             //   PlanetColumns.IMAGE_RESOURCE)));
    }

}

