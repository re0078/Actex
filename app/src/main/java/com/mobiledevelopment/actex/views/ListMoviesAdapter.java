package com.mobiledevelopment.actex.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.models.Movie;
import com.mobiledevelopment.actex.utils.ListApiUtil;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Data
public class ListMoviesAdapter extends RecyclerView.Adapter<MovieViewHolderHorizontal> {
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342";

    @NonNull
    private List<Movie> movieList;
    @Setter
    private OnMovieClickListener listener;
    private ListApiUtil listApiUtil;
    @NonNull
    private FragmentActivity activity;

    @NonNull
    @Override
    public MovieViewHolderHorizontal onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.movie_layout, parent, false);
        if (listApiUtil == null) {
            listApiUtil = ListApiUtil.getInstance();
        }
        return new MovieViewHolderHorizontal(contactView, movieList, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolderHorizontal holder, int position) {
        Movie movie = movieList.get(position);
        TextView textView = holder.getNameTextView();
        textView.setText(movie.getTitle());
        final ImageView imageView = holder.getMovieImage();
        holder.getMovieImage().setOnClickListener(view -> {
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            Fragment fragment = MovieFragment.newInstance(movie);
            ft.replace(R.id.fl_main_fragment, fragment).addToBackStack(null).commit();
        });
        holder.getDeleteButton().setOnClickListener(view -> {
            switch (movie.getListType()) {
                case FAVORITE:
                    deleteFavoriteItem(movie.getId(), position);
                    break;
                case WATCHLIST:
                    deleteWatchlistItem(movie.getId(), position);
                    break;
                default:
                    deleteItem(movie.getId(), movie.getListId(), position);

            }
        });
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

    private void deleteItem(int itemId, int listId, int position) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        activity.runOnUiThread(() -> listApiUtil.deleteListItem(itemId, listId,
                activity.getString(R.string.api_key), future));
        future.whenComplete((success, t) -> {
            if (Objects.nonNull(success)) {
                if (success) {
                    Toast.makeText(activity, "The item deleted successfully", Toast.LENGTH_SHORT).show();
                    movieList.remove(position);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(activity, "Couldn't remove the item from the playlist", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, "Unable to contact the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteFavoriteItem(int itemId, int position) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        activity.runOnUiThread(() -> listApiUtil.deleteFavoriteItem(itemId, activity.getString(R.string.api_key), future));
        future.whenComplete((success, t) -> {
            if (Objects.nonNull(success)) {
                if (success) {
                    Toast.makeText(activity, "The item deleted from favorites successfully", Toast.LENGTH_SHORT).show();
                    movieList.remove(position);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(activity, "Couldn't remove the item from the favorites", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, "Unable to contact the server", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void deleteWatchlistItem(int itemId, int position) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        activity.runOnUiThread(() -> listApiUtil.deleteWatchlistItem(itemId, activity.getString(R.string.api_key), future));
        future.whenComplete((success, t) -> {
            if (Objects.nonNull(success)) {
                if (success) {
                    Toast.makeText(activity, "The item deleted from watchlist successfully", Toast.LENGTH_SHORT).show();
                    movieList.remove(position);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(activity, "Couldn't remove the item from the watchlist", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, "Unable to contact the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
