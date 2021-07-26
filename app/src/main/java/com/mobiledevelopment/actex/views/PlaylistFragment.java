package com.mobiledevelopment.actex.views;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mobiledevelopment.actex.models.lists.PlaylistsResult;
import com.mobiledevelopment.actex.util.ApiUtil;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
        adapter = PlaylistAdapter.getInstance();
        adapter.setPlaylists(new ArrayList<>());
        listsApiEndpointInterface = RetrofitBuilder.getListApi();
        apiUtil = ApiUtil.getInstance();
        activity = getActivity();
        assert activity != null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View playlistFragment = inflater.inflate(R.layout.fragment_playlist, container, false);
        recyclerView = playlistFragment.findViewById(R.id.playlist_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(playlistFragment.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        getPlaylists();
        return playlistFragment;
    }

    private void getPlaylists() {
        CompletableFuture<PlaylistsResult> listsFuture = new CompletableFuture<>();
        activity.runOnUiThread(() -> apiUtil.getPlaylists(getString(R.string.api_key), listsFuture));
        listsFuture.whenComplete((result, t) -> {
            if (Objects.nonNull(result)) {
                if (result.getResults().isEmpty()) {
                    Toast.makeText(getContext(), "You don't have any playlists", Toast.LENGTH_LONG).show();
                } else {
                    adapter.setPlaylists(result.getResults());
                    adapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(getContext(), "Unable to fetch playlists", Toast.LENGTH_LONG).show();
            }
        });
    }
}
