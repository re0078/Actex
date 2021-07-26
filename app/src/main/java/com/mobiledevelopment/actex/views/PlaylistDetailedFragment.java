package com.mobiledevelopment.actex.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.clients.ListsApiEndpointInterface;
import com.mobiledevelopment.actex.clients.RetrofitBuilder;
import com.mobiledevelopment.actex.models.Movie;
import com.mobiledevelopment.actex.models.lists.ListResponse;
import com.mobiledevelopment.actex.models.lists.PlaylistResponse;
import com.mobiledevelopment.actex.util.ApiUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlaylistDetailedFragment extends Fragment {
    private FragmentActivity activity;
    private RecyclerView recyclerView;
    private ListMoviesAdapter adapter;
    private ApiUtil apiUtil;


    public static PlaylistDetailedFragment newInstance() {
        return new PlaylistDetailedFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        assert activity != null;
        apiUtil = ApiUtil.getInstance();
        if (adapter == null) {
            adapter = new ListMoviesAdapter(new ArrayList<>());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View playlistDetailedFragment = inflater.inflate(R.layout.fragment_playlist_movies, container, false);
        recyclerView = playlistDetailedFragment.findViewById(R.id.playlist_movies_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(playlistDetailedFragment.getContext()));
        recyclerView.setAdapter(adapter);
        assert getArguments() != null;
        TextView playlistTitle = playlistDetailedFragment.findViewById(R.id.playlist_title);
        int moviesCount = getArguments().getInt(getString(R.string.movies_count_bundle_key));
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < moviesCount; i++) {
            movies.add((Movie) getArguments().getSerializable(String.valueOf(i)));
        }
        String playlistName = getArguments().getString(getString(R.string.list_name_bundle_key));
        playlistTitle.setText(playlistName);
        adapter.setMovieList(movies);
        adapter.notifyDataSetChanged();
        return playlistDetailedFragment;
    }

//    private void fetchPlaylistMovies(int listId) {
//        CompletableFuture<PlaylistResponse> moviesFuture = new CompletableFuture<>();
//        activity.runOnUiThread(() -> apiUtil.fetchPlaylistMovies(listId, getString(R.string.api_key),
//                moviesFuture));
//        moviesFuture.whenComplete((movies, t) -> {
//            if (Objects.nonNull(movies)) {
//                List<Movie> fetchedMovies = movies.getItems();
//                if (fetchedMovies.isEmpty()) {
//                    Toast.makeText(getContext(), "Couldn't fetch playlist movies", Toast.LENGTH_LONG).show();
//                } else {
//                    adapter.setMovieList(fetchedMovies);
//                    adapter.notifyDataSetChanged();
//                }
//            } else {
//                Toast.makeText(getContext(), "Unable to contact the server", Toast.LENGTH_LONG).show();
//            }
//        });
//    }
}
