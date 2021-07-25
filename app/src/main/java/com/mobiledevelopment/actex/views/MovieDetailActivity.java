package com.mobiledevelopment.actex.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.mobiledevelopment.actex.R;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        OnMovieClickListener onMovieClickListener = movie -> {
            Fragment fragment = MovieFragment.newInstance(movie);
            ft.replace(R.id.fl_main_fragment, fragment).addToBackStack(null).commit();
        };
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_main_fragment, TrendListsFragment.newInstance(onMovieClickListener))
                .addToBackStack(null)
                .commit();
    }
}