package com.mobiledevelopment.actex.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchResultAdapter extends ArrayAdapter<Movie> {
    private String imageBaseUrl = "https://image.tmdb.org/t/p/w92";

    private OnMovieClickListener listener;

    public void setOnClickListener(OnMovieClickListener listener) {
        this.listener = listener;
    }


    private static class ViewHolder {
        public ImageView ivCover;
        public TextView tvTitle;
        public TextView tvReleaseDate;
    }

    public SearchResultAdapter(@NonNull Context context, ArrayList<Movie> arrMovie) {
        super(context, 0, arrMovie);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Movie movie = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_search_res, parent, false);
            viewHolder.ivCover = (ImageView) convertView.findViewById(R.id.ivMovieCover);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvReleaseDate = (TextView) convertView.findViewById(R.id.tvReleaseDate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvReleaseDate.setText(movie.getReleaseDate());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMovieClick(movie);
            }
        });
        Picasso.get().load(imageBaseUrl+movie.getPosterPath()).placeholder(R.drawable.loading).error(R.drawable.ic_baseline_image_24).into(viewHolder.ivCover);
        return convertView;
    }
}
