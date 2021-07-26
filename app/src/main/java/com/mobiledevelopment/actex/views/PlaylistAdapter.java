package com.mobiledevelopment.actex.views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.models.Movie;
import com.mobiledevelopment.actex.models.Playlist;
import com.mobiledevelopment.actex.util.ApiUtil;
import com.mobiledevelopment.actex.util.ListApiUtil;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342";
    private static final PlaylistAdapter PLAYLIST_ADAPTER =
            new PlaylistAdapter(ApiUtil.getInstance(), ListApiUtil.getInstance());
    private List<Playlist> playlists;
    @NonNull
    private ApiUtil apiUtil;
    @NonNull
    private ListApiUtil listApiUtil;
    private FragmentActivity activity;
    private Gson gson;

    public static PlaylistAdapter getInstance(FragmentActivity activity) {
        PLAYLIST_ADAPTER.activity = activity;
        PLAYLIST_ADAPTER.gson = new Gson();
        return PLAYLIST_ADAPTER;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View playlistView = inflater.inflate(R.layout.playlist_layout, parent, false);
        return new PlaylistViewHolder(playlistView);
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        setupViewHolderAttributes(playlist, holder, position);
    }

    @Setter
    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView preview;
        private ImageButton play;
        private ImageButton delete;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.playlist_name);
            preview = itemView.findViewById(R.id.playlist_preview);
            play = itemView.findViewById(R.id.playlist_play);
            delete = itemView.findViewById(R.id.delete_playlist_button);
        }
    }

    private void setupViewHolderAttributes(Playlist playlist, PlaylistViewHolder holder, int position) {
        holder.name.setText(playlist.getName());
        if (playlist.isFixedName()) {
            holder.delete.setVisibility(View.INVISIBLE);
        } else {
            holder.delete.setOnClickListener(view -> deletePlaylist(String.valueOf(playlist.getId()),
                    activity.getString(R.string.api_key), position));
        }
        holder.play.setOnClickListener(view -> {
            PlaylistDetailedFragment destFragment = PlaylistDetailedFragment.newInstance();
            Bundle args = new Bundle();
            args.putInt(activity.getString(R.string.movies_count_bundle_key), playlist.getMovies().size());
            for (int i = 0; i < playlist.getMovies().size(); i++) {
                args.putSerializable(String.valueOf(i), playlist.getMovies().get(i));
            }
            args.putString(activity.getString(R.string.list_name_bundle_key), playlist.getName());
            destFragment.setArguments(args);
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_main_fragment, destFragment)
                    .addToBackStack(null)
                    .commit();
        });

        if (playlist.getMovies().isEmpty()){
            Picasso.get()
                    .load(R.drawable.ic_baseline_image_24)
                    .into(holder.preview);
        } else {
            Picasso.get()
                    .load(IMAGE_BASE_URL + playlist.getMovies().get(0).getPosterPath())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.ic_baseline_image_24)
                    .into(holder.preview);
        }
    }

    private void deletePlaylist(String listId, String apiKey, int position) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        activity.runOnUiThread(() -> listApiUtil.deletePlaylist(listId, apiKey, future));
        future.whenComplete((result, t) -> {
            if (Objects.nonNull(result)) {
                if (result) {
                    Toast.makeText(activity, "The playlist deleted successfully", Toast.LENGTH_SHORT).show();
                    playlists.remove(position);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(activity, "Failed to delete the playlist", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(activity, "Unable to contact the server", Toast.LENGTH_LONG).show();
            }
        });
    }
}
