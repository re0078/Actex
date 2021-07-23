package com.mobiledevelopment.actex.views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomeListAdapter extends RecyclerView.Adapter<CustomeListAdapter.ViewHolder> {

    private String imageBaseUrl = "https://image.tmdb.org/t/p/w342";

    private List<Movie> mMovies;

    public CustomeListAdapter(List<Movie> movies) {
        mMovies = movies;
    }


    private OnMovieClickListener listener;

    public void setOnClickListener(OnMovieClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public ImageView movieImage;

        public ViewHolder(View itemView) {
            super(itemView);
//            nameTextView = itemView.findViewById(R.id.movieNameText);
//            movieImage = itemView.findViewById(R.id.movieImage);
            itemView.setOnClickListener(view -> {
                Log.e("click", "MovieAdapter");
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onMovieClick(mMovies.get(position));
                }
            });
        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
//        View contactView = inflater.inflate(R.layout.item_movie, parent, false);
//        return new ViewHolder(contactView);
        return new ViewHolder(new Button(context)); // TODO
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        TextView textView = holder.nameTextView;
        textView.setText(movie.getTitle());
        final ImageView imageView = holder.movieImage;
//        Picasso.get().load(imageBaseUrl+movie.getPosterPath()).placeholder(R.drawable.loading_placeholder).error(R.drawable.ic_baseline_image_24).into(imageView);

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }
    public void addAll(List<Movie> results) {
        mMovies.addAll(results);
        this.notifyDataSetChanged();
    }
    public void clear(){
        mMovies.clear();
        this.notifyDataSetChanged();
    }

}
