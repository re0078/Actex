package com.mobiledevelopment.actex.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.mobiledevelopment.actex.R;
import com.mobiledevelopment.actex.clients.MovieListsApiEndpointInterface;
import com.mobiledevelopment.actex.clients.RetrofitBuilder;
import com.mobiledevelopment.actex.models.Movie;
import com.mobiledevelopment.actex.models.lists.MovieList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchableActivity extends AppCompatActivity {

    private static final String TAG = "SEARCHABLE_ACTIVITY";
    private ListView searchResultLv;
    private SearchResultAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable); // TODO
        Log.i(TAG, "onCreate: ");
        searchResultLv = findViewById(R.id.search_result_lv);
        adapter = new SearchResultAdapter(this, new ArrayList<Movie>());
        OnMovieClickListener onMovieClickListener = movie -> {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = MovieFragment.newInstance(movie);
            ft.replace(R.id.fl_main_fragment, fragment).addToBackStack(null).commit();
        };
        adapter.setOnClickListener(onMovieClickListener);
        searchResultLv.setAdapter(adapter);
        Intent intent = getIntent();
        if (intent.hasExtra("searchQuery")){
            String query = (String)getIntent().getExtras().get("searchQuery");
            searchQuery(query);
        }
    }

    private void searchQuery(String query) {
        MovieListsApiEndpointInterface movieListApi = RetrofitBuilder.getMovieApi();
        Call<MovieList> movieList = movieListApi.getSearchResults(getResources().getString(R.string.api_key), query);
        movieList.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<Movie> movies = response.body().results;
                        adapter.clear();
                        for (Movie movie : movies) {
                            adapter.add(movie);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else Log.e(TAG, "no result" + response.code());
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
    }
}
