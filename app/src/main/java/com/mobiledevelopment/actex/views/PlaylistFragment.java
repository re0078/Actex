package com.mobiledevelopment.actex.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.clients.ListsApiEndpointInterface;
import com.mobiledevelopment.actex.clients.RetrofitBuilder;
import com.mobiledevelopment.actex.models.Movie;
import com.mobiledevelopment.actex.models.lists.ListResponse;
import com.mobiledevelopment.actex.models.Playlist;
import com.mobiledevelopment.actex.util.ApiUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlaylistFragment extends Fragment {
    private static final String TAG = "PLAYLIST_FRAGMENT";
    ListsApiEndpointInterface listsApiEndpointInterface;
    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private ApiUtil apiUtil;
    private Activity activity;

    public static PlaylistFragment newInstance() {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        assert activity != null;
        adapter = PlaylistAdapter.getInstance(activity);
        adapter.setPlaylists(new ArrayList<>());
        listsApiEndpointInterface = RetrofitBuilder.getListApi();
        apiUtil = ApiUtil.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View playlistFragment = inflater.inflate(R.layout.fragment_playlist, container, false);
        recyclerView = playlistFragment.findViewById(R.id.playlist_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(playlistFragment.getContext()));
        recyclerView.setAdapter(adapter);
        getFavoriteMovies();
        return playlistFragment;
    }

    private void getFavoriteMovies() {
        CompletableFuture<ListResponse<Movie>> moviesFuture = new CompletableFuture<>();
        activity.runOnUiThread(() -> apiUtil.getFavoriteMovies(getString(R.string.api_key), moviesFuture));
        moviesFuture.whenComplete((result, t) -> {
            if (Objects.nonNull(result)) {
                if (result.getResults().isEmpty()) {
                    Toast.makeText(getContext(), "You don't have any favorites", Toast.LENGTH_LONG).show();
                } else {
                    Playlist favoriteMoviesPlaylist = Playlist.builder()
                            .name(getString(R.string.favorite_list_title))
                            .itemCount(result.getResults().size())
                            .fixedName(true)
                            .posterPath((String) result.getResults().get(0).getPosterPath()).build();
                    adapter.setPlaylists(new ArrayList<>());
                    adapter.getPlaylists().add(favoriteMoviesPlaylist);
                    adapter.notifyItemInserted(0);
                }
            } else {
                Toast.makeText(getContext(), "Unable to fetch playlists", Toast.LENGTH_LONG).show();
            }
            getWatchlistMovies();
        });
    }

    private void getWatchlistMovies() {
        CompletableFuture<ListResponse<Movie>> moviesFuture = new CompletableFuture<>();
        activity.runOnUiThread(() -> apiUtil.getWatchlistMovies(getString(R.string.api_key), moviesFuture));
        moviesFuture.whenComplete((result, t) -> {
            if (Objects.nonNull(result)) {
                if (result.getResults().isEmpty()) {
                    Toast.makeText(getContext(), "Your watchlist is empty", Toast.LENGTH_LONG).show();
                } else {
                    Playlist watchlistMoviesPlaylist = Playlist.builder()
                            .name(getString(R.string.favorite_list_title))
                            .itemCount(result.getResults().size())
                            .fixedName(true)
                            .posterPath((String) result.getResults().get(0).getPosterPath()).build();
                    adapter.setPlaylists(new ArrayList<>());
                    adapter.getPlaylists().add(watchlistMoviesPlaylist);
                    adapter.notifyItemInserted(1);
                }
            } else {
                Toast.makeText(getContext(), "Unable to fetch playlists", Toast.LENGTH_LONG).show();
            }
            getPlaylists();
        });
    }

    private void getPlaylists() {
        CompletableFuture<ListResponse<Playlist>> listsFuture = new CompletableFuture<>();
        activity.runOnUiThread(() -> apiUtil.getPlaylists(getString(R.string.api_key), listsFuture));
        listsFuture.whenComplete((result, t) -> {
            if (Objects.nonNull(result)) {
                List<Playlist> playlists = result.getResults();
                assert playlists != null;
                if (playlists.isEmpty()) {
                    Toast.makeText(getContext(), "You don't have any playlists", Toast.LENGTH_LONG).show();
                } else {
                    int currentSize = adapter.getPlaylists().size();
                    adapter.getPlaylists().addAll(playlists);
                    adapter.notifyItemRangeInserted(currentSize, playlists.size());
                }
            } else {
                Toast.makeText(getContext(), "Unable to fetch playlists", Toast.LENGTH_LONG).show();
            }
        });
    }
}
