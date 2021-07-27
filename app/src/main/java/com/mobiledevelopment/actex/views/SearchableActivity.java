package com.mobiledevelopment.actex.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

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
import retrofit2.internal.EverythingIsNonNull;

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
        adapter = new SearchResultAdapter(this, new ArrayList<>());
        adapter.setOnClickListener(movie -> {
            Log.i(TAG, "onItemClick: " + movie);
            Intent intent = new Intent(SearchableActivity.this, MovieDetailActivity.class);
            intent.putExtra("searchRes", movie);
            startActivity(intent);
        });
        searchResultLv.setAdapter(adapter);
        Intent intent = getIntent();
        if (intent.hasExtra("searchQuery")) {
            String query = (String) getIntent().getExtras().get("searchQuery");
            searchQuery(query);
        }
    }

    private void searchQuery(String query) {
        MovieListsApiEndpointInterface movieListApi = RetrofitBuilder.getMovieApi();
        Call<MovieList> movieList = movieListApi.getSearchResults(getResources().getString(R.string.api_key), query);
        movieList.enqueue(new Callback<MovieList>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<Movie> movies = response.body().results;
                        adapter.clear();
                        for (Movie movie : movies) {
                            adapter.add(movie);
                        }
                        adapter.notifyDataSetChanged();
                        if (movies.size() == 0)
                            findViewById(R.id.no_search_result_text).setVisibility(View.VISIBLE);
                    }
                } else Log.e(TAG, "no result" + response.code());
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });
    }
}
