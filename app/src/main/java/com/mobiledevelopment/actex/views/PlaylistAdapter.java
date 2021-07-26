package com.mobiledevelopment.actex.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.models.lists.MovieList;
import com.mobiledevelopment.actex.models.lists.Playlist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342";
    private List<Playlist> playlists;

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
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        Picasso.get()
                .load(IMAGE_BASE_URL + playlist.getPosterPath())
                .placeholder(R.drawable.loading)
                .error(R.drawable.ic_baseline_image_24)
                .into(holder.preview);
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        private EditText name;
        private ImageView preview;
        private ImageButton play;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (EditText) itemView.findViewById(R.id.playlist_name);
            preview = (ImageView) itemView.findViewById(R.id.playlist_preview);
            play = (ImageButton) itemView.findViewById(R.id.playlist_play);
            play.setOnClickListener(view -> {
                //TODO movie list
            });
        }
    }
}