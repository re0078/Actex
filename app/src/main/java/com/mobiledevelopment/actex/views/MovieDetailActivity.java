package com.mobiledevelopment.actex.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.mobiledevelopment.actex.R;

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
    }
}