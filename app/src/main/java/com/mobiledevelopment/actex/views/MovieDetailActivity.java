package com.mobiledevelopment.actex.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.models.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    final String TAG = "Movie Detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        OnMovieClickListener onMovieClickListener = movie -> {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = MovieFragment.newInstance(movie);
            ft.replace(R.id.fl_main_fragment, fragment).addToBackStack(null).commit();
        };
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_main_fragment, TrendListsFragment.newInstance(onMovieClickListener))
                .addToBackStack(null)
                .commit();
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        Fragment fragment;
        if (intent.hasExtra("searchRes")) {
            Movie movie = (Movie) getIntent().getExtras().getSerializable("searchRes");
            fragment = MovieFragment.newInstance(movie);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fl_main_fragment, fragment).commit();
        }
    }
}