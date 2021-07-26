package com.mobiledevelopment.actex.views;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.models.Movie;

import java.util.List;

public class MovieViewHolderVertical extends RecyclerView.ViewHolder {
    public TextView nameTextView;
    public ImageView movieImage;

    public MovieViewHolderVertical(View itemView, List<Movie> mMovies, OnMovieClickListener listener) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.ver_movie_name_text);
        movieImage = itemView.findViewById(R.id.ver_movie_image);
        itemView.setOnClickListener(view -> {
            Log.e("click", "MovieAdapter");
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                listener.onMovieClick(mMovies.get(position));
            }
        });
    }
}
