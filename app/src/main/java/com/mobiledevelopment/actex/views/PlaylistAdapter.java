package com.mobiledevelopment.actex.views;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.models.Playlist;
import com.mobiledevelopment.actex.util.ApiUtil;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w342";
    private static final PlaylistAdapter PLAYLIST_ADAPTER = new PlaylistAdapter(ApiUtil.getInstance());
    private List<Playlist> playlists;
    @NonNull
    private ApiUtil apiUtil;
    private Activity activity;

    public static PlaylistAdapter getInstance(Activity activity) {
        PLAYLIST_ADAPTER.activity = activity;
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
//            holder.name.setInputType(InputType.TYPE_NULL);
//            holder.name.setEnabled(false);
            holder.delete.setVisibility(View.INVISIBLE);
        } else {
            holder.delete.setOnClickListener(view -> deletePlaylist(String.valueOf(playlist.getId()),
                    activity.getString(R.string.api_key), position));
        }
        Picasso.get()
                .load(IMAGE_BASE_URL + playlist.getPosterPath())
                .placeholder(R.drawable.loading)
                .error(R.drawable.ic_baseline_image_24)
                .into(holder.preview);
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
            play.setOnClickListener(view -> {
                //TODO movie list
            });
        }
    }

    private void deletePlaylist(String listId, String apiKey, int position) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        activity.runOnUiThread(() -> apiUtil.deletePlaylist(listId, apiKey, future));
        future.whenComplete((result, t) -> {
            if (Objects.nonNull(result)) {
                if (result) {
                    Toast.makeText(activity, "The playlist deleted successfully", Toast.LENGTH_SHORT).show();
                    playlists.remove(position);
                    notifyItemRangeChanged(position, playlists.size() - position - 1);
                } else {
                    Toast.makeText(activity, "Failed to delete the playlist", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(activity, "Unable to contact the server", Toast.LENGTH_LONG).show();
            }
        });
    }
}
