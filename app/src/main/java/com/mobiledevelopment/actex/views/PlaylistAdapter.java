package com.mobiledevelopment.actex.views;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.models.Playlist;
import com.squareup.picasso.Picasso;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342";
    private static final PlaylistAdapter PLAYLIST_ADAPTER = new PlaylistAdapter();
    private List<Playlist> playlists;

    public static PlaylistAdapter getInstance() {
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
        holder.name.setText(playlist.getName());
        if (playlist.isFixedName()) {
            holder.name.setInputType(InputType.TYPE_NULL);
            holder.name.setEnabled(false);
        }
        Picasso.get()
                .load(IMAGE_BASE_URL + playlist.getPosterPath())
                .placeholder(R.drawable.loading)
                .error(R.drawable.ic_baseline_image_24)
                .into(holder.preview);
    }

    @Setter
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
