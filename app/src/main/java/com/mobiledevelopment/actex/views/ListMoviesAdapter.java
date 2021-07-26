package com.mobiledevelopment.actex.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Data
public class ListMoviesAdapter extends RecyclerView.Adapter<MovieViewHolderHorizontal> {
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342";

    @NonNull
    private List<Movie> movieList;
    @Setter
    private OnMovieClickListener listener;

    @NonNull
    @Override
    public MovieViewHolderHorizontal onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.movie_layout, parent, false);
        return new MovieViewHolderHorizontal(contactView, movieList, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolderHorizontal holder, int position) {
        Movie movie = movieList.get(position);
        TextView textView = holder.nameTextView;
        textView.setText(movie.getTitle());
        final ImageView imageView = holder.movieImage;
        Picasso.get().load(IMAGE_BASE_URL + movie.getPosterPath())
                .placeholder(R.drawable.loading)
                .fit()
                .centerInside()
                .error(R.drawable.ic_baseline_image_24).into(imageView);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
