package com.mobiledevelopment.actex.views;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.clients.MovieListBuilder;
import com.mobiledevelopment.actex.models.TrendListType;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;


public class TrendListsFragment extends Fragment {

    RecyclerView recent;
    RecyclerView mostPopular;
    RecyclerView topRated;
    Resources res;
    private OnMovieClickListener onMovieClickListener;

    public TrendListsFragment() {
    }

    public static TrendListsFragment newInstance(OnMovieClickListener listener) {
        TrendListsFragment fragment = new TrendListsFragment();
        Bundle args = new Bundle();
        fragment.onMovieClickListener  = listener;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View listFragment = inflater.inflate(R.layout.fragment_lists, container, false);
//        recent = listFragment.findViewById(R.id.recently_played_recycler);
//        mostPopular = listFragment.findViewById(R.id.most_popular_recycler);
//        topRated = listFragment.findViewById(R.id.top_rated_recycler);
//        res = getResources();
//        ArrayList<RecyclerView> recyclerViews = setAdapters();
//        MovieListBuilder movieListBuilder = new MovieListBuilder(res.getString(R.string.api_key));
//        prepareLists(movieListBuilder, recyclerViews);
//        return listFragment;
        return inflater.inflate(R.layout.fragment_first, container, false); // TODO
    }

    public void prepareLists(MovieListBuilder movieListBuilder, ArrayList<RecyclerView> recyclerViews) {
        for (int i = 0; i < TrendListType.values().length; i++) {
            RecyclerView recyclerView = recyclerViews.get(i);
            TrendListType trendListType = TrendListType.values()[i];
            movieListBuilder.getMovieList(trendListType, trendListType.adapter);
            recyclerView.setAdapter(trendListType.adapter);
            trendListType.adapter.setOnClickListener(onMovieClickListener);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(new SlideInLeftAnimator());
            Log.i("listDownloadStarted", "downloading");
        }
    };

    public ArrayList<RecyclerView> setAdapters() {
        ArrayList<RecyclerView> res = new ArrayList<>();
        res.add(recent);
        res.add(mostPopular);
        res.add(topRated);
        return res;

    }
}