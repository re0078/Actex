package com.mobiledevelopment.actex.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.mobiledevelopment.actex.MainActivity;
import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.clients.MovieListBuilder;
import com.mobiledevelopment.actex.models.TrendListType;
import com.mobiledevelopment.actex.util.ApiUtil;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TrendListsFragment extends Fragment {
    private ApiUtil apiUtil;
    private RecyclerView recent;
    private RecyclerView mostPopular;
    private RecyclerView topRated;
    private FragmentActivity activity;
    @NonNull
    private final OnMovieClickListener onMovieClickListener;

    public static TrendListsFragment newInstance(OnMovieClickListener listener) {
        TrendListsFragment fragment = new TrendListsFragment(listener);
        fragment.apiUtil = ApiUtil.getInstance();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        assert activity != null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View listFragment = inflater.inflate(R.layout.fragment_lists, container, false);
        ImageButton playlists = listFragment.findViewById(R.id.playlist_list);
        ImageButton logout = listFragment.findViewById(R.id.logout_button);
        logout.setOnClickListener(view -> logoutProcess());
        playlists.setOnClickListener(view -> setupPlaylists());
        recent = listFragment.findViewById(R.id.recently_played_recycler);
        mostPopular = listFragment.findViewById(R.id.most_popular_recycler);
        topRated = listFragment.findViewById(R.id.top_rated_recycler);
        ArrayList<RecyclerView> recyclerViews = setAdapters();
        MovieListBuilder movieListBuilder = new MovieListBuilder(getResources().getString(R.string.api_key));
        prepareLists(movieListBuilder, recyclerViews);
        return listFragment;
    }

    private void prepareLists(MovieListBuilder movieListBuilder, ArrayList<RecyclerView> recyclerViews) {
        for (int i = 0; i < TrendListType.values().length; i++) {
            RecyclerView recyclerView = recyclerViews.get(i);
            TrendListType trendListType = TrendListType.values()[i];
            movieListBuilder.getMovieList(trendListType, trendListType.getAdapter());
            recyclerView.setAdapter(trendListType.getAdapter());
            trendListType.getAdapter().setListener(onMovieClickListener);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(new SlideInLeftAnimator());
            Log.i("listDownloadStarted", "downloading");
        }
    }

    private ArrayList<RecyclerView> setAdapters() {
        ArrayList<RecyclerView> res = new ArrayList<>();
        res.add(recent);
        res.add(mostPopular);
        res.add(topRated);
        return res;
    }

    private void logoutProcess() {
        CompletableFuture<Boolean> logoutFuture = new CompletableFuture<>();
        activity.runOnUiThread(() -> apiUtil.logout(getString(R.string.api_key), logoutFuture));
        logoutFuture.whenComplete((result, t) -> {
            if (Objects.nonNull(result)) {
                if (result) {
                    Toast.makeText(activity, "Logged out successfully", Toast.LENGTH_LONG)
                            .show();
                    Intent intent = new Intent(activity, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(activity, "Unbal to logout", Toast.LENGTH_LONG)
                            .show();
                }
            } else {
                Toast.makeText(activity, "Failed to connect to server", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void setupPlaylists() {
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_main_fragment, PlaylistFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}