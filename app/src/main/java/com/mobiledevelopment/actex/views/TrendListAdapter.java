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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class TrendListAdapter extends RecyclerView.Adapter<MovieViewHolderVertical> {
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342";
    @NonNull
    @Getter
    private List<Movie> movieList;
    @Setter
    private OnMovieClickListener listener;

    @NonNull
    @Override
    public MovieViewHolderVertical onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolderVertical(contactView, movieList, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolderVertical holder, int position) {
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

    public void addAll(List<Movie> results) {
        movieList.addAll(results);
        this.notifyDataSetChanged();
    }


}
