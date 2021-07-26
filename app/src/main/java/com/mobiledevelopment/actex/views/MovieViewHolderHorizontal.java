package com.mobiledevelopment.actex.views;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.models.Movie;

import java.util.List;

import lombok.Getter;

@Getter
public class MovieViewHolderHorizontal extends RecyclerView.ViewHolder {
    private TextView nameTextView;
    private ImageView movieImage;
    private ImageButton deleteButton;


    public MovieViewHolderHorizontal(View itemView, List<Movie> movies, OnMovieClickListener listener) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.hor_movie_name_text);
        movieImage = itemView.findViewById(R.id.hor_movie_image);
        deleteButton = itemView.findViewById(R.id.delete_item_button);
        itemView.setOnClickListener(view -> {
            listener.onMovieClick(movies.get(getAdapterPosition()));
        });
    }
}
